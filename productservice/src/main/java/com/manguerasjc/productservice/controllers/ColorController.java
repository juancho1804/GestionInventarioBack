package com.manguerasjc.productservice.controllers;

import com.manguerasjc.productservice.dataAccess.domain.Color;
import com.manguerasjc.productservice.services.usercases.IColorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/colors")
public class ColorController {

    @Autowired
    private IColorService colorService;

    @PostMapping
    public void addColor(@RequestBody Color color){
        colorService.addColor(color);
    }

    @GetMapping
    public List<Color> getColors(){
        return colorService.getColors();
    }

    @DeleteMapping
    public Boolean deleteColor(@RequestParam Long id){
        return colorService.deleteColor(id);
    }

}
