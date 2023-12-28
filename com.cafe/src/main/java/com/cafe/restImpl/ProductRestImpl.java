package com.cafe.restImpl;

import com.cafe.services.ProductService;
import com.cafe.utils.CafeUtils;
import com.cafe.wrapper.ProductWrapper;
import com.cafe.constants.CafeConstants;
import com.cafe.rest.ProductRest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
public class ProductRestImpl implements ProductRest {

    @Autowired
    ProductService productService;

    @Override
    public ResponseEntity<String> addNewProduct(Map<String, String> requestMap) {

        try{
              return  productService.addNewProduct(requestMap);
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<List<ProductWrapper>> getAllProduct() {
          try{
                return productService.getAllProduct();
          }catch(Exception ex){
              ex.printStackTrace();
          }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
