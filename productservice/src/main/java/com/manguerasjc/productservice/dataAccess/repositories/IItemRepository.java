package com.manguerasjc.productservice.dataAccess.repositories;

import com.manguerasjc.productservice.dataAccess.domain.Item;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IItemRepository extends JpaRepository<Item, Long> {
}
