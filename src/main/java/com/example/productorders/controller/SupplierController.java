package com.example.productorders.controller;

import com.example.productorders.DTO.SupplierDTO;
import com.example.productorders.model.Supplier;
import com.example.productorders.service.SupplierService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/suppliers")
@CrossOrigin(origins = "*")
@Slf4j
public class SupplierController {

    private final SupplierService supplierService;

    @Autowired
    public SupplierController(SupplierService supplierService) {
        this.supplierService = supplierService;
    }

    @GetMapping("/{id}")
    public SupplierDTO getSupplierById(@PathVariable Long id) {
        log.info("getSupplierbyId in SupplierController");
        return supplierService.getSupplierById(id);
    }

    @PostMapping
    public SupplierDTO saveSupplier(@RequestBody SupplierDTO supplierDTO) {
        log.info("saveSupplier in SupplierController");
        return supplierService.saveSupplier(supplierDTO);
    }

    @PutMapping("/{id}")
    public SupplierDTO updateSupplier(@RequestBody SupplierDTO supplierDTO, @PathVariable Long id) {
        log.info("updateSupplier in SupplierController");
        return supplierService.updateSupplier(supplierDTO, id);
    }

    @DeleteMapping("/{id}")
    public void deleteSupplier(@PathVariable Long id) {
        log.info("deleteSupplier in SupplierController");
        supplierService.deleteSupplier(id);
    }
}
