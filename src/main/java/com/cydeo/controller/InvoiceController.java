package com.cydeo.controller;

import com.cydeo.dto.InvoiceDto;
import com.cydeo.enums.InvoiceStatus;
import com.cydeo.service.EmailService;
import com.cydeo.service.InvoiceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
