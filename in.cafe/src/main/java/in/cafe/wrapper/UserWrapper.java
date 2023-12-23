package in.cafe.wrapper;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class UserWrapper {

    private Integer id;
    
    private String name;

    private String email;

    private String userPhone;

    private String status;

    public UserWrapper(Integer id, String name, String email, String userPhone, String status) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.userPhone = userPhone;
        this.status = status;
    }
}
