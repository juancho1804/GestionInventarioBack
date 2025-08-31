package com.manguerasjc.productservice.controllers;

import com.manguerasjc.productservice.dataAccess.domain.Brand;
import com.manguerasjc.productservice.services.usercases.IBrandService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/brands")
public class BrandController {

    @Autowired
    private IBrandService brandService;

    @GetMapping
    public List<Brand> listBrands(){
        return brandService.listBrands();
    }

    @PostMapping
    public Brand addBrand(@RequestBody Brand brand){
        return brandService.addBrand(brand);
    }

    @DeleteMapping("/{id}")
    public boolean deleteBrand(@PathVariable Long id){
        return brandService.deleteBrand(id);
    }


}
