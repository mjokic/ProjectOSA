package sf282015.osa.projectOSA.dto;

import sf282015.osa.projectOSA.entity.User;

public class UserDTO {
	

	private String name;
	private String email;
	private String picture;
	private String address;
	private String phone;
	private String password;
	
	
	public UserDTO(){}
	
	public UserDTO(User user){
		this.name = user.getName();
		this.email = user.getEmail();
		this.picture = user.getPicture();
		this.address = user.getAddress();
		this.phone = user.getPhone();
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getPicture() {
		return picture;
	}
	public void setPicture(String picture) {
		this.picture = picture;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
}
