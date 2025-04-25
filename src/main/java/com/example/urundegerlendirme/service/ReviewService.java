package com.example.urundegerlendirme.service;

import com.example.urundegerlendirme.dto.ReviewRequest;
import com.example.urundegerlendirme.model.Product;
import com.example.urundegerlendirme.model.Review;
import com.example.urundegerlendirme.model.User;
import com.example.urundegerlendirme.repository.ProductRepository;
import com.example.urundegerlendirme.repository.ReviewRepository;
import com.example.urundegerlendirme.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {
    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public List<Review> getProductReviews(Long productId) {
        return reviewRepository.findByProductId(productId);
    }

    public List<Review> getUserReviews(Long userId) {
        return reviewRepository.findByUserId(userId);
    }

    @Transactional
    public Review createReview(ReviewRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Ürün bulunamadı"));

        Review review = new Review();
        review.setContent(request.getContent());
        review.setUser(user);
        review.setProduct(product);

        return reviewRepository.save(review);
    }

    @Transactional
    public Review updateReview(Long reviewId, ReviewRequest request) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Değerlendirme bulunamadı"));

        if (!review.getUser().getId().equals(user.getId())) {
            throw new RuntimeException("Bu değerlendirmeyi düzenleme yetkiniz yok");
        }

        review.setContent(request.getContent());
        return reviewRepository.save(review);
    }

    @Transactional
    public void deleteReview(Long reviewId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı"));

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Değerlendirme bulunamadı"));

        if (!review.getUser().getId().equals(user.getId()) && !user.getRole().name().equals("ADMIN")) {
            throw new RuntimeException("Bu değerlendirmeyi silme yetkiniz yok");
        }

        reviewRepository.delete(review);
    }
} 