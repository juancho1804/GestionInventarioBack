package com.manguerasjc.productservice.controllers;

import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;
import com.manguerasjc.productservice.services.usercases.IProductService;
import com.manguerasjc.productservice.services.usercases.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping
    public ProductResponseDTO createProduct(@RequestBody ProductRequestDTO productRequestDTO) {
        return productService.addProduct(productRequestDTO);
    }

    @PutMapping
    public ProductResponseDTO updateProduct(@RequestParam Long id,@RequestBody ProductRequestDTO productRequestDTO) {
        return productService.updateProduct(id, productRequestDTO);
    }

    @GetMapping
    public List<ProductResponseDTO> getProducts(){
        return productService.getProducts();

    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}
