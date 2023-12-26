package in.cafe.restImpl;

import in.cafe.constants.CafeConstants;
import in.cafe.entities.Category;
import in.cafe.rest.categoryRest;
import in.cafe.services.CategoryService;
import in.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class categoryRestImpl implements categoryRest {

    @Autowired
    CategoryService categoryService;
    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {

        try{
            return categoryService.addNewCategory(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
        try{
            return categoryService.getAllCategory(filterValue);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
            return categoryService.updateCategory(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }
}