package com.bus.controller;



import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.bus.model.Feedback;
import com.bus.service.FeedbackService;

@Controller
public class FeedbackController {

    private final FeedbackService feedbackService;

 
    public FeedbackController(FeedbackService feedbackService) {
        this.feedbackService = feedbackService;
    }
    
   


    @GetMapping("/feedback")
    public String showFeedbackForm(Model model) {
        model.addAttribute("feedback", new Feedback());
        return "feedback";
    }


    
   
        
    @PostMapping("/submit-feedback")
    public String submitFeedback(@ModelAttribute Feedback feedback, @RequestParam String email) {
       
        feedbackService.saveFeedback(feedback);
        return "redirect:/profile?email=" + email; 
    }
    




}

