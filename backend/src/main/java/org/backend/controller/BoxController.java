package org.backend.controller;

import org.backend.entity.Box;
import org.backend.service.BoxService;
import org.springframework.beans.factory.annotation.Autowired;
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


    @PostMapping("/unlock/{id}")
    public Box unlockBox(@PathVariable Long id) {
        return boxService.unlockLocker(id);
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
