package com.example.urundegerlendirme.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LikeRequest {
    @NotNull(message = "Değerlendirme ID'si boş olamaz")
    private Long reviewId;

    private boolean isPositive = true;
} 