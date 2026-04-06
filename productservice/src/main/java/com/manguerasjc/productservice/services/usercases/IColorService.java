package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Color;

import java.util.List;

public interface IColorService {
    void addColor(Color color);
    List<Color>getColors();
    Boolean deleteColor(Long id);
}
