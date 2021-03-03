package com.project.aws_project01.controllers;

import com.project.aws_project01.enums.EventType;
import com.project.aws_project01.models.Product;
import com.project.aws_project01.repositories.ProductRepository;
import com.project.aws_project01.services.ProductPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductRepository productRepository;
    private final ProductPublisher productPublisher;

    @GetMapping
    public Iterable<Product> findAll() {
        return productRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> findById(@PathVariable long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        return optProduct
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @PostMapping
    public ResponseEntity<Product> saveProduct(
            @RequestBody Product product) {
        Product productCreated = productRepository.save(product);
        productPublisher.publishProductEvent(productCreated, EventType.PRODUCT_CREATED, "admin");
        return new ResponseEntity<Product>(productCreated,
                HttpStatus.CREATED);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<Product> updateProduct(
            @RequestBody Product product, @PathVariable("id") long id) {
        if (productRepository.existsById(id)) {
            product.setId(id);
            Product productUpdated = productRepository.save(product);
            productPublisher.publishProductEvent(productUpdated, EventType.PRODUCT_UPDATED, "admin");
            return new ResponseEntity<Product>(productUpdated, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<Product> deleteProduct(@PathVariable("id") long id) {
        Optional<Product> optProduct = productRepository.findById(id);
        if (optProduct.isPresent()) {
            Product product = optProduct.get();
            productRepository.delete(product);
            productPublisher.publishProductEvent(product, EventType.PRODUCT_DELETED, "admin");
            return new ResponseEntity<Product>(product, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping(path = "/bycode")
    public ResponseEntity<Product> findByCode(@RequestParam String code) {
        Optional<Product> optProduct = productRepository.findByCode(code);
        return optProduct
                .map(product -> new ResponseEntity<>(product, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
}