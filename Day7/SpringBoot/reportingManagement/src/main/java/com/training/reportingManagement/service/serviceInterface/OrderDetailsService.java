package com.training.reportingManagement.service.serviceInterface;

import com.training.reportingManagement.dao.entity.Order;
import com.training.reportingManagement.dao.entity.Order_Details;
import com.training.reportingManagement.dto.OrderDateSummaryDTO;
import com.training.reportingManagement.dto.OrderSummaryDTO;
import java.time.LocalDate;

public interface OrderDetailsService {
    Order_Details addOrder(Order order);
    OrderSummaryDTO getOrdersByEmailId(String emailId);
    Order_Details getOrderByOrderId(int id);
    OrderDateSummaryDTO getOrdersByDate(LocalDate date);
}