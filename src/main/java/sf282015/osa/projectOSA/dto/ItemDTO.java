package sf282015.osa.projectOSA.dto;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import sf282015.osa.projectOSA.entity.Auction;
import sf282015.osa.projectOSA.entity.Item;
import sf282015.osa.projectOSA.entity.User;

public class ItemDTO {

	private long id;
	private String name;
	private String description;
	private String picture;
	private boolean sold;
	private Set<Long> auctionIDs = new HashSet<Long>();
	private long userId = 0;
	@JsonProperty(access=Access.READ_ONLY)
	private boolean onAuction;

	
	
	public ItemDTO(){}
	
	public ItemDTO(Item item){
		this.id = item.getId();
		this.name = item.getName();
		this.description = item.getDescription();
		this.picture = item.getPicture();
		this.sold = item.isSold();
		this.onAuction = item.isOnAuction();

		Set<Auction> auctions = item.getAuctions();
	
		for(Auction auction : auctions){
			this.auctionIDs.add(auction.getId());
		}
		
		User user = item.getUser();
		if(user != null){
			userId = user.getId();
		}
		
		
	}
	

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

	public Set<Long> getAuctionIDs() {
		return auctionIDs;
	}

	public void setAuctionIDs(Set<Long> auctions) {
		this.auctionIDs = auctions;
	}

	public long getUserId() {
		return userId;
	}

	public void setUserId(long userId) {
		this.userId = userId;
	}

	// maybe remove...
	public boolean isOnAuction() {
		return onAuction;
	}

	public void setOnAuction(boolean onAuction) {
		this.onAuction = onAuction;
	}
	//
	
	
	
}
