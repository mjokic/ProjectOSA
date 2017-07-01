package sf282015.osa.projectOSA.security;

public class AccountCredentials {

	private String email;
	private String password;
	private String device_token;
	
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
	

	// testing...
	public String getDevice_token() {
		return device_token;
	}
	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}
	
	
	@Override
	public String toString() {
		return "AccountCredentials [email=" + email + ", password=" + password + ", device_token=" + device_token + "]";
	}
	
}
