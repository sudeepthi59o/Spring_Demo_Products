package com.example.productorders.controller;

import com.example.productorders.DTO.ProductDTO;
import com.example.productorders.DTO.ProductSupplierDTO;
import com.example.productorders.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
@CrossOrigin(origins = "*")
@Slf4j
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDTO> getProducts() {
        log.info("getProducts in ProductController");
        return productService.getProducts();
    }

    @GetMapping("/{id}")
    public ProductDTO getProductById(@PathVariable Long id) {
        log.info("getProductById in ProductController");
        return productService.getProductById(id);
    }

    @GetMapping("/suppliers/{id}")
    public ProductSupplierDTO getProductSupplier(@PathVariable Long id) {
        log.info("getProductSupplier in ProductController");
        return productService.getProductSupplier(id);
    }

    @PostMapping
    public ProductDTO saveProduct(@RequestBody ProductDTO productDTO) {
        log.info("saveProduct in ProductController");
        return productService.saveProduct(productDTO);
    }

    @PutMapping("/{id}")
    public ProductDTO editProduct(@RequestBody ProductDTO productDTO, @PathVariable Long id)
    {
        log.info("editProduct in ProductController");
        return productService.updateProduct(productDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id)
    {
        log.info("deleteProduct in ProductController");
        productService.deleteProduct(id);
    }
}


