package com.example.productorders.DTO;

import com.example.productorders.model.Product;
import com.example.productorders.model.Supplier;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProductSupplierDTO {

    private String product_name;
    private Double product_price;
    private Double product_stock;
    private String supplier_name;
    private String supplier_phoneNum;
    private String supplier_email;
}
