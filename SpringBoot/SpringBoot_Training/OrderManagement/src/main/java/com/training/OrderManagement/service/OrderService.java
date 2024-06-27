package com.training.OrderManagement.service;

import com.training.OrderManagement.entity.Cart;
import com.training.OrderManagement.entity.Order;
import com.training.OrderManagement.entity.Product;
import com.training.OrderManagement.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class OrderService {

    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());

    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    private void sendOrderPlacedMessage() {
        String message = "Get all orderssuccessfully: " ;
        kafkaTemplate.send("order-events", message);
        LOGGER.info("Sent Kafka message: " + message);
    }

    public ResponseEntity<?> placeOrder(String userId) {
        ResponseEntity<?> cartResponse= cartService.getCart(userId);
        Object body = cartResponse.getBody();
        if (!(body instanceof Cart)) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Unexpected response body type");
        }
        Cart cart = (Cart) body;
        if (cart.getProductList().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart is empty for user: " + userId);
        }
        Order order = createOrder(cart, userId);
        orderRepository.save(order);
        updateProductQuantities(cart.getProductList());

        cartService.clearCart(userId);

        return ResponseEntity.ok("Order placed successfully!");
    }


    private Order createOrder(Cart cart, String userId) {
        double totalPrice = 0.0;
        int totalProducts = 0;
        for (Product product : cart.getProductList()) {
            totalPrice += product.getProductPrice() * product.getQuantity();
            totalProducts += product.getQuantity();
        }
        Order order = new Order();
        order.setId(String.valueOf((int)(Math.random() * 1000)));
        order.setUserId(userId);
        order.setProductList(cart.getProductList());
        order.setTotalProducts(totalProducts);
        order.setTotalPrice(totalPrice);
        return order;
    }

    private void updateProductQuantities(List<Product> products) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        for (Product product : products) {
            String url = "http://localhost:8081/product/updateStock/{productId}/{quantity}";
            try {
                ResponseEntity<Void> responseEntity = restTemplate.exchange(
                        url,
                        HttpMethod.PUT,
                        entity,
                        Void.class,
                        product.getProductId(),
                        product.getQuantity());
                if (responseEntity.getStatusCode() != HttpStatus.OK) {
                    throw new RuntimeException("Failed to update product quantities in Inventory Management.");
                }
            }
            catch (Exception exception) {
                LOGGER.log(Level.SEVERE, "Failed to update product quantities for product ID: " + product.getProductId(),exception);
                throw new RuntimeException("Failed to update product quantities in Inventory Management.", exception);
            }
        }
    }

    public List<Order> getAllOrders() {
        List<Order> orderList=orderRepository.findAll();

        if(orderList.size()<=0){
            throw new RuntimeException("No orders available");
        }
        return orderList;
    }

    public List<Order> getOrdersByUserId(String userId) {
        List<Order> orderList=orderRepository.findByUserId(userId);
        if(orderList.size()<=0){
            throw new RuntimeException("No orders available for this user");
        }
        return orderList;
    }

    public Order getOrderById(String orderId) {
        Order order=orderRepository.findById(orderId).orElse(null);
        if(order==null){
            throw new RuntimeException("No orders available for this user");
        }
        return order;
    }


    @Cacheable(value="springBootTraining", key="#key")
    public String redisCache(String key,String value){
        return value;
    }
}