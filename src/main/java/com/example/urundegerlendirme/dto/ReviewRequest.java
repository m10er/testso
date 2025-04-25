package com.example.urundegerlendirme.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class ReviewRequest {
    @NotBlank(message = "Değerlendirme içeriği boş olamaz")
    private String content;

    @NotNull(message = "Ürün ID'si boş olamaz")
    private Long productId;
} 