package sf282015.osa.projectOSA.entity;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import sf282015.osa.projectOSA.dto.UserDTO;

@Entity
public class User {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(name="email", nullable=false, unique=true, length=40)
	private String email;
	
	@Column(nullable = false)
	@JsonProperty(access=Access.WRITE_ONLY)
	private String password;
	
	@Column(nullable = false)
	private String picture;
	
	@Column(nullable = false)
	private String address;
	
	@Column(nullable = false, unique=true, length=15)
	private String phone;
	
	@Column(nullable = false)
//	@JsonProperty(access=Access.READ_ONLY)
	private String role;
	
	@Column(nullable = false)
	private boolean active;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private Set<Bid> bids;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private Set<Auction> auctions;

	// testing...
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="user")
	private Set<Item> items;
	//
	
	@JsonProperty(access=Access.READ_ONLY)
	@Column(name="device_token", nullable=true)
	private String device_token;
	
	public User(){}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
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

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
	
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

	@JsonIgnore
	public Set<Bid> getBids() {
		return bids;
	}

	public void setBids(Set<Bid> bids) {
		this.bids = bids;
	}

	@JsonIgnore
	public Set<Auction> getAuctions() {
		return auctions;
	}

	public void setAuctions(Set<Auction> auctions) {
		this.auctions = auctions;
	}
	
	//testing..
	@JsonIgnore
	public Set<Item> getItems() {
		return items;
	}

	public void setItems(Set<Item> items) {
		this.items = items;
	}
	//
	
	public String getDevice_token() {
		return device_token;
	}

	public void setDevice_token(String device_token) {
		this.device_token = device_token;
	}

	public void applyUserDTO(UserDTO userDto) {
		this.name = userDto.getName();
		this.email= userDto.getEmail();
		this.picture = userDto.getPicture();
		this.address = userDto.getAddress();
		this.phone = userDto.getPhone();
		this.password = userDto.getPassword();
	}

}
