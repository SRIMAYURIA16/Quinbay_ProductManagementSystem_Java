package com.training.reportingManagement.service.serviceImplementation;

import com.training.reportingManagement.dao.entity.Order;
import com.training.reportingManagement.dao.entity.Order_Details;
import com.training.reportingManagement.dao.entity.Order_Items;
import com.training.reportingManagement.dao.entity.Product;
import com.training.reportingManagement.dao.repository.OrderItemsRepository;
import com.training.reportingManagement.service.serviceInterface.OrderItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderItemsServiceImpl implements OrderItemsService {

    @Autowired
    private OrderItemsRepository orderItemsRepository;

    @Override
    public void addOrder(Order order, Order_Details orderDetails) {
        List<Product> productList = order.getProductList();
        for (Product product : productList) {
            Order_Items orderItems = new Order_Items();
            orderItems.setQuantity(product.getQuantity());
            orderItems.setProductId(product.getProductId());
            orderItems.setProductName(product.getProductName());
            orderItems.setProductPrice(product.getProductPrice());
            orderItems.setOrderDetails(orderDetails); // Set the orderDetails
            orderItemsRepository.save(orderItems);
        }
    }

    @Override
    public List<Order_Items> getOrderItemsByProductId(long productId) {
        return orderItemsRepository.findByProductId(productId);
    }

    @Override
    public List<Order_Items> getOrderItemsByProductName(String productName) {
        return orderItemsRepository.findByProductName(productName);
    }
}
