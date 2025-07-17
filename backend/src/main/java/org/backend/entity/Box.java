package org.backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Duration;
import java.time.LocalDateTime;


@Setter
@Getter
@Entity
@Table(name = "box")
public class Box {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_empty")
    private boolean isEmpty;

    @Column(name = "locked_until")
    private LocalDateTime lockedUntil;

    @Column(name = "qr_code_path")
    private String qrCodePath;

//    public void lock(Duration duration) {
//        this.isEmpty = true;
//        this.lockedUntil = LocalDateTime.now().plus(duration);
//    }
//
//    public void unlock() {
//        this.isEmpty = false;
//        this.lockedUntil = null;
//    }

    public boolean isAlmostExpired(Duration threshold) {
        return isEmpty && lockedUntil != null &&
                LocalDateTime.now().isAfter(lockedUntil.minus(threshold));
    }

    public Box(){
        lockedUntil = null;
        isEmpty = true;
    }

}
