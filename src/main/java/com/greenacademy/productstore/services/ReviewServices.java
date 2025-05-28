package com.greenacademy.productstore.services;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.greenacademy.productstore.models.Review;
import com.greenacademy.productstore.models.User;
import com.greenacademy.productstore.repositories.ReviewRepository;

@Service
public class ReviewServices {
    private ReviewRepository reviewRepository;

    public ReviewServices(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public void create(Review review, User user) {
        review.setUser(user);
        review.setUpvote(0);
        review.setDownvote(0);
        reviewRepository.save(review);
    }

    public Page<Review> getByProductId(Integer id, Pageable pageable) {
        return reviewRepository.findByProductId(id, pageable);
    }

    public Optional<Review> getByOrderItemId(Integer id) {
        return reviewRepository.findByOrderItemId(id);
    }

    public Double getAvarageByProductId(Integer id) {
        return reviewRepository.findAvarageByProductId(id);
    }

}
