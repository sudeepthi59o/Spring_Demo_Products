package com.example.productorders.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductDTO {
    private Long id;
    private String name;
    private Double price;
    private Double stock;
    private Long supplierId;

    public ProductDTO(String name, Double price, Double stock, Long supplierId) {
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.supplierId = supplierId;
    }
}
