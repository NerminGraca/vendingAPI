package ba.ngraca.vending.payload.request;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
 
public class SignupRequest {
    @NotBlank(message = "Username field cannot be empty")
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank(message = "Role field cannot be empty")
    private String role;
    
    @NotBlank(message = "Password field cannot be empty")
    @Size(min = 6, max = 40)
    private String password;
  
    public String getUsername() {
        return username;
    }
 
    public void setUsername(String username) {
        this.username = username;
    }
 
    public String getPassword() {
        return password;
    }
 
    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
