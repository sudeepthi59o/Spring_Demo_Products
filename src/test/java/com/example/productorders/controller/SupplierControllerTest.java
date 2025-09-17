package com.example.productorders.controller;

import com.example.productorders.DTO.SupplierDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class SupplierControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    private String baseUrl;

    private SupplierDTO supplierDTO;

    @BeforeEach
    void setUp() {
        baseUrl = "http://localhost:"+port+"/suppliers";
        supplierDTO = new SupplierDTO("Test Supplier","987-056-0908","supplier@email.com");
    }

    @Test
    void getSuppliers() {

        ResponseEntity<SupplierDTO[]> response = restTemplate.getForEntity(baseUrl,SupplierDTO[].class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void getSupplierById() {

        ResponseEntity<SupplierDTO> response = restTemplate.postForEntity(baseUrl,supplierDTO,SupplierDTO.class);
        assertEquals(HttpStatus.OK,response.getStatusCode());

        Long supplierId = response.getBody().getId();

        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.getForEntity(baseUrl+"/"+supplierId,SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());

        SupplierDTO s = responseSupplier.getBody();

        assertNotNull(s);
        assertEquals(supplierId, s.getId());

    }

    @Test
    void saveSupplier() {
        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.postForEntity(baseUrl, supplierDTO, SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());
    }

    @Test
    void updateSupplier() {

        SupplierDTO supplierDTO1 = new SupplierDTO("Test Supplier 2","786-098-7657", "supplier@email.com");

        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.postForEntity(baseUrl, supplierDTO, SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());

        Long productId = responseSupplier.getBody().getId();

        restTemplate.put(baseUrl + "/"+productId, supplierDTO1);

        ResponseEntity<SupplierDTO> response = restTemplate.getForEntity(baseUrl+"/"+productId, SupplierDTO.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        SupplierDTO supplierDTO2 = response.getBody();

        assertEquals(supplierDTO1.getName(), supplierDTO2.getName());
    }

    @Test
    void deleteSupplier() {

        ResponseEntity<SupplierDTO> responseSupplier = restTemplate.postForEntity(baseUrl, supplierDTO, SupplierDTO.class);
        assertEquals(HttpStatus.OK, responseSupplier.getStatusCode());

        Long productId = responseSupplier.getBody().getId();

        restTemplate.delete(baseUrl + "/" + productId);

        ResponseEntity<SupplierDTO> response = restTemplate.getForEntity(baseUrl + "/" + productId, SupplierDTO.class);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
    }
}