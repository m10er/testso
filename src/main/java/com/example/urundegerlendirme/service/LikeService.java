package com.example.urundegerlendirme.service;

import com.example.urundegerlendirme.dto.LikeRequest;
import com.example.urundegerlendirme.model.Like;
import com.example.urundegerlendirme.model.Review;
import com.example.urundegerlendirme.model.User;
import com.example.urundegerlendirme.repository.LikeRepository;
import com.example.urundegerlendirme.repository.ReviewRepository;
import com.example.urundegerlendirme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {
    private final LikeRepository likeRepository;
    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    @Transactional
    public Like createLike(LikeRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Review review = reviewRepository.findById(request.getReviewId())
                .orElseThrow(() -> new RuntimeException("Değerlendirme bulunamadı"));

        if (likeRepository.existsByUserIdAndReviewId(user.getId(), review.getId())) {
            throw new RuntimeException("Bu değerlendirmeyi zaten beğendiniz/beğenmediniz");
        }

        Like like = new Like();
        like.setUser(user);
        like.setReview(review);
        like.setPositive(request.isPositive());

        Like savedLike = likeRepository.save(like);

        // Kullanıcı puanını güncelle
        User reviewOwner = review.getUser();
        if (request.isPositive()) {
            reviewOwner.setPoints(reviewOwner.getPoints() + 1);
        } else {
            reviewOwner.setPoints(reviewOwner.getPoints() - 1);
        }
        userRepository.save(reviewOwner);

        return savedLike;
    }

    @Transactional
    public void deleteLike(Long reviewId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Like like = likeRepository.findByUserIdAndReviewId(user.getId(), reviewId)
                .orElseThrow(() -> new RuntimeException("Beğeni bulunamadı"));

        // Kullanıcı puanını güncelle
        User reviewOwner = like.getReview().getUser();
        if (like.isPositive()) {
            reviewOwner.setPoints(reviewOwner.getPoints() - 1);
        } else {
            reviewOwner.setPoints(reviewOwner.getPoints() + 1);
        }
        userRepository.save(reviewOwner);

        likeRepository.delete(like);
    }
} 