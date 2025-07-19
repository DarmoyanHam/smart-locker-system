package org.backend.controller;

import org.backend.MyException;
import org.backend.entity.Box;
import org.backend.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/boxes")
public class BoxController {

    @Autowired
    private BoxService boxService;


    @PostMapping("/lock")
    public ResponseEntity<byte[]> lockBox(@RequestParam(defaultValue = "5") int durationInMinutes) {
        try {
            String qrPath = boxService.lockLockerAndReturnQrPath(Duration.ofMinutes(durationInMinutes));
            Path imagePath = Path.of(qrPath);
            byte[] imageBytes = java.nio.file.Files.readAllBytes(imagePath);

            return ResponseEntity
                    .ok()
                    .header("Content-Type", "image/png")
                    .body(imageBytes);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping(value = "/unlock-by-qr", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Box> unlockByQrFromImage(@RequestParam("file") MultipartFile file) {
        try {
            Box box = boxService.unlockLockerByQrImage(file);
            return ResponseEntity.ok(box);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(null);
        }
    }

    @GetMapping("/status")
    public Map<String, List<Box>> getBoxStatus() {
        return boxService.getBoxStatus();
    }

    @GetMapping("/expiring")
    public List<Box> getExpiringBoxes(@RequestParam(defaultValue = "1") long minutes) {
        return boxService.getExpiringBoxes(Duration.ofMinutes(minutes));
    }
}
