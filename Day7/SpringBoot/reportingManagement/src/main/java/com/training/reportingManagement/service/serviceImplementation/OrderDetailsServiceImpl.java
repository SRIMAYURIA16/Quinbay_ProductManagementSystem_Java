package com.training.reportingManagement.service.serviceImplementation;

import com.training.reportingManagement.dao.entity.Order;
import com.training.reportingManagement.dao.entity.Order_Details;
import com.training.reportingManagement.dao.repository.OrderDetailsRepository;
import com.training.reportingManagement.dto.OrderDateSummaryDTO;
import com.training.reportingManagement.dto.OrderSummaryDTO;
import com.training.reportingManagement.service.serviceInterface.OrderDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class OrderDetailsServiceImpl implements OrderDetailsService {

    @Autowired
    private OrderDetailsRepository orderDetailsRepository;

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public Order_Details addOrder(Order order) {
        Order_Details orderDetails = new Order_Details();
        orderDetails.setEmailId(order.getUserId());
        orderDetails.setTotalAmount(order.getTotalPrice());
        orderDetails.setTotalProducts(order.getProductList().size());
        orderDetails.setCreatedDate(LocalDate.now());
        orderDetailsRepository.save(orderDetails);
        sendConfirmationEmail(order.getUserId());
        return orderDetails;
    }

    @Override
    public OrderSummaryDTO getOrdersByEmailId(String emailId) {
        List<Order_Details> orderDetailsList = orderDetailsRepository.findByEmailId(emailId);
        int totalProducts = 0;
        double totalAmount = 0.0;

        for (Order_Details orderDetails : orderDetailsList) {
            totalProducts += orderDetails.getTotalProducts();
            totalAmount += orderDetails.getTotalAmount();
        }

        OrderSummaryDTO summary = new OrderSummaryDTO();
        summary.setEmailId(emailId);
        summary.setTotalProducts(totalProducts);
        summary.setTotalAmount(totalAmount);

        return summary;
    }

    @Override
    public Order_Details getOrderByOrderId(int id) {
        Optional<Order_Details> orderDetails = orderDetailsRepository.findById(id);
        return orderDetails.orElse(null);
    }

    @Override
    public OrderDateSummaryDTO getOrdersByDate(LocalDate date) {
        List<Order_Details> orderDetailsList = orderDetailsRepository.findByCreatedDate(date);

        int totalProducts = 0;
        double totalIncome = 0.0;

        for (Order_Details orderDetails : orderDetailsList) {
            totalProducts += orderDetails.getTotalProducts();
            totalIncome += orderDetails.getTotalAmount();
        }

        OrderDateSummaryDTO summary = new OrderDateSummaryDTO();
        summary.setDate(date);
        summary.setTotalProducts(totalProducts);
        summary.setTotalIncome(totalIncome);
        summary.setOrderDetailsList(orderDetailsList);

        return summary;
    }

    private void sendConfirmationEmail(String email) {
        SimpleMailMessage msg = new SimpleMailMessage();
        msg.setTo(email);
        msg.setSubject("Order Confirmation");
        msg.setText("Dear Customer,\n\nYour order has been successfully placed.\n\nThank you for shopping with us!");

        try {
            javaMailSender.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
