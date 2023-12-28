package com.cafe.servicesImp;

import com.cafe.entities.Category;
import com.cafe.wrapper.ProductWrapper;
import com.cafe.JWT.JwtFilter;
import com.cafe.constants.CafeConstants;
import com.cafe.entities.Product;
import com.cafe.repo.ProductRepo;
import com.cafe.services.ProductService;
import com.cafe.utils.CafeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    JwtFilter jwtFilter;

    @Autowired
    ProductRepo productRepo;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {
        try{
             if(jwtFilter.isAdmin()){
                 if(validateProductMap(requestMap,false)){
                     productRepo.save(getProductFromMap(requestMap, false));
                     return CafeUtils.getResponseEntity("Product Added Successfully",HttpStatus.OK);
                 }
                 return CafeUtils.getResponseEntity(CafeConstants.INVALID_Data,HttpStatus.BAD_REQUEST);
             }else{
                 return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
             }

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }




    private boolean validateProductMap(Map<String, String> requestMap, boolean validateId) {
        if(requestMap.containsKey("name")){
            if(requestMap.containsKey("id") && validateId){
                return true;
            }else if (!validateId) {
                return true;
            }
        }
        return false;
    }

    private Product getProductFromMap(Map<String, String> requestMap, boolean isAdd) {
        Category category = new Category();
        category.setId(Integer.parseInt(requestMap.get("categoryId")));

        Product product = new Product();
        if(isAdd){
            product.setId(Integer.parseInt(requestMap.get("categoryId")));
        }else{
            product.setStatus("true");
        }
        product.setCategory(category);
        product.setName(requestMap.get("name"));
        product.setDescription(requestMap.get("description"));
        product.setPrice(Integer.parseInt(requestMap.get("price")));

        return product;


    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
       try{
           return new ResponseEntity<>(productRepo.getAllProduct(),HttpStatus.OK);
       }catch (Exception ex){
           ex.printStackTrace();
       }

        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
