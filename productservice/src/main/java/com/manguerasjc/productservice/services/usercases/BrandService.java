package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Brand;
import com.manguerasjc.productservice.dataAccess.repositories.IBrandRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BrandService implements IBrandService {

    @Autowired
    private IBrandRepository brandRepository;

    public List<Brand> listBrands(){
        return brandRepository.findAll();
    }

    public Brand addBrand(Brand brand){
        return brandRepository.save(brand);
    }

    public Boolean deleteBrand(Long id){
        if(!brandRepository.existsById(id)){
            throw new EntityNotFoundException("La marca no se encontró");
        }
        brandRepository.deleteById(id);
        return true;
    }
}
