package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.ESize;
import com.manguerasjc.productservice.dataAccess.domain.Size;
import com.manguerasjc.productservice.dataAccess.repositories.ISizeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SizeService implements ISizeService{

    @Autowired
    private ISizeRepository sizeRepository;

    public List<Size>getSizes(){
        return sizeRepository.findAll();
    }

    public Size addSize(String nameSize){
        Size size = null;
        try {
            ESize sizeEnum = ESize.valueOf(nameSize);
            if(sizeRepository.findSizeBySize(sizeEnum)==null){
                sizeRepository.save(new Size(sizeEnum));
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("La talla no es válida, debe ser S,M,L,XL,XXL, o XXXL");
        }
        return size;
    }

    public Boolean deleteSize(Long id){
        Size size = sizeRepository.findById(id).orElseThrow(() -> new RuntimeException("La talla a eliminar no fue encontrada."));
        sizeRepository.delete(size);
        return true;
    }
}
