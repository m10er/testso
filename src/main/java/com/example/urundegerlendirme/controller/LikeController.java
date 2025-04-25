package com.example.urundegerlendirme.controller;

import com.example.urundegerlendirme.dto.LikeRequest;
import com.example.urundegerlendirme.model.Like;
import com.example.urundegerlendirme.service.LikeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/likes")
@RequiredArgsConstructor
public class LikeController {
    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Like> createLike(@Valid @RequestBody LikeRequest request) {
        return ResponseEntity.ok(likeService.createLike(request));
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteLike(@PathVariable Long reviewId) {
        likeService.deleteLike(reviewId);
        return ResponseEntity.ok().build();
    }
} 