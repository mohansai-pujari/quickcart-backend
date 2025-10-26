package org.product_service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.product_service.entity.Product;
import org.product_service.repository.ProductRepository;
import org.product_service.service.ProductService;

import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;


    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    private static final UUID FIXED_PRODUCT_ID = UUID.fromString("987e6543-e21b-43d3-b456-123456789000");

    @Test
    void testGetProductById() throws Exception {
        Product product = new Product(FIXED_PRODUCT_ID, "Wireless Mouse", "WM-001",
                30000.0, 15, true);

        when(productRepository.findById(FIXED_PRODUCT_ID)).thenReturn(Optional.of(product));

        Product result = productService.getProductById(FIXED_PRODUCT_ID);

        assertNotNull(result);
        assertEquals("Wireless Mouse", result.getName());
        verify(productRepository, times(1)).findById(FIXED_PRODUCT_ID);
    }

    @Test
    void testAddProduct() {
        Product product = new Product(FIXED_PRODUCT_ID, "Wireless Mouse", "WM-001",
                30000.0, 15, true);

        when(productRepository.save(product)).thenReturn(product);

        Product saved = productService.addProduct(product);

        assertEquals(15, saved.getStock());
        assertTrue(saved.isActiveStatus());
        verify(productRepository, times(1)).save(product);
    }
}
