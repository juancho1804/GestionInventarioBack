package com.manguerasjc.productservice.services.usercases;

import com.manguerasjc.productservice.dataAccess.domain.Category;
import com.manguerasjc.productservice.dataAccess.domain.Product;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;

public class ProductSpecification {

    public static Specification<Product>hasCategoriesIds(List<Long> categoriesIds){
        return (root, cb, query)->{
            if(categoriesIds == null || categoriesIds.isEmpty()) return null;
            return root.get("category").get("id").in(categoriesIds);
        };
    }

    public static Specification<Product>hasBrandsIds(List<Long> brandsIds){
        return (root, cb, query)->{
            if(brandsIds == null || brandsIds.isEmpty()) return null;
            return root.get("brand").get("id").in(brandsIds);
        };
    }

    public static Specification<Product> hasSizesIds(List<Long> sizesIds) {
        return (root, query, cb) -> {
            if (sizesIds == null || sizesIds.isEmpty()) return null;

            assert query != null;
            query.distinct(true);

            var variantsJoin = root.join("variants");
            return variantsJoin.get("size").get("id").in(sizesIds);
        };
    }
}
