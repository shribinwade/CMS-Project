package com.cafe.repo;

import com.cafe.wrapper.ProductWrapper;
import com.cafe.entities.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepo extends JpaRepository<Product,Integer> {

    List<ProductWrapper> getAllProduct();
}
