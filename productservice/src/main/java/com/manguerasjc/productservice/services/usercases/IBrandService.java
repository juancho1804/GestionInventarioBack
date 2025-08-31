package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Brand;

import java.util.List;

public interface IBrandService {
    List<Brand> listBrands();
    Brand addBrand(Brand brand);
    Boolean deleteBrand(Long id);
}
