package com.example.productorders.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private String name;
    private Double price;
    private Double stock;

    @ManyToOne
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

}
