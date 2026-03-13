package com.manguerasjc.productservice.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.manguerasjc.productservice.services.DTO.request.ProductRequestDTO;
import com.manguerasjc.productservice.services.DTO.request.ProductVariantRequestDTO;
import com.manguerasjc.productservice.services.DTO.response.ProductResponseDTO;
import com.manguerasjc.productservice.services.usercases.IProductService;
import com.manguerasjc.productservice.services.usercases.ProductService;
import jdk.jfr.ContentType;
import org.hibernate.annotations.Parameter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@RestController
@RequestMapping("/products")
public class ProductController {

    @Autowired
    private IProductService productService;

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponseDTO addProduct(
            @RequestParam(value = "categoryId") Long categoryId,
            @RequestParam(value = "color") String color,
            @RequestParam(value = "brandId") Long brandId,
            @RequestParam(value = "price") Double price,
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestPart(value = "productVariantRequest") String productVariantRequest
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ProductVariantRequestDTO productVariantRequestDTO =
                mapper.readValue(productVariantRequest, ProductVariantRequestDTO.class);

        ProductRequestDTO dto = new ProductRequestDTO(categoryId, color, brandId, price, image, productVariantRequestDTO);

        return productService.addProduct(dto);
    }


    @PutMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ProductResponseDTO updateProduct(@RequestParam Long id,
                                            @RequestParam("categoryId") Long categoryId,
                                            @RequestParam("color") String color,
                                            @RequestParam("brandId") Long brandId,
                                            @RequestParam("price") Double price,
                                            @RequestPart(value = "image", required = false) MultipartFile image,
                                            @RequestPart("productVariantRequest") String productVariantRequestJson
    ) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        ProductVariantRequestDTO productVariantRequest =
                mapper.readValue(productVariantRequestJson, ProductVariantRequestDTO.class);

        ProductRequestDTO dto = new ProductRequestDTO(categoryId, color, brandId, price, image, productVariantRequest);
        return productService.updateProduct(id,dto);
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
