package com.training.reportingManagement.service.serviceInterface;

import com.training.reportingManagement.dao.entity.Order;

public interface OrderConsumerService {
    void consumerOrder(Order order);
}