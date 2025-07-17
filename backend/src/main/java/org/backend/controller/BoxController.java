package org.backend.controller;

import org.backend.MyException;
import org.backend.entity.Box;
import org.backend.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Duration;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/boxes")
public class BoxController {

    @Autowired
    private BoxService boxService;


    @PostMapping("/lock")
    public Box lockBox(@RequestParam(defaultValue = "5") int durationInMinutes) {
        return boxService.lockLocker(Duration.ofMinutes(durationInMinutes));
    }


    @PostMapping("/unlock-by-qr")
    public ResponseEntity<Box> unlockBoxByQr(@RequestParam String qrCodePath) {
        try {
            Box box = boxService.unlockByQr(qrCodePath);
            return ResponseEntity.ok(box);
        } catch (MyException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
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
