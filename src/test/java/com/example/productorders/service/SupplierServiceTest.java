package com.example.productorders.service;

import com.example.productorders.DTO.SupplierDTO;
import com.example.productorders.model.Product;
import com.example.productorders.model.Supplier;
import com.example.productorders.repository.SupplierRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SupplierServiceTest {

    @Mock
    private SupplierRepository supplierRepository;

    @InjectMocks
    private SupplierService supplierService;

    Product product;
    Supplier supplier;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        supplier = new Supplier(1L, "Test Supplier", "888-900-989", "testemail@email.com");
    }

    @Test
    void fromEntityToDTO_ShouldReturnCorrectSupplierDTO() {

        SupplierDTO supplierDTO = supplierService.fromEntityToDTO(supplier);

        assertEquals(supplierDTO.getId(), supplier.getId());
        assertEquals(supplierDTO.getName(), supplier.getName());
        assertEquals(supplierDTO.getPhoneNum(), supplier.getPhoneNum());
        assertEquals(supplierDTO.getEmail(), supplier.getEmail());

    }

    @Test
    void fromEntityToDTO_ShouldThrowExceptionIfSupplierIsNull() {

        Supplier supplier1 = null;

         assertThrows(IllegalArgumentException.class, () -> {
            supplierService.fromEntityToDTO(supplier1);
        });

    }


    @Test
    void fromDTOTOEntity_ShouldReturnSupplierEntity() {

        SupplierDTO supplierDTO = new SupplierDTO(1L, "Test Supplier", "888-900-989", "testemail@email.com");

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        Supplier supplier1 = supplierService.fromDTOTOEntity(supplierDTO);

        assertEquals(supplierDTO.getName(), supplier1.getName());
        assertEquals(supplierDTO.getPhoneNum(), supplier1.getPhoneNum());
        assertEquals(supplierDTO.getEmail(), supplier1.getEmail());
    }

    @Test
    void fromDTOTOEntity__ShouldHandleNullDTO() {

        SupplierDTO supplierDTO = null;

        assertThrows(IllegalArgumentException.class, () -> {
            supplierService.fromDTOTOEntity(supplierDTO);
        });

    }

    @Test
    void getSupplierById_ShouldReturnSupplierDTO() {

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        SupplierDTO supplierDTO = supplierService.getSupplierById(1L);

        assertEquals(supplier.getId(), supplierDTO.getId());
        assertEquals(supplier.getName(), supplierDTO.getName());
        assertEquals(supplier.getPhoneNum(), supplierDTO.getPhoneNum());
        assertEquals(supplier.getEmail(), supplierDTO.getEmail());

    }

    @Test
    void getSupplierById_ShouldThrowExceptionIfSupplierNotFound() {

        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

       assertThrows(EntityNotFoundException.class, () -> {
            supplierService.getSupplierById(999L);
        });

    }

    @Test
    void getSupplierById_ShouldThrowExceptionForInvalidId() {

        Long id = -1L;

         assertThrows(IllegalArgumentException.class, () -> {
            supplierService.getSupplierById(id);
        });

    }

    @Test
    void saveSupplier_ShouldReturnSavedSupplierDTO() {

        SupplierDTO supplierDTO = new SupplierDTO(1L, "Test Supplier", "888-900-989", "testemail@email.com");

        when(supplierRepository.save(ArgumentMatchers.any(Supplier.class))).thenReturn(supplier);

        SupplierDTO supplierDTOupdated = supplierService.saveSupplier(supplierDTO);

        assertNotNull(supplierDTOupdated);
        assertEquals(supplierDTOupdated.getName(), supplierDTO.getName());
        assertEquals(supplierDTOupdated.getPhoneNum(), supplierDTO.getPhoneNum());
        assertEquals(supplierDTOupdated.getEmail(), supplierDTO.getEmail());

    }

    @Test
    void saveSupplier_ShouldHandleNullDTO() {

        SupplierDTO supplierDTO = null;

        assertThrows(EntityNotFoundException.class, () -> {
            supplierService.saveSupplier(supplierDTO);
        });

    }

    @Test
    void updateSupplier_ShouldReturnUpdatedSupplierDTO() {
        SupplierDTO supplierDTO = new SupplierDTO(1L, "Test Supplier 2", "888-900-9800", "testemail@email.com");

        when(supplierRepository.findById(1L)).thenReturn(Optional.of(supplier));

        SupplierDTO result = supplierService.updateSupplier(supplierDTO, 1L);

        assertNotNull(result);
        assertEquals(result.getName(), supplierDTO.getName());
        assertEquals(result.getPhoneNum(), supplierDTO.getPhoneNum());
        assertEquals(result.getEmail(), supplierDTO.getEmail());

    }

    @Test
    void updateSupplier_ShouldThrowExceptionIfSupplierNotFound() {

        SupplierDTO supplierDTO = null;

        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () -> {
            supplierService.updateSupplier(supplierDTO, 999L);
        });

    }

    @Test
    void getSuppliers_ShouldReturnSupplierDTOList() {
        Supplier s1 = new Supplier(2L,"Test Supplier 2", "678-908-7890", "testsupplier2@email.com");
        Supplier s2 = new Supplier(3L,"Test Supplier 3", "678-678-7890", "testsupplier3@email.com");

        List<Supplier> suppliers= List.of(s1,s2);

        when(supplierRepository.findAll()).thenReturn(suppliers);

        List<SupplierDTO> supplierDTOS = supplierService.getSuppliers();

        assertNotNull(supplierDTOS);
        assertEquals(2,supplierDTOS.size());
        assertEquals(s1.getName(),supplierDTOS.get(0).getName());
        assertEquals(s2.getName(),supplierDTOS.get(1).getName());
    }

    @Test
    void getSuppliers_ShouldReturnEmptyListWhenNoSuppliers() {

        when(supplierRepository.findAll()).thenReturn(new ArrayList<>());

        List<SupplierDTO> supplierDTOS = supplierService.getSuppliers();

        assertNotNull(supplierDTOS);
        assertTrue(supplierDTOS.isEmpty());

    }

    @Test
    void getSuppliers_ShouldReturnCorrectNumberOfProducts() {
        List<Supplier> suppliers = new ArrayList<>();
        for (long i = 1; i <= 1000; i++) {
            suppliers.add(new Supplier(i, "Supplier " + i, "780-098-9008", "Supplier"+ i +"@email.com"));
        }

        when(supplierRepository.findAll()).thenReturn(suppliers);

        List<SupplierDTO> supplierDTOS = supplierService.getSuppliers();

        assertNotNull(supplierDTOS);
        assertEquals(supplierDTOS.size(),1000);

    }

    @Test
    void deleteSupplier_DeleteIfExists() {
        SupplierDTO supplierDTO = new SupplierDTO(supplier.getId(),supplier.getName(),supplier.getPhoneNum(),supplier.getEmail());

        when(supplierRepository.existsById(supplierDTO.getId())).thenReturn(true);
        doNothing().when(supplierRepository).deleteById(supplierDTO.getId());

        supplierService.deleteSupplier(supplierDTO.getId());

        verify(supplierRepository,times(1)).deleteById(supplierDTO.getId());

    }

    @Test
    void deleteSupplier_ShouldThrowExceptionIfProductNotFound() {

        Long id =999L;

        when(supplierRepository.findById(999L)).thenReturn(Optional.empty());

         assertThrows(EntityNotFoundException.class,()->{supplierService.deleteSupplier(id);});
    }

    @Test
    void deleteSupplier_ShouldThrowExceptionForInvalidId() {

        Long id =-1L;

        assertThrows(EntityNotFoundException.class,()->{supplierService.deleteSupplier(id);});
    }
}

