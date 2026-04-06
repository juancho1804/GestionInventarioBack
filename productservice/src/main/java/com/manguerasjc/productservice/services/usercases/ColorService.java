package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Color;
import com.manguerasjc.productservice.dataAccess.repositories.IColorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ColorService implements IColorService{

    @Autowired
    private IColorRepository colorRepository;

    @Override
    public void addColor(Color color) {
        colorRepository.save(color);
    }

    @Override
    public List<Color> getColors() {
        return colorRepository.findAll();
    }

    @Override
    public Boolean deleteColor(Long id) {
        boolean bandera = false;
        if(colorRepository.existsById(id)){
            colorRepository.deleteById(id);
            bandera = true;
        }
        return bandera;
    }


}
