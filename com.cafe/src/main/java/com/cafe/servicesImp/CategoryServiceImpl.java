package com.cafe.servicesImp;

import com.cafe.JWT.JwtFilter;
import com.cafe.entities.Category;
import com.cafe.repo.CategoryRepo;
import com.cafe.utils.CafeUtils;
import com.google.common.base.Strings;
import com.cafe.constants.CafeConstants;
import com.cafe.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryRepo categoryRepo;

    @Autowired
    JwtFilter jwtFilter;

    @Override
    public ResponseEntity<String> addNewCategory(Map<String, String> requestMap) {
       try{
           if(jwtFilter.isAdmin()){
               if(validateCategoryMap(requestMap,false)){
                   categoryRepo.save(getCategoryFromMap(requestMap,false));
                   return CafeUtils.getResponseEntity("Category Added Successfully",HttpStatus.OK);
               }
           }else{
               return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS, HttpStatus.UNAUTHORIZED);
           }
       }catch(Exception ex){
           ex.printStackTrace();
       }
       return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<Category>> getAllCategory(String filterValue) {
              try{
                    if(!Strings.isNullOrEmpty(filterValue) && filterValue.equalsIgnoreCase("true")){
                        return new ResponseEntity<List<Category>>(categoryRepo.getAllCategory(),HttpStatus.OK);
                    }
                    return new ResponseEntity<>(categoryRepo.findAll(),HttpStatus.OK);
              }catch(Exception ex){
                  ex.printStackTrace();
              }
              return new ResponseEntity<List<Category>>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> updateCategory(Map<String, String> requestMap) {
        try{
             if(jwtFilter.isAdmin()) {
                 if (validateCategoryMap(requestMap, true)) {
                     Optional optional = categoryRepo.findById(Integer.parseInt(requestMap.get("id")));
                     if (optional.isPresent()) {
                         categoryRepo.save(getCategoryFromMap(requestMap, true));
                         return CafeUtils.getResponseEntity("Category Updated Successfully", HttpStatus.OK);
                     } else {
                         return CafeUtils.getResponseEntity("Category id does not exits", HttpStatus.OK);
                     }
                 }
                 return CafeUtils.getResponseEntity(CafeConstants.INVALID_Data, HttpStatus.BAD_REQUEST);
             }else{
                 return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
             }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private boolean validateCategoryMap(Map<String, String> requestMap, boolean validateId) {
         if(requestMap.containsKey("name")){
             if(requestMap.containsKey("id") && validateId){
                 return true;
             }else if (!validateId) {
                  return true;
             }

         }
        return false;
    }

    private Category getCategoryFromMap(Map<String,String> requestMap,Boolean isAdd){
        Category category = new Category();
        if(isAdd){
            category.setId(Integer.parseInt(requestMap.get("id")));
        }
        category.setName(requestMap.get("name"));
        return category;
    }

}
