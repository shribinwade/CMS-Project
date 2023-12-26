package in.cafe.repo;

import in.cafe.entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface CategoryRepo extends JpaRepository<Category,Integer> {

    @Query("Select c from Category c")
    List<Category> getAllCategory();



}
