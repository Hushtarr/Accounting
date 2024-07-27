package com.cydeo.service;

public interface EmailService {
    void sendInvoiceEmail(String to, String subject, String text);
}
