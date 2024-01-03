package com.cafe.servicesImp;

import com.cafe.JWT.CustomerUsersDetailsService;
import com.cafe.JWT.JwtFilter;
import com.cafe.JWT.JwtUtil;
import com.cafe.constants.CafeConstants;
import com.cafe.repo.repository;
import com.cafe.utils.CafeUtils;
import com.cafe.utils.EmailUtils;
import com.cafe.wrapper.UserWrapper;
import com.google.common.base.Strings;
import com.cafe.entities.User;
import com.cafe.services.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Slf4j
@Service
public class userServiceImpl implements UserService {
  @Autowired
  private repository repo;

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  CustomerUsersDetailsService customerUsersDetailsService;

  @Autowired
  private UserService userService;
  @Autowired
  private JwtUtil jwtUtil;

  @Autowired
  private JwtFilter jwtFilter;

  @Autowired
  private EmailUtils emailUtils;
  
  

    @Override
    public ResponseEntity<String> signUp(Map<String, String> requestMap) {
        log.info("inside signup {} ",requestMap);
        try {
            if (validateSignUpMap(requestMap)) {
                User user = repo.findByEmailId(requestMap.get("email"));
                if (Objects.isNull(user)) {
                    repo.save(getUserFromMap(requestMap));
                    return CafeUtils.getResponseEntity(CafeConstants.SUCCESSFULLY_REGISTERED, HttpStatus.OK);
                } else {
                    return CafeUtils.getResponseEntity(CafeConstants.EMAIL_ALREADY_EXITS, HttpStatus.BAD_REQUEST);
                }
            } else {
                return CafeUtils.getResponseEntity(CafeConstants.INVALID_Data, HttpStatus.BAD_REQUEST);
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }


    private boolean validateSignUpMap(Map<String, String> requestMap){
         if(requestMap.containsKey("name") && requestMap.containsKey("userPhone")
         && requestMap.containsKey("email") && requestMap.containsKey("password")){
             return true;
    }else{
             return false;
         }
    }

    private User getUserFromMap(Map<String,String> requestMap){

        User user = new User();
        user.setName(requestMap.get("name"));
        user.setUserPhone(requestMap.get("userPhone"));
        user.setEmail(requestMap.get("email"));
        user.setPassword(requestMap.get("password"));
        user.setStatus("false");
        user.setRole("user");
        return user;


    }
    @Override
    public ResponseEntity<String> login(Map<String, String> requestMap) {
        log.info("Inside login");
        try{
            Authentication auth = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(requestMap.get("email"), requestMap.get("password")));
            if(auth.isAuthenticated()){
                if(customerUsersDetailsService.getUserDetail().getStatus().equalsIgnoreCase("true")){
                    return new ResponseEntity<String>("{\"token\":\""+
                jwtUtil.generateToken(customerUsersDetailsService.getUserDetail().getEmail(),
                        customerUsersDetailsService.getUserDetail().getRole())+"\"}",HttpStatus.OK);
                }
                else{
                    return new ResponseEntity<String>("{\"message\":\""+"Wait for Admin Approval."+"\"}",
                            HttpStatus.BAD_REQUEST);
                }
            }
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<String>("{\"message\":\""+"Bad Credentials."+"\"}",
                HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<List<UserWrapper>> getAllUser() {
        try{
            if(jwtFilter.isAdmin()){

                List<UserWrapper> userWrappers = new ArrayList<>();

                List<User> users = repo.getAllUser();

                for (User user: users) {
                    UserWrapper userWrapper = new UserWrapper();
                    try{
                        BeanUtils.copyProperties(user,userWrapper);
                    }
                    catch (Exception ex){
                        ex.printStackTrace();
                    }
                    userWrappers.add(userWrapper);
                }


                return new ResponseEntity<>(userWrappers,HttpStatus.OK);
            }else{
                return new ResponseEntity<>(new ArrayList<>(),HttpStatus.UNAUTHORIZED);
            }

        }catch (Exception ex){
            ex.printStackTrace();
        }
        return new ResponseEntity<>(new ArrayList<>(),HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> update(Map<String, String> requestMap) {
       try{
            if(jwtFilter.isAdmin()){
                Optional<User> optional = repo.findById(Integer.parseInt(requestMap.get("id")));
                if(!optional.isEmpty()){
                        repo.updateStatus(requestMap.get("status"),Integer.parseInt(requestMap.get(("id"))));
                        sendMailToAllAdmin(requestMap.get("status"),optional.get().getEmail(),repo.getAllAdmin());
                        return CafeUtils.getResponseEntity("User Status Updated Successfully",HttpStatus.OK);
                }
                else{
                    CafeUtils.getResponseEntity("User id doesn't not exist",HttpStatus.OK);
                }
            }else{
                return CafeUtils.getResponseEntity(CafeConstants.UNAUTHORIZED_ACCESS,HttpStatus.UNAUTHORIZED);
            }
       }catch(Exception ex){
           ex.printStackTrace();
       }

        return null;
    }

    @Override
    public ResponseEntity<String> checkToken() {
    return CafeUtils.getResponseEntity("true",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> changePassword(Map<String, String> requestMap) {
        try{
            User userObj = repo.findByEmail(jwtFilter.getCurrentUser());
            if(!userObj.equals(null)){
                  if(userObj.getPassword().equals(requestMap.get("oldPassword"))){
                     userObj.setPassword(requestMap.get("newPassword"));
                     repo.save(userObj);
                     return CafeUtils.getResponseEntity("Password Updated Successfully",HttpStatus.OK);
                  }
                  return CafeUtils.getResponseEntity("Incorrect Old Password",HttpStatus.OK);
            }
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<String> forgotPassword(Map<String, String> requestMap) {
        try{
            User user = repo.findByEmail(requestMap.get("email"));
            if(!Objects.isNull(user) && !Strings.isNullOrEmpty(user.getEmail())){

                emailUtils.forgotMail(user.getEmail(),"Credentials by Cafe Management System",user.getPassword());

               return CafeUtils.getResponseEntity("Check your mail for Credentials",HttpStatus.OK);
            }
            return CafeUtils.getResponseEntity("Password Updated Successfully",HttpStatus.OK);

        }catch(Exception ex){
            ex.printStackTrace();
        }
        return CafeUtils.getResponseEntity(CafeConstants.SOMETHING_WENT_WRONG,HttpStatus.INTERNAL_SERVER_ERROR);

    }

    private void sendMailToAllAdmin(String status, String user, List<String> allAdmin) {
           allAdmin.remove(jwtFilter.getCurrentUser());
           if(status != null && status.equalsIgnoreCase("true")){
                 emailUtils.sendEmail(jwtFilter.getCurrentUser(),"Account Approved","USER:- "+user+" \n is approved by \nADMIN:-" + jwtFilter.getCurrentUser(),allAdmin);
           }else{
               emailUtils.sendEmail(jwtFilter.getCurrentUser(),"Account Approved","USER:- "+user+" \n is disabled by \nADMIN:-" + jwtFilter.getCurrentUser(),allAdmin);
           }
    }


}