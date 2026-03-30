package com.manguerasjc.productservice.dataAccess.repositories;

import com.manguerasjc.productservice.dataAccess.domain.ESize;
import com.manguerasjc.productservice.dataAccess.domain.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ISizeRepository extends JpaRepository<Size, Long> {
    Size findSizeBySize(ESize size);
}
