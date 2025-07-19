package org.backend.service;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;
import jakarta.annotation.PostConstruct;
import org.backend.MyException;
import org.backend.entity.Box;
import org.backend.repository.BoxRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class BoxService {

    @Autowired
    private BoxRepository boxRepository;

    @Autowired
    private EmailNotifier emailNotifier;

    private final Map<Long, Box> isEmpty = new HashMap<>();
    private final Map<Long, Box> busy = new HashMap<>();
    private static final Duration NOTIFY_BEFORE = Duration.ofMinutes(1);
    @Value("${qr.codes.path}")
    private String qrCodes;

    @PostConstruct
    public void init() {
        initBoxesInMemory();
    }


    public void initBoxesInMemory() {
        List<Box> tempBoxes = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            Box box = new Box();
            box.setEmpty(true);
            box.setLockedUntil(null);
            tempBoxes.add(box);
        }

        List<Box> savedBoxes = boxRepository.saveAll(tempBoxes);

        isEmpty.clear();
        for (Box box : savedBoxes) {
            isEmpty.put(box.getId(), box);
        }

        System.out.println("Boxes initialized and saved.");
    }


    public String lockLockerAndReturnQrPath(Duration duration) {
        Optional<Map.Entry<Long, Box>> freeEntry = isEmpty.entrySet().stream().findFirst();

        if (freeEntry.isEmpty()) {
            throw new MyException("There are not empty boxes.");
        }

        Long id = freeEntry.get().getKey();
        Box box = freeEntry.get().getValue();

        box.setEmpty(false);
        box.setLockedUntil(LocalDateTime.now().plus(duration));

        try {
            String qrText = "boxId=" + id + "&expiresAt=" + box.getLockedUntil();
            String qrPath = QrGenerator.generateQrImage(qrText, qrCodes);
            box.setQrCodePath(qrPath);
        } catch (Exception e) {
            throw new MyException("QR Code generation failed: " + e.getMessage());
        }

        isEmpty.remove(id);
        busy.put(id, box);
        boxRepository.save(box);

        return box.getQrCodePath();
    }



    public Box unlockLockerByQrImage(MultipartFile file) {
        try {
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            LuminanceSource source = new BufferedImageLuminanceSource(bufferedImage);
            BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));

            Result result = new MultiFormatReader().decode(bitmap);
            String qrText = result.getText(); // например: "boxId=3&expiresAt=2025-07-19T14:35:12"

            Map<String, String> params = parseQrText(qrText);
            Long boxId = Long.parseLong(params.get("boxId"));

            Optional<Box> optionalBox = boxRepository.findById(boxId);
            if (optionalBox.isEmpty()) throw new MyException("Box not found");

            Box box = optionalBox.get();

            if (LocalDateTime.now().isAfter(box.getLockedUntil())) {
                throw new MyException("QR expired");
            }

            box.setEmpty(true);
            box.setLockedUntil(null);
            box.setQrCodePath(null);

            busy.remove(boxId);
            isEmpty.put(boxId, box);

            return boxRepository.save(box);

        } catch (IOException | NotFoundException e) {
            throw new MyException("Failed to decode QR image: " + e.getMessage());
        }
    }


    @Scheduled(fixedRate = 30000)
    public void notifyAdminAboutExpiringLockers() {
        List<Box> boxes = boxRepository.findByIsEmptyFalse();

        for (Box box : boxes) {
            if (box.isAlmostExpired(NOTIFY_BEFORE)) {
                emailNotifier.notifyExpiringLocker(box.getId(), box.getLockedUntil());
            }
        }
    }

    public Map<String, List<Box>> getBoxStatus() {
        List<Box> emptyList = new ArrayList<>(isEmpty.values());
        List<Box> busyList = new ArrayList<>(busy.values());

        Map<String, List<Box>> status = new HashMap<>();
        status.put("free", emptyList);
        status.put("busy", busyList);

        return status;
    }

    public List<Box> getExpiringBoxes(Duration threshold) {
        List<Box> expiring = new ArrayList<>();

        for (Box box : busy.values()) {
            if (box.isAlmostExpired(threshold)) {
                expiring.add(box);
            }
        }

        return expiring;
    }

    private Map<String, String> parseQrText(String text) {
        return Arrays.stream(text.split("&"))
                .map(s -> s.split("="))
                .collect(Collectors.toMap(kv -> kv[0], kv -> kv[1]));
    }

}
