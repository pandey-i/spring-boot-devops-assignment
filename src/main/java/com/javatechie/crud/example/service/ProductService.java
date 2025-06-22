package com.javatechie.crud.example.service;

import com.javatechie.crud.example.entity.Product;
import com.javatechie.crud.example.exception.ProductNotFoundException;
import com.javatechie.crud.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository repository;

    public Product saveProduct(Product product) {
        return repository.save(product);
    }

    public List<Product> saveProducts(List<Product> products) {
        return repository.saveAll(products);
    }

    public List<Product> getProducts() {
        return repository.findAll();
    }

    public Product getProductById(int id) {
        return repository.findById(id).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + id));
    }

    public Product getProductByName(String name) {
        Product product = repository.findByName(name);
        if (product == null) {
            throw new ProductNotFoundException("Product not found with name: " + name);
        }
        return product;
    }

    public List<Product> searchProducts(String keyword, Double minPrice, Double maxPrice) {
        if (minPrice != null && maxPrice != null) {
            return repository.findByNameContainingAndPriceBetween(keyword, minPrice, maxPrice);
        } else {
            return repository.findByNameContaining(keyword);
        }
    }

    public String deleteProduct(int id) {
        repository.deleteById(id);
        return "product removed !! " + id;
    }

    public Product updateProduct(Product product) {
        Product existingProduct = repository.findById(product.getId()).orElseThrow(() -> new ProductNotFoundException("Product not found with id: " + product.getId()));
        existingProduct.setName(product.getName());
        existingProduct.setQuantity(product.getQuantity());
        existingProduct.setPrice(product.getPrice());
        return repository.save(existingProduct);
    }


}
