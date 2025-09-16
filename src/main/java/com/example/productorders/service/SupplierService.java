package com.example.productorders.service;

import com.example.productorders.DTO.SupplierDTO;
import com.example.productorders.model.Supplier;
import com.example.productorders.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SupplierService {

    private final SupplierRepository supplierRepository;

    @Autowired
    public SupplierService(SupplierRepository supplierRepository) {
        this.supplierRepository=supplierRepository;
    }

    public SupplierDTO fromEntityToDTO(Supplier supplier) {
        log.info("fromEntityToDTO in SupplierService");

        if(supplier == null) {
            throw new IllegalArgumentException("Supplier cannot be null");
        }

        SupplierDTO supplierDTO = new SupplierDTO();

        supplierDTO.setId(supplier.getId());
        supplierDTO.setName(supplier.getName());
        supplierDTO.setPhoneNum(supplier.getPhoneNum());
        supplierDTO.setEmail(supplier.getEmail());

        return supplierDTO;

    }

    public Supplier fromDTOTOEntity(SupplierDTO supplierDTO) {
        log.info("fromDTOTOEntity in SupplierService");

        if(supplierDTO == null) {
            throw new IllegalArgumentException("SupplierDTO cannot be null");
        }

        Supplier supplier;
        if(supplierDTO.getId() != null) {
            supplier = supplierRepository.findById(supplierDTO.getId()).orElse(new Supplier());
        }
        else {
            supplier = new Supplier();
        }

        supplier.setName(supplierDTO.getName());
        supplier.setPhoneNum(supplierDTO.getPhoneNum());
        supplier.setEmail(supplierDTO.getEmail());

        saveEntity(supplier);

        return supplier;
    }

    public List<SupplierDTO> fromEntityToDTOList(List<Supplier> suppliers) {
        List<SupplierDTO> supplierDTOS = new ArrayList<>();
        for(Supplier s: suppliers) {
            supplierDTOS.add(fromEntityToDTO(s));
        }
        return  supplierDTOS;
    }

    public Supplier saveEntity(Supplier supplier) {
        log.info("saveEntity in SupplierService");
        return supplierRepository.save(supplier);
    }

    public SupplierDTO getSupplierById(Long id) {
        log.info("getSupplierById in SupplierService");

        if(id<0)
        {
            throw new IllegalArgumentException("Supplier Id cannot be negative");
        }
        Supplier supplier = supplierRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Cannot find supplier with id"+ id));
        return fromEntityToDTO(supplier);
    }

    public SupplierDTO saveSupplier(SupplierDTO supplierDTO) {

        if(supplierDTO == null) {
            throw new EntityNotFoundException("Supplier DTO cannot be null");
        }

        log.info("saveSupplier in SupplierService");
        Supplier supplier = new Supplier();

        supplier.setName(supplierDTO.getName());
        supplier.setPhoneNum(supplierDTO.getPhoneNum());
        supplier.setEmail(supplierDTO.getEmail());

        saveEntity(supplier);

        return fromEntityToDTO(supplier);
    }

    public SupplierDTO updateSupplier(SupplierDTO supplierDTO, Long id) {
        log.info("updateSupplier in SupplierService");
        Supplier supplier = supplierRepository.findById(id).orElseThrow(()-> new EntityNotFoundException("Cannot find supplier with id " + id));

        supplier.setName(supplierDTO.getName());
        supplier.setPhoneNum(supplierDTO.getPhoneNum());
        supplier.setEmail(supplierDTO.getEmail());

        saveEntity(supplier);

        return fromEntityToDTO(supplier);
    }

    public void deleteSupplier(Long id) {
        log.info("deleteSupplier in SupplierService");

        if(!supplierRepository.existsById(id)) {
            throw new EntityNotFoundException("Id does not exist");
        }

        supplierRepository.deleteById(id);
    }

    public List<SupplierDTO> getSuppliers() {
        List<Supplier> suppliers= supplierRepository.findAll();
        return fromEntityToDTOList(suppliers);
    }
}
