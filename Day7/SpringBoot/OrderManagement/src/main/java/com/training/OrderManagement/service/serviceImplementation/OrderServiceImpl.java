package com.training.OrderManagement.service.serviceImplementation;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.training.OrderManagement.dao.entity.Order;
import com.training.OrderManagement.dao.entity.Product;
import com.training.OrderManagement.dao.repository.OrderRepository;
import com.training.OrderManagement.dto.CartDTO;
import com.training.OrderManagement.dto.OrderDTO;
import com.training.OrderManagement.dto.ProductDTO;
import com.training.OrderManagement.service.serviceInterface.CartService;
import com.training.OrderManagement.service.serviceInterface.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {
    private static final Logger log= Logger.getLogger(OrderServiceImpl.class.getName());
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CartService cartService;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;
    @Autowired
    private ObjectMapper objectMapper;

    @Override
    public ResponseEntity<String> placeOrder(String userId) {
        ResponseEntity<CartDTO> cartResponse = cartService.getCart(userId);
        if (cartResponse.getBody() == null) {
            log.severe("Cart response body is null for user: " + userId);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Cart is empty for user: " + userId);
        }
        CartDTO cartDTO = cartResponse.getBody();
        log.info("Cart for user " + userId + ": " + cartDTO);
        if (cartDTO.getProductList().isEmpty()) {
            log.warning("Cart is empty for user: " + userId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart is empty for user: " + userId);
        }
        Order order = createOrder(cartDTO, userId);
        orderRepository.save(order);
        updateProductQuantities(cartDTO.getProductList());
        cartService.clearCart(userId);
        try {
            String jsonOrder = objectMapper.writeValueAsString(order);
            kafkaTemplate.send("Orders", jsonOrder);
            log.info("Order sent to Kafka successfully");
        }
        catch (JsonProcessingException e) {
            log.log(Level.SEVERE, "Failed to send order details to Kafka", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to send order details to Kafka");
        }
        return ResponseEntity.ok("Order placed successfully!");
    }

    private Order createOrder(CartDTO cartDTO, String userId) {
        double totalPrice = cartDTO.getProductList().stream()
                .mapToDouble(product -> product.getProductPrice() * product.getQuantity())
                .sum();
        int totalProducts = cartDTO.getProductList().stream()
                .mapToInt(ProductDTO::getQuantity)
                .sum();
        Order order = new Order();
        order.setId(UUID.randomUUID().toString());
        order.setUserId(userId);
        order.setProductList(cartDTO.getProductList().stream()
                .map(this::convertToEntity)
                .collect(Collectors.toList()));
        order.setTotalProducts(totalProducts);
        order.setTotalPrice(totalPrice);
        return order;
    }

    private void updateProductQuantities(List<ProductDTO> products) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        for (ProductDTO product : products) {
            String url = "http://localhost:8081/product/update/{productId}/{quantity}";
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
                log.log(Level.SEVERE, "Failed to update product quantities for product ID: " + product.getProductId(), exception);
                throw new RuntimeException("Failed to update product quantities in Inventory Management.", exception);
            }
        }
    }

    @Override
    public List<OrderDTO> getAllOrders() {
        List<Order> orderList = orderRepository.findAll();
        if (orderList.isEmpty()) {
            throw new RuntimeException("No orders available");
        }
        return orderList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<OrderDTO> getOrdersByUserId(String userId) {
        List<Order> orderList = orderRepository.findByUserId(userId);
        if (orderList.isEmpty()) {
            throw new RuntimeException("No orders available for this user");
        }
        return orderList.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public OrderDTO getOrderById(String orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(() ->
                new RuntimeException("No orders available for this user")
        );
        return convertToDTO(order);
    }

    private OrderDTO convertToDTO(Order order) {
        List<ProductDTO> productDTOList = order.getProductList().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new OrderDTO(order.getId(), order.getUserId(), productDTOList, order.getTotalPrice(), order.getTotalProducts());
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product.getProductId(), product.getProductName(), product.getProductPrice(), product.getQuantity());
    }

    private Product convertToEntity(ProductDTO productDTO) {
        return new Product(productDTO.getProductId(), productDTO.getProductName(), productDTO.getProductPrice(), productDTO.getQuantity());
    }
}