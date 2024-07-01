package com.training.reportingManagement.service.serviceInterface;

import com.training.reportingManagement.dao.entity.Order;
import com.training.reportingManagement.dao.entity.Order_Details;
import com.training.reportingManagement.dao.entity.Order_Items;
import java.util.List;

public interface OrderItemsService {
    void addOrder(Order order, Order_Details orderDetails);
    List<Order_Items> getOrderItemsByProductId(long productId);
    List<Order_Items> getOrderItemsByProductName(String productName);
}