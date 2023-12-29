package com.cafe.repo;

import com.cafe.wrapper.ProductWrapper;
import com.cafe.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;

import java.util.List;

import javax.transaction.Transactional;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    List<ProductWrapper> getAllProduct();
    
    @Modifying
    @Transactional
    Integer updateProductStatus(@Param("status") String status, @Param("id") int id);
    
    List<ProductWrapper> getProductByCategory(@Param("id") Integer id);
    
    ProductWrapper getProductById(@Param("id") Integer id);
}
