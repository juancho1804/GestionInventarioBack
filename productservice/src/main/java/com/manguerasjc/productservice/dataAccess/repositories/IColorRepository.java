package com.manguerasjc.productservice.dataAccess.repositories;

import com.manguerasjc.productservice.dataAccess.domain.Color;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IColorRepository extends JpaRepository<Color, Long> {
}
