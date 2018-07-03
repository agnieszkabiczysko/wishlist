package agnieszka.wishlist.model;

import javax.validation.constraints.AssertTrue;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class Password {

	private static final String PASSWORD_PATTERN =
            "(((?=.*[\\d])(?=.*[a-z])(?=.*[A-Z])(?=.*[@!#$%])).{6,20})";
			
	@NotNull(message="{password.NotNull.validation}")
	@Size(min = 6, max = 20, message = "{password.Size.validation}")
	@Pattern(regexp=PASSWORD_PATTERN, message = "{password.Pattern.validation}")
	private String password;
    private String passwordConfirmation;
    
    @AssertTrue(message="{password.checkPassword.validation}")
    public boolean isValid() {
      return this.password.equals(this.passwordConfirmation);
    }
    
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getPasswordConfirmation() {
		return passwordConfirmation;
	}
	public void setPasswordConfirmation(String passwordConfirmation) {
		this.passwordConfirmation = passwordConfirmation;
	}
	
}
