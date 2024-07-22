package com.bus.service;




import java.time.LocalDateTime;


import org.springframework.stereotype.Service;

import com.bus.model.Feedback;
import com.bus.repository.FeedbackRepository;

@Service
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;

    
    public FeedbackService(FeedbackRepository feedbackRepository) {
        this.feedbackRepository = feedbackRepository;
    }

    public void saveFeedback(Feedback feedback) {
        feedback.setCreatedAt(LocalDateTime.now());
        feedbackRepository.save(feedback);
    }
}

