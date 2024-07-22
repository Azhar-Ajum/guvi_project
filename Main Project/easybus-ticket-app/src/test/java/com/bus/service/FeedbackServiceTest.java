package com.bus.service;

import com.bus.model.Feedback;
import com.bus.repository.FeedbackRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;



import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FeedbackServiceTest {

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private FeedbackService feedbackService;

    @Test
    void testSaveFeedback() {
        Feedback feedback = new Feedback();
        feedback.setName("John Doe");
        feedback.setEmail("john.doe@example.com");
        feedback.setFeedbackText("Great service!");

        feedbackService.saveFeedback(feedback);

        assertNotNull(feedback.getCreatedAt());
        verify(feedbackRepository, times(1)).save(feedback);
    }
}
