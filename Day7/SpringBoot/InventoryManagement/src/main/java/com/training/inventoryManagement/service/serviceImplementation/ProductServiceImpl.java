package com.training.inventoryManagement.service.serviceImplementation;

import com.training.inventoryManagement.dto.ProductDTO;
import com.training.inventoryManagement.dao.entity.Category;
import com.training.inventoryManagement.dao.entity.Product;
import com.training.inventoryManagement.dao.entity.SellerDetails;
import com.training.inventoryManagement.dao.repository.CategoryRepository;
import com.training.inventoryManagement.dao.repository.ProductHistoryRepository;
import com.training.inventoryManagement.dao.repository.ProductRepository;
import com.training.inventoryManagement.dao.repository.SellerRepository;
import com.training.inventoryManagement.service.serviceInterface.ProductHistoryService;
import com.training.inventoryManagement.service.serviceInterface.ProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private static final Logger log = LoggerFactory.getLogger(ProductServiceImpl.class);

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
    private KafkaTemplate<String, String> kafkaTemplate;

    @CachePut(value = "products", key = "#productDTO.productId")
    @Override
    public ResponseEntity<String> addProduct(ProductDTO productDTO) {
        int categoryId = productDTO.getCategoryId();
        int sellerId = productDTO.getSellerId();
        if (categoryId == 0 || sellerId == 0) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category or Seller ID is null");
        }
        Optional<Category> category = categoryRepository.findById(categoryId);
        if (category.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found");
        }
        Optional<SellerDetails> seller = sellerRepository.findById(sellerId);
        if (seller.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller not found");
        }
        Product product = convertToProduct(productDTO, category.get(), seller.get());
        productRepository.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body("Product added successfully!!!");
    }

    @Override
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

    @Override
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

    @Cacheable(value = "products", key = "#productId")
    @Override
    public ProductDTO getProductById(Long productId) {
        Optional<Product> optionalProduct = productRepository.findById(productId);
        if (optionalProduct.isPresent()) {
            log.info("Fetching product from database for id: {}", productId);
            return ProductDTO.fromProduct(optionalProduct.get());
        }
        return null;
    }

    @CacheEvict(value = "products", key = "#productId")
    @Override
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

    @Override
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
        } catch (Exception exception) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error retrieving products: " + exception.getMessage());
        }
    }

    @Override
    public ResponseEntity<String> updateProduct(ProductDTO productDTO) {
        Optional<Product> optionalProduct = productRepository.findById(productDTO.getProductId());
        if (optionalProduct.isPresent()) {
            Product existingProduct = optionalProduct.get();
            logProductChanges(existingProduct,productDTO);
            existingProduct.setProductName(productDTO.getProductName());
            existingProduct.setProductPrice(productDTO.getProductPrice());
            existingProduct.setQuantity(productDTO.getQuantity());
            if (productDTO.getCategoryId() != 0) {
                Optional<Category> categoryOptional = categoryRepository.findById(productDTO.getCategoryId());
                if (categoryOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Category not found with ID: " + productDTO.getCategoryId());
                }
                existingProduct.setCategory(categoryOptional.get());
            }
            if (productDTO.getSellerId() != 0) {
                Optional<SellerDetails> sellerOptional = sellerRepository.findById(productDTO.getSellerId());
                if (sellerOptional.isEmpty()) {
                    return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Seller not found with ID: " + productDTO.getSellerId());
                }
                existingProduct.setSeller(sellerOptional.get());
            }
            productRepository.save(existingProduct);
            return ResponseEntity.status(HttpStatus.OK).body("Product updated successfully");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    @Override
    public ResponseEntity<String> updateStock(long productId, int stocks) {
        Optional<Product> product = productRepository.findById(productId);
        if (product.isPresent()) {
            Product existingProduct = product.get();
            int updatedQuantity = existingProduct.getQuantity() - stocks;
            existingProduct.setQuantity(updatedQuantity);
            ProductDTO updatedProductDTO = ProductDTO.fromProduct(existingProduct);
            logProductChanges(existingProduct, updatedProductDTO);
            productRepository.save(existingProduct);
            return ResponseEntity.status(HttpStatus.OK).body("Stocks updated in the inventory");
        }
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found");
    }

    private Product convertToProduct(ProductDTO productDTO, Category category, SellerDetails seller) {
        Product product = new Product();
        product.setProductId(productDTO.getProductId());
        product.setProductName(productDTO.getProductName());
        product.setProductPrice(productDTO.getProductPrice());
        product.setQuantity(productDTO.getQuantity());
        product.setCategory(category);
        product.setSeller(seller);
        return product;
    }

    private void logProductChanges(Product existingProduct, ProductDTO updatedProductDTO) {
        if (!existingProduct.getProductName().equals(updatedProductDTO.getProductName())) {
            productHistoryService.saveProductHistory(existingProduct.getProductId(), "productName", existingProduct.getProductName(), updatedProductDTO.getProductName());
        }
        if (existingProduct.getProductPrice() != updatedProductDTO.getProductPrice()) {
            productHistoryService.saveProductHistory(existingProduct.getProductId(), "productPrice", String.valueOf(existingProduct.getProductPrice()), String.valueOf(updatedProductDTO.getProductPrice()));
        }
        if (existingProduct.getQuantity() != updatedProductDTO.getQuantity()) {
            productHistoryService.saveProductHistory(existingProduct.getProductId(), "quantity", String.valueOf(existingProduct.getQuantity()), String.valueOf(updatedProductDTO.getQuantity()));
        }
        if (existingProduct.getCategory().getCategoryId() != updatedProductDTO.getCategoryId()) {
            productHistoryService.saveProductHistory(existingProduct.getProductId(), "categoryId", String.valueOf(existingProduct.getCategory().getCategoryId()), String.valueOf(updatedProductDTO.getCategoryId()));
        }
        if (existingProduct.getSeller().getId() != updatedProductDTO.getSellerId()) {
            productHistoryService.saveProductHistory(existingProduct.getProductId(), "sellerId", String.valueOf(existingProduct.getSeller().getId()), String.valueOf(updatedProductDTO.getSellerId()));
        }
    }
}