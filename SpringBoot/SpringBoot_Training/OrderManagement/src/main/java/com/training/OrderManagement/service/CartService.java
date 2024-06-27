package com.training.OrderManagement.service;

import com.training.OrderManagement.entity.Cart;
import com.training.OrderManagement.entity.Product;
import com.training.OrderManagement.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

@Service
@Transactional
public class CartService {

    private static final Logger LOGGER = Logger.getLogger(CartService.class.getName());
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private CartRepository cartRepository;

    public ResponseEntity<String> addProductToCart(String userId, long productId, int stocks) {
        Cart cart = getOrCreateCart(userId);
        Product product = getProductDetails(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
        }
        if (product.getQuantity() < stocks) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Insufficient stock");
        }
        boolean productExists = false;
        for (Product products : cart.getProductList()) {
            if (products.getProductId() == productId) {
                products.setQuantity(products.getQuantity() + stocks);
                productExists = true;
                break;
            }
        }
        if (!productExists) {
            product.setQuantity(stocks);
            cart.getProductList().add(product);
        }
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added to cart successfully!!!");
    }

    public ResponseEntity<?> getCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null || cart.getProductList().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart is empty for the user");
        }
        return ResponseEntity.ok(cart);
    }

    public ResponseEntity<String> removeFromCart(String userId, long productId) {
        Cart cart = getOrCreateCart(userId);
        if(cart.getProductList().size()<=0){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart is empty for the user");
        }
        cart.getProductList().removeIf(product -> product.getProductId() == productId);
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.OK).body("Removed the product from the cart for the user:"+userId);
    }

    public ResponseEntity<String> clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getProductList().clear();
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.OK).body("Cart cleared successfully");
    }

    public ResponseEntity<String> updateProductStocksInCart(String userId, long productId, int newStocks) {
        Cart cart = getOrCreateCart(userId);
        for (Product product : cart.getProductList()) {
            if (product.getProductId() == productId) {
                product.setQuantity(product.getQuantity()+newStocks);
                break;
            }
        }
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.OK).body("Updated!!!");
    }

    private Cart getOrCreateCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null) {
            cart = new Cart();
            cart.setUserId(userId);
            cart.setProductList(new ArrayList<>());
        }
        return cart;
    }

    public Product getProductDetails(long productId) {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        HttpEntity<Void> entity = new HttpEntity<>(headers);
        String url = "http://localhost:8081/product/getById/{productId}";
        try {
            ResponseEntity<Product> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    entity,
                    Product.class,
                    productId);
            return response.getBody();
        }
        catch (Exception exception) {
            LOGGER.log(Level.SEVERE, "Error fetching product details for product ID: " + productId, exception);
            throw new RuntimeException("Failed to fetch product details", exception);
        }
    }
}
