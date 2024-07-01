package com.training.reportingManagement.service.serviceInterface;

public interface EmailService {
    void sendSimpleMessage(String to, String subject, String text);
}