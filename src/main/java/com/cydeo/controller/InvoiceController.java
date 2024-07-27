package com.cydeo.controller;

import com.cydeo.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class InvoiceController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/sendInvoice")
    public String sendInvoiceEmail(@RequestParam String to, @RequestParam String subject, @RequestParam String text) {
        emailService.sendInvoiceEmail(to, subject, text);
        return "Invoice email sent successfully!";
    }
}
