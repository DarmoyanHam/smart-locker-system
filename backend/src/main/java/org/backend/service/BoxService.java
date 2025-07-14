package org.backend.service;

import jakarta.annotation.PostConstruct;
import org.backend.MyException;
import org.backend.entity.Box;
import org.backend.repository.LockerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BoxService {

    @Autowired
    private LockerRepository lockerRepository;

    @Autowired
    private EmailNotifier emailNotifier;

    private final Map<Long, Box> isEmpty = new HashMap<>();
    private final Map<Long, Box> busy = new HashMap<>();
    private static final Duration NOTIFY_BEFORE = Duration.ofMinutes(1);

    @PostConstruct
    public void init() {
        initBoxesInMemory();
        saveAllToDatabase();
    }


    public void initBoxesInMemory() {
        for (int i = 0; i < 10; i++) {
            Box box = new Box();
            box.setEmpty(true);
            box.setLockedUntil(null);
            isEmpty.put((long) i, box);
        }
    }


    public void saveAllToDatabase() {
        List<Box> boxList = new ArrayList<>(isEmpty.values());
        List<Box> savedBoxes = lockerRepository.saveAll(boxList);

        isEmpty.clear();
        for (Box box : savedBoxes) {
            isEmpty.put(box.getId(), box);
        }

        System.out.println("All boxes have been saved");
    }

    public Box lockLocker(Duration duration) {
        Optional<Map.Entry<Long, Box>> freeEntry = isEmpty.entrySet().stream().findFirst();

        if (freeEntry.isEmpty()) {
            throw new MyException("There are not empty boxes.");
        }

        Long id = freeEntry.get().getKey();
        Box box = freeEntry.get().getValue();

        box.setEmpty(false);
        box.setLockedUntil(LocalDateTime.now().plus(duration));
        isEmpty.remove(id);
        busy.put(id, box);
        lockerRepository.save(box);
        return box;
    }


    public Box unlockLocker(Long lockerId) {
        Box box = busy.get(lockerId);

        if (box == null) {
            throw new MyException("Box not found.");
        }

        box.setEmpty(true);
        box.setLockedUntil(null);

        busy.remove(lockerId);
        isEmpty.put(lockerId, box);

        System.out.println("Box with id " + lockerId + " has been unlocked.");
        lockerRepository.save(box);
        return box;
    }

    @Scheduled(fixedRate = 30000)
    public void notifyAdminAboutExpiringLockers() {
        List<Box> boxes = lockerRepository.findByLockedTrue();

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

}
