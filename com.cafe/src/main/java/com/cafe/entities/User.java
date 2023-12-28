package com.cafe.entities;



import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

//@NamedQuery(name= "User.getAllUser", query = "SELECT NEW in.cafe.wrapper.UserWrapper(u.id, u.name, u.email, u.userPhone, u.status) FROM User u WHERE u.role = 'user'")
@Data
@Entity
@DynamicUpdate
@DynamicInsert
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user")
public class User implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    private String email;

    private String password;

    private String userPhone;

    private String status;

    private String role;


    public <E> User(String email, String password, ArrayList<E> es) {
    }


}
