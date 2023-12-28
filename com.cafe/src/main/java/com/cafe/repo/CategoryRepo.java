package com.cafe.repo;

import com.cafe.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category,Integer> {

    @Query("Select c from Category c where c.id in ( select p.category from Product p where p.status = 'true')")
    List<Category> getAllCategory();



}
