package com.bus.controller;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.ui.Model;


import com.bus.model.Feedback;
import com.bus.service.FeedbackService;

@ExtendWith(MockitoExtension.class)
public class FeedbackControllerTest {

    @Mock
    private FeedbackService feedbackService;

    @Mock
    private Model model;

    @InjectMocks
    private FeedbackController feedbackController;

    @Test
    public void testShowFeedbackForm() {
        
        String viewName = feedbackController.showFeedbackForm(model);

        // Assert
        assertEquals("feedback", viewName);

       
        verify(model).addAttribute(eq("feedback"), any(Feedback.class));
    }

    @Test
    public void testSubmitFeedback() {
  
        Feedback feedback = new Feedback();
        String email = "user@example.com";

       
        String viewName = feedbackController.submitFeedback(feedback, email);

        // Assert
        verify(feedbackService).saveFeedback(feedback);
        assertEquals("redirect:/profile?email=user@example.com", viewName);
    }
}

