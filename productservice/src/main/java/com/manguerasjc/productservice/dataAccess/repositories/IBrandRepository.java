package com.manguerasjc.productservice.dataAccess.repositories;

import com.manguerasjc.productservice.dataAccess.domain.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IBrandRepository extends JpaRepository<Brand, Long> {
}
