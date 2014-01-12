package beans.accountmanagement;

import javax.validation.constraints.Pattern;
import org.hibernate.validator.constraints.NotEmpty;

public class UserDTO {
	
	@Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
            message="invalid email")
	private String email;
	
	@NotEmpty
    private String firstName;
	
	@NotEmpty
    private String lastName;
	
	@NotEmpty
    private String password;
	
    public String getFirstName() {
        return firstName;
    }
     
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }
  
    public String getLastName() {
        return lastName;
    }
  
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
  
    public String getEmail() {
        return email;
    } 
 
    public void setEmail(String email) {
        this.email = email;
    }
  
    public String getPassword() {
        return password;
    }
     
    public void setPassword(String password) {
        this.password = password;
    }
     
}
