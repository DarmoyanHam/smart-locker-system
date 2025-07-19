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

    @Column(name = "box_size")
    private BoxSize boxSize;

    @Column(name = "number")
    private int number;

    public void decidingEnumType(int num){
        if(num >= 1 &&  num <= 3){
            boxSize = BoxSize.SMALL;
        }else if(num > 3 && num <= 7){
            boxSize = BoxSize.MEDIUM;
        }else{
            boxSize = BoxSize.LARGE;
        }

    }

    public boolean isAlmostExpired(Duration threshold) {
        return isEmpty && lockedUntil != null &&
                LocalDateTime.now().isAfter(lockedUntil.minus(threshold));
    }

    public Box(){
        lockedUntil = null;
        isEmpty = true;
    }

}
