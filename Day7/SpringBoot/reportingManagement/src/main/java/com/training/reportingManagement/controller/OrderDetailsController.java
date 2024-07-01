package com.training.reportingManagement.controller;

import com.training.reportingManagement.dao.entity.Order_Details;
import com.training.reportingManagement.dto.OrderDateSummaryDTO;
import com.training.reportingManagement.dto.OrderSummaryDTO;
import com.training.reportingManagement.service.serviceInterface.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.time.LocalDate;

@RestController
@RequestMapping("/orderDetails")
public class OrderDetailsController {

    @Autowired
    private OrderDetailsService orderDetailsService;

    @GetMapping("/email/{emailId}")
    public OrderSummaryDTO getOrdersByEmailId(@PathVariable String emailId) {
        return orderDetailsService.getOrdersByEmailId(emailId);
    }

    @GetMapping("/order/{id}")
    public Order_Details getOrderByOrderId(@PathVariable int id) {
        return orderDetailsService.getOrderByOrderId(id);
    }

    @GetMapping("date/{date}")
    public ResponseEntity<?> getOrdersByDate(@PathVariable("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        OrderDateSummaryDTO summaryDTO = orderDetailsService.getOrdersByDate(date);

        if (summaryDTO == null || summaryDTO.getOrderDetailsList().isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        return ResponseEntity.ok(summaryDTO);
    }
}