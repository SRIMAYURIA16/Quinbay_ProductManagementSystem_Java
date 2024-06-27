package com.training.InventoryManagement.Service;

import com.training.InventoryManagement.dto.ProductDTO;
import com.training.InventoryManagement.entity.Category;
import com.training.InventoryManagement.entity.Product;
import com.training.InventoryManagement.entity.SellerDetails;
import com.training.InventoryManagement.repository.CategoryRepository;
import com.training.InventoryManagement.repository.ProductHistoryRepository;
import com.training.InventoryManagement.repository.ProductRepository;
import com.training.InventoryManagement.repository.SellerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SellerRepository sellerRepository;
    @Autowired
    private ProductHistoryRepository productHistoryRepository;
    @Autowired
    private ProductHistoryService productHistoryService;
    @Autowired
    private KafkaTemplate kafkaTemplate;

    @KafkaListener(topics = "order-events", groupId = "my_group")
    public void listen(String message) {
        System.out.println("Received message: " + message);
    }
    public ResponseEntity<String> addProduct(ProductDTO productDTO) {
        int categoryId = productDTO.getCategory().getCategoryId() ;
        int sellerId = productDTO.getSeller().getId() ;
        if (categoryId == 0 || sellerId == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category or Seller ID is null");
        }
        Optional<Category> category=categoryRepository.findById(categoryId);
        if(category.isEmpty()){
        }
        Optional<SellerDetails> seller=sellerRepository.findById(sellerId);
        if(seller.isEmpty()){
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller not found");
        }
        Product product = convertToProduct(productDTO);
//            product.setProductName(productDTO.getProductName());
//            product.setProductPrice(productDTO.getProductPrice());
//            product.setQuantity(productDTO.getQuantity());
//            product.setCategory(category.get());
//            product.setSeller(seller.get());
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully!!!");
    }

    public ResponseEntity<?> findByPrefix(String prefix) {
        List<Product> productList = productRepository.findByProductNameStartingWith(prefix);
        if (productList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Products are not available for this prefix: " + prefix);
        }
        List<ProductDTO> productDTOs = productList.stream()
                .map(ProductDTO::fromProduct)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    public ResponseEntity<?> findByProductNameAndQuantityGT(String prefix, int quantity) {
        List<Product> productList = productRepository.findByProductNameStartingWithAndQuantityGreaterThan(prefix, quantity);
        if (productList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products are available greater than this quantity");
        }
        List<ProductDTO> productDTOs = productList.stream()
                .map(ProductDTO::fromProduct)
                .collect(Collectors.toList());
        return ResponseEntity.ok(productDTOs);
    }

    public ResponseEntity<?> getProductById(Long productId) {
        if (productId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID must not be null");
        }
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            ProductDTO productDTO = ProductDTO.fromProduct(optionalProduct.get());
            return ResponseEntity.ok(productDTO);
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    public ResponseEntity<String> deleteProduct(Long productId) {
        if (productId == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Product ID must not be null");
        }
        if (productRepository.existsById(productId)) {
            productRepository.deleteById(productId);
            return ResponseEntity.status(HttpStatus.OK).body("Product deleted successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    public ResponseEntity<?> getAllProducts() {
        try {
            List<Product> productList = productRepository.findAll();
            if (productList.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No products in the inventory");
            }
            List<ProductDTO> productDTOs = productList.stream()
                    .map(ProductDTO::fromProduct)
                    .collect(Collectors.toList());
            return ResponseEntity.ok(productDTOs);
        }
        catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving products: " + exception.getMessage());
        }
    }

    private Product convertToProduct(ProductDTO productDTO) {
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setProductName(productDTO.getProductName());
        product.setProductPrice(productDTO.getProductPrice());
        product.setQuantity(productDTO.getQuantity());

        if (productDTO.getCategory() != null) {
            Category category = categoryRepository.findById(productDTO.getCategory().getCategoryId())
                    .orElseThrow(() -> new RuntimeException("Category not found"));
            product.setCategory(category);
        }

        if (productDTO.getSeller() != null) {
            SellerDetails seller = sellerRepository.findById(productDTO.getSeller().getId())
                    .orElseThrow(() -> new RuntimeException("Seller not found"));
            product.setSeller(seller);
        }

        return product;
    }


    public ResponseEntity<String> updateProduct(ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findById(productDTO.getProductId());
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();

            existingProduct.setProductName(productDTO.getProductName());
            existingProduct.setProductPrice(productDTO.getProductPrice());
            existingProduct.setQuantity(productDTO.getQuantity());

            if (productDTO.getCategory().getCategoryId() != 0) {
                Optional<Category> categoryOptional = categoryRepository.findById(productDTO.getCategory().getCategoryId());
                if (categoryOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found with ID: " + productDTO.getCategory().getCategoryId());
                }
                existingProduct.setCategory(categoryOptional.get());
            }
            if (productDTO.getSeller().getId() != 0) {
                Optional<SellerDetails> sellerOptional = sellerRepository.findById(productDTO.getSeller().getId());
                if (sellerOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller not found with ID: " + productDTO.getSeller().getId());
                }
                existingProduct.setSeller(sellerOptional.get());
            }

            productRepository.save(existingProduct);
            logProductChanges(existingProduct, productDTO);

            return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }



    private void logProductChanges(Product existingProduct, ProductDTO updatedProductDTO) {
        if (!(existingProduct.getProductName().equals(updatedProductDTO.getProductName()))) {
            productHistoryService.saveProductHistory(existingProduct.getProductId(), "productName", existingProduct.getProductName(), updatedProductDTO.getProductName());
        }
        if (existingProduct.getProductPrice() != updatedProductDTO.getProductPrice()) {
            productHistoryService.saveProductHistory(existingProduct.getProductId(), "productPrice", String.valueOf(existingProduct.getProductPrice()), String.valueOf(updatedProductDTO.getProductPrice()));
        }
        if (existingProduct.getQuantity() != updatedProductDTO.getQuantity()) {
            productHistoryService.saveProductHistory(existingProduct.getProductId(), "quantity", String.valueOf(existingProduct.getQuantity()), String.valueOf(updatedProductDTO.getQuantity()));
        }
    }
}