package com.example.productorders.service;

import com.example.productorders.DTO.ProductDTO;
import com.example.productorders.model.Product;
import com.example.productorders.model.Supplier;
import com.example.productorders.repository.ProductRepository;
import com.example.productorders.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;
    private final SupplierRepository supplierRepository;

    @Autowired
    public ProductService(ProductRepository productRepository, SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.supplierRepository=supplierRepository;
    }

    public ProductDTO fromEntityToDTO(Product product) {
        log.info("fromEntityToDTO in ProductService");
        ProductDTO productDTO = new ProductDTO();

        productDTO.setId(product.getId());
        productDTO.setName(product.getName());
        productDTO.setPrice(product.getPrice());
        productDTO.setStock(product.getStock());
        productDTO.setSupplierId(product.getSupplier().getId());

        return productDTO;

    }

    public Product fromDTOToEntity(ProductDTO productDTO) {
        log.info("fromDTOtoEntity in ProductService");
        Product product;
        if(productDTO.getId() != null) {
            product = productRepository.findById(productDTO.getId()).orElse(new Product());
        }
        else {
            product = new Product();
        }

        Supplier s = supplierRepository.findById(productDTO.getSupplierId()).orElseThrow(()-> new EntityNotFoundException(
                "Supplier not found with id " + productDTO.getSupplierId()
        ));

        product.setId(productDTO.getId());
        product.setName(productDTO.getName());
        product.setPrice(productDTO.getPrice());
        product.setStock(productDTO.getStock());
        product.setSupplier(s);

        saveEntity(product);

        return product;

    }

    public Product saveEntity(Product product) {

        log.info("saveEntity in ProductService");

        return productRepository.save(product);
    }

    public ProductDTO getProductById(Long id) {

        log.info("getProductById in ProductService");
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Cannot find product with id " + id));
        return fromEntityToDTO(product);
    }

    public ProductDTO saveProduct(ProductDTO productDTO) {

        log.info("saveProducty in ProductService");

        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId()).orElseThrow(()-> new EntityNotFoundException("Cannot find supplier with id " + productDTO.getSupplierId()));

        Product product = new Product();

        product.setName(productDTO.getName());
        product.setStock(productDTO.getStock());
        product.setPrice(productDTO.getPrice());
        product.setSupplier(supplier);

        saveEntity(product);

        return fromEntityToDTO(product);
    }

    public void deleteProduct(Long id) {
        log.info("deleteProduct in ProductService");
        productRepository.deleteById(id);
    }

    public ProductDTO updateProduct(ProductDTO productDTO, Long id) {
        log.info("updateProduct in ProductService");
        Product product = productRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Cannot find product with id " + id));
        Supplier supplier = supplierRepository.findById(productDTO.getSupplierId()).orElseThrow(()-> new EntityNotFoundException("Cannot find supplier with id " + productDTO.getSupplierId()));

        product.setName(productDTO.getName());
        product.setStock(productDTO.getStock());
        product.setPrice(productDTO.getPrice());
        product.setSupplier(supplier);

        saveEntity(product);
        return fromEntityToDTO(product);
    }
}
