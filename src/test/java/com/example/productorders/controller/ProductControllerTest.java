package com.example.productorders.controller;

import com.example.productorders.DTO.ProductDTO;
import com.example.productorders.DTO.ProductSupplierDTO;
import com.example.productorders.DTO.SupplierDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ProductControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;
    private String supplierUrl;

    ProductDTO productDTO;
    SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:" + port + "/products";
        supplierUrl = "http://localhost:" + port + "/suppliers";
        productDTO = new ProductDTO("Test Product", 150.0, 50.0, 1L);
        supplierDTO = new SupplierDTO("Test Supplier", "908-897-0980", "supplier@email.com");

    }

    @Test
    void testGetProducts() {
        ResponseEntity<ProductDTO[]> response = restTemplate.getForEntity(baseUrl, ProductDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testSaveProduct() {

        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.postForEntity(supplierUrl, supplierDTO, SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());

        ResponseEntity<ProductDTO> response = restTemplate.postForEntity(baseUrl, productDTO, ProductDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testGetProductById() {
        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.postForEntity(supplierUrl, supplierDTO, SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());

        ResponseEntity<ProductDTO> response = restTemplate.postForEntity(baseUrl, productDTO, ProductDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Long productId = response.getBody().getId();

        ResponseEntity<ProductDTO> responseProduct = restTemplate.getForEntity(baseUrl + "/"+productId, ProductDTO.class);

        assertEquals(HttpStatus.OK, responseProduct.getStatusCode());
        ProductDTO product = responseProduct.getBody();

        assertNotNull(product);
        assertEquals(productId, product.getId());
    }

    @Test
    void testGetProductSupplier() {
        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.postForEntity(supplierUrl, supplierDTO, SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());

        ResponseEntity<ProductDTO> response = restTemplate.postForEntity(baseUrl, productDTO, ProductDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Long productId = response.getBody().getId();

        ResponseEntity<ProductSupplierDTO> responseProductSupplier = restTemplate.getForEntity(baseUrl + "/suppliers/"+productId, ProductSupplierDTO.class);

        assertEquals(HttpStatus.OK, responseProductSupplier.getStatusCode());
        ProductSupplierDTO productSupplierDTO = responseProductSupplier.getBody();

        assertNotNull(productSupplierDTO);
    }

    @Test
    void testEditProduct() {

        ProductDTO product1 = new ProductDTO("Test Product", 156.0, 50.0, 1L);

        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.postForEntity(supplierUrl, supplierDTO, SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());

        ResponseEntity<ProductDTO> response = restTemplate.postForEntity(baseUrl, productDTO, ProductDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Long productId = response.getBody().getId();

        restTemplate.put(baseUrl + "/"+productId, product1);

        ResponseEntity<ProductDTO> responseProduct = restTemplate.getForEntity(baseUrl+"/"+productId, ProductDTO.class);
        assertEquals(HttpStatus.OK, responseProduct.getStatusCode());

        ProductDTO productDTO1 = responseProduct.getBody();

        assertEquals(product1.getPrice(), productDTO1.getPrice());


    }

    @Test
    void testDeleteProduct() {

        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.postForEntity(supplierUrl, supplierDTO, SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());

        ResponseEntity<ProductDTO> response = restTemplate.postForEntity(baseUrl, productDTO, ProductDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        Long productId = response.getBody().getId();

        restTemplate.delete(baseUrl + "/" + productId);

        ResponseEntity<ProductDTO> responseDelete = restTemplate.getForEntity(baseUrl + "/" + productId, ProductDTO.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseDelete.getStatusCode());
    }

    }