package com.example.productorders.service;

import com.example.productorders.DTO.ProductDTO;
import com.example.productorders.DTO.ProductSupplierDTO;
import com.example.productorders.model.Product;
import com.example.productorders.model.Supplier;
import com.example.productorders.repository.ProductRepository;
import com.example.productorders.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private ProductService productService;

    private Product product;
    private Supplier supplier;

    @BeforeEach
    void setUp() {
        // Initialize mocks and the service
        MockitoAnnotations.openMocks(this);

        // Create a sample product and supplier for testing
        supplier = new Supplier(1L,"Test Supplier","993-446-5678", "supplier@rmail.com");
        product = new Product(1L,"Test Product", 100.0, 50.0, supplier);
    }

    @Test
    void deleteProduct_ProductExists_ShouldDeleteProduct() {

        ProductDTO productDTO = new ProductDTO(product.getId(),product.getName(),product.getPrice(),product.getStock(),product.getSupplier().getId());

        when(productRepository.existsById(productDTO.getId())).thenReturn(true);
        doNothing().when(productRepository).deleteById(productDTO.getId());

        productService.deleteProduct(productDTO.getId());

        verify(productRepository, times(1)).deleteById(productDTO.getId());
    }

    @Test
    void fromEntityToDTO_ShouldReturnCorrectProductDTO() {

        ProductDTO productDTO = productService.fromEntityToDTO(product);

        assertEquals(product.getId(), productDTO.getId());
        assertEquals(product.getName(), productDTO.getName());
        assertEquals(product.getPrice(), productDTO.getPrice());
        assertEquals(product.getStock(), productDTO.getStock());
        assertEquals(product.getSupplier().getId(), productDTO.getSupplierId());
    }

    @Test
    void fromEntityToDTO_ShouldThrowExceptionIfProductIsNull() {
        Product product2 = null;

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            productService.fromEntityToDTO(product2);
        });
    }

    @Test
    void fromDTOToEntity_ShouldReturnProductEntity() {
        // Arrange
        ProductDTO productDTO = new ProductDTO(1L, "Test Product", 100.0, 50.0, 1L);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));

        Product product = productService.fromDTOToEntity(productDTO);

        assertEquals(productDTO.getId(), product.getId());
        assertEquals(productDTO.getName(), product.getName());
        assertEquals(productDTO.getPrice(), product.getPrice());
        assertEquals(productDTO.getStock(), product.getStock());
        assertEquals(supplier, product.getSupplier());
    }

    @Test
    void fromDTOToEntity_ShouldHandleNullDTO() {
        ProductDTO productDTO = null;

        assertThrows(IllegalArgumentException.class, () -> productService.fromDTOToEntity(productDTO));
    }

    @Test
    void fromDTOToEntity_ShouldThrowExceptionIfSupplierNotFound() {
        ProductDTO productDTO = new ProductDTO(1L, "Test Product", 100.0, 50.0, 999L);  // Invalid supplier ID
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.fromDTOToEntity(productDTO));
    }


    @Test
    void saveProduct_ShouldReturnSavedProductDTO() {
        ProductDTO productDTO = new ProductDTO("Test Product", 100.0, 50.0, 1L);

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(productRepository.save(any(Product.class))).thenReturn(product);

        ProductDTO result = productService.saveProduct(productDTO);

        assertNotNull(result);
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getStock(), result.getStock());
        assertEquals(product.getSupplier().getId(), result.getSupplierId());
    }

    @Test
    void saveProduct_ShouldHandleNullDTO() {
        ProductDTO productDTO = null;

        assertThrows(IllegalArgumentException.class, () -> productService.saveProduct(productDTO));
    }

    @Test
    void saveProduct_ShouldThrowExceptionIfSupplierNotFound() {
        ProductDTO productDTO = new ProductDTO("Test Product", 100.0, 50.0, 999L);
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.saveProduct(productDTO));
    }


    @Test
    void getProductById_ShouldReturnProductDTO() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        ProductDTO result = productService.getProductById(1L);

        assertNotNull(result);
        assertEquals(product.getId(), result.getId());
        assertEquals(product.getName(), result.getName());
        assertEquals(product.getPrice(), result.getPrice());
        assertEquals(product.getStock(), result.getStock());
    }

    @Test
    void getProductById_ShouldThrowExceptionIfProductNotFound() {
        Long productId = 999L;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.getProductById(productId));
    }

    @Test
    void getProductById_ShouldThrowExceptionForInvalidId() {
        Long invalidId = -1L;

        assertThrows(IllegalArgumentException.class, () -> productService.getProductById(invalidId));
    }


    @Test
    void deleteProduct_ShouldThrowExceptionIfProductNotFound() {
        Long productId = 999L;  // Non-existent product ID
        when(productRepository.existsById(productId)).thenReturn(false);

        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(productId));
    }

    @Test
    void deleteProduct_ShouldThrowExceptionForInvalidId() {
        Long invalidId = -1L;  // Invalid ID

        assertThrows(EntityNotFoundException.class, () -> productService.deleteProduct(invalidId));
    }



    @Test
    void updateProduct_ShouldReturnUpdatedProductDTO() {
        ProductDTO productDTO = new ProductDTO(1L, "Updated Product", 120.0, 60.0, 1L);
        Product updatedProduct = new Product(1L, "Updated Product", 120.0, 60.0, supplier);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));
        when(productRepository.save(any(Product.class))).thenReturn(updatedProduct);

        ProductDTO result = productService.updateProduct(productDTO, 1L);

        assertNotNull(result);
        assertEquals(updatedProduct.getId(), result.getId());
        assertEquals(updatedProduct.getName(), result.getName());
        assertEquals(updatedProduct.getPrice(), result.getPrice());
        assertEquals(updatedProduct.getStock(), result.getStock());
    }

    @Test
    void updateProduct_ShouldThrowExceptionIfSupplierNotFound() {
        ProductDTO productDTO = new ProductDTO(1L, "Updated Product", 120.0, 60.0, 999L);  // Non-existent supplier ID
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product()));
        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> productService.updateProduct(productDTO, 1L));
    }


    @Test
    void getProducts_ShouldReturnProductDTOList() {
        Supplier supplier = new Supplier(1L, "Test Supplier", "993-446-5678", "supplier@rmail.com");
        Product product1 = new Product(1L, "Product 1", 100.0, 50.0, supplier);
        Product product2 = new Product(2L, "Product 2", 150.0, 60.0, supplier);
        List<Product> productList = List.of(product1, product2);

        when(productRepository.findAll()).thenReturn(productList);

        List<ProductDTO> result = productService.getProducts();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(product1.getName(), result.get(0).getName());
        assertEquals(product2.getName(), result.get(1).getName());
    }

    @Test
    void getProducts_ShouldReturnEmptyListWhenNoProducts() {
        when(productRepository.findAll()).thenReturn(new ArrayList<>());  // Empty product list

        List<ProductDTO> result = productService.getProducts();

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    void getProducts_ShouldReturnCorrectNumberOfProducts() {
        List<Product> products = new ArrayList<>();
        for (long i = 1; i <= 1000; i++) {
            products.add(new Product(i, "Product " + i, 100.0, 50.0, new Supplier(i, "Supplier " + i, "123-456-7890", "supplier" + i + "@mail.com")));
        }
        when(productRepository.findAll()).thenReturn(products);

        List<ProductDTO> result = productService.getProducts();

        assertNotNull(result);
        assertEquals(1000, result.size());
    }


    @Test
    void getProductSupplier_ShouldReturnProductSupplierDTO() {
        ProductSupplierDTO expectedDTO = new ProductSupplierDTO(product.getName(), product.getPrice(), product.getStock(), supplier.getName(), supplier.getPhoneNum(), supplier.getEmail());

        when(productRepository.existsById(1L)).thenReturn(true);
        when(productRepository.findProductWithSupplier(1L)).thenReturn(product);

        ProductSupplierDTO result = productService.getProductSupplier(1L);

        assertNotNull(result);
        assertEquals(expectedDTO.getProduct_name(), result.getProduct_name());
        assertEquals(expectedDTO.getSupplier_name(), result.getSupplier_name());
    }

    @Test
    void getProductSupplier_ShouldThrowExceptionIfProductNotFound() {
        Long productId = 999L;  // Non-existent product ID
        when(productRepository.findProductWithSupplier(productId)).thenReturn(null);

        assertThrows(EntityNotFoundException.class, () -> productService.getProductSupplier(productId));
    }

    @Test
    void getProductSupplier_ShouldThrowExceptionIfProductHasNoSupplier() {
        when(productRepository.findProductWithSupplier(1L)).thenReturn(product);

        assertThrows(EntityNotFoundException.class, () -> productService.getProductSupplier(1L));
    }


}