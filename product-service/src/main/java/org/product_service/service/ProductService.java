package org.product_service.service;

import org.product_service.entity.Product;
import org.product_service.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repository;

    public Product addProduct(Product product) {
        repository.findByName(product.getName()).ifPresent(existing -> {
            throw new IllegalArgumentException("Product with name '" + product.getName() + "' already exists.");
        });
        return repository.save(product);
    }

    public List<Product> addMultipleProducts(List<Product> products) {
        Set<String> names = new HashSet<>();
        for (Product p : products) {
            if (!names.add(p.getName().toLowerCase())) {
                throw new IllegalArgumentException("Duplicate product name in input list: " + p.getName());
            }
        }

        List<String> existingNames = repository.findAll().stream()
                .map(p -> p.getName().toLowerCase())
                .toList();

        List<Product> nonDuplicateProducts = products.stream()
                .filter(p -> !existingNames.contains(p.getName().toLowerCase()))
                .toList();

        if (nonDuplicateProducts.isEmpty()) {
            throw new IllegalArgumentException("All product names already exist in the system.");
        }

        return repository.saveAll(nonDuplicateProducts);
    }


    public List<Product> getAllProducts(int offset, int noOfRecords) {
        if(offset >= 0 && noOfRecords > 0){
            return repository.findAll().subList(offset * noOfRecords, noOfRecords);
        }
        return repository.findAll();
    }

    public Product getProductById(UUID id) throws Exception {
        return repository.findById(id).orElseThrow(Exception::new);
    }

    public Product updateProduct(Product productToUpdate) {
        Product product;
        try {
            product = getProductById(productToUpdate.getId());
        } catch (Exception e) {
            throw new RuntimeException();
        }

        product.productUpdate(productToUpdate);
        return repository.save(product);
    }

    public String deleteProduct(UUID id) {
        try {
            getProductById(id);
        } catch (Exception e) {
            return "Product id: " + id + " not found to delete";
        }
        repository.deleteById(id);
        return "Successfully Deleted the product details with id " + id;
    }
}
