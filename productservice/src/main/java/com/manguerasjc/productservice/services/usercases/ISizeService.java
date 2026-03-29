package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Size;

import java.util.List;

public interface ISizeService {
    List<Size>getSizes();
    Size addSize(String nameSize);
    Boolean deleteSize(Long id);
}
