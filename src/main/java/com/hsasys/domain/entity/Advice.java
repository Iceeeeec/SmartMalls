package com.hsasys.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NonNull
@Builder
public class Advice
{
    private Long id;

    private Integer userId;

    private String advice;

    private LocalDateTime recommendedTime;
}
