package com.manguerasjc.productservice.dataAccess.repositories;

import com.manguerasjc.productservice.dataAccess.domain.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
}
