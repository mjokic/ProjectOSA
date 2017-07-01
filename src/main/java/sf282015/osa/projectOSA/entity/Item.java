package sf282015.osa.projectOSA.entity;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import sf282015.osa.projectOSA.dto.ItemDTO;


@Entity
public class Item {


	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private String name;
	
	@Column(nullable = false)
	private String description;
	
	@Column(nullable = false)
	private String picture;
	
	@Column(nullable = false)
	private boolean sold;
	
	@OneToMany(mappedBy="item")
	private Set<Auction> auctions;

	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id", nullable=false)
	private User user;
	
	@JsonProperty(access=Access.READ_ONLY)
	@Column(nullable=false)
	private boolean onAuction;
	
	
	public Item(){}

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

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public boolean isSold() {
		return sold;
	}

	public void setSold(boolean sold) {
		this.sold = sold;
	}
	

	@JsonIgnore
	public Set<Auction> getAuctions() {
		return auctions;
	}

	public void setAuctions(Set<Auction> auctions) {
		this.auctions = auctions;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
	
	
	// maybe remove
	public boolean isOnAuction() {
		return onAuction;
	}

	public void setOnAuction(boolean onAuction) {
		this.onAuction = onAuction;
	}
	//

	public void applyItemDTO(ItemDTO itemDTO){
		this.name = itemDTO.getName();
		this.description = itemDTO.getDescription();
		this.picture = itemDTO.getPicture();
		this.sold = itemDTO.isSold();
	}
	
}
