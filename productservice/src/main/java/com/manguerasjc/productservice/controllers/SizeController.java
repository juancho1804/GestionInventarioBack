package com.manguerasjc.productservice.controllers;

import com.manguerasjc.productservice.dataAccess.domain.Size;
import com.manguerasjc.productservice.services.usercases.ISizeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/sizes")
public class SizeController {
    @Autowired
    private ISizeService sizeService;

    @GetMapping
    public List<Size>getSizes(){
        return sizeService.getSizes();
    }

    @PostMapping
    public Size addSize(@RequestParam(name = "nameSize") String nameSize){
        return sizeService.addSize(nameSize);
    }

    @DeleteMapping("/{id}")
    public Boolean deleteSize(@PathVariable Long id){
        return sizeService.deleteSize(id);
    }
}
