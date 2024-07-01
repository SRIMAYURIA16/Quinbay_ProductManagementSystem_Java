package com.training.OrderManagement.service.serviceImplementation;

import com.training.OrderManagement.dao.entity.Cart;
import com.training.OrderManagement.dao.entity.Product;
import com.training.OrderManagement.dao.repository.CartRepository;
import com.training.OrderManagement.dto.CartDTO;
import com.training.OrderManagement.dto.ProductDTO;
import com.training.OrderManagement.service.serviceInterface.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CartServiceImpl implements CartService {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CartRepository cartRepository;

    @Override
    public ResponseEntity<String> addProductToCart(String userId, long productId, int stocks) {
        Cart cart = getOrCreateCart(userId);
        Product product = convertToEntity(getProductDetails(productId));
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

    @Override
    public ResponseEntity<CartDTO> getCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        if (cart == null || cart.getProductList().isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.ok(convertToDTO(cart));
    }

    @Override
    public ResponseEntity<String> removeFromCart(String userId, long productId) {
        Cart cart = getOrCreateCart(userId);
        if (cart.getProductList().size() <= 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cart is empty for the user");
        }
        cart.getProductList().removeIf(product -> product.getProductId() == productId);
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.OK).body("Removed the product from the cart for the user:" + userId);
    }

    @Override
    public ResponseEntity<String> clearCart(String userId) {
        Cart cart = getOrCreateCart(userId);
        cart.getProductList().clear();
        cartRepository.save(cart);
        return ResponseEntity.status(HttpStatus.OK).body("Cart cleared successfully");
    }

    @Override
    public ResponseEntity<String> updateProductStocksInCart(String userId, long productId, int newStocks) {
        Cart cart = getOrCreateCart(userId);
        boolean found = false;
        for (Product product : cart.getProductList()) {
            if (product.getProductId() == productId) {
                found = true;
                product.setQuantity(product.getQuantity() + newStocks);
                break;
            }
        }
        if (!found) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product is not in the cart!!!");
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

    @Override
    public ProductDTO getProductDetails(long productId) {
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
            return convertToDTO(response.getBody());
        } catch (Exception exception) {
            throw new RuntimeException("Failed to fetch product details", exception);
        }
    }

    private ProductDTO convertToDTO(Product product) {
        return new ProductDTO(product.getProductId(), product.getProductName(), product.getProductPrice(), product.getQuantity());
    }

    private Product convertToEntity(ProductDTO productDTO) {
        return new Product(productDTO.getProductId(), productDTO.getProductName(), productDTO.getProductPrice(), productDTO.getQuantity());
    }

    private CartDTO convertToDTO(Cart cart) {
        List<ProductDTO> productDTOList = cart.getProductList().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
        return new CartDTO(cart.getId(), cart.getUserId(), productDTOList);
    }
}