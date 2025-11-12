package com.iblog.bean;

import java.time.LocalDateTime;

import lombok.*;

/**
 * @author fz
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    private Long id;
    private String cateName;
    private LocalDateTime date;
}
