package sf282015.osa.projectOSA.dto;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;

import sf282015.osa.projectOSA.entity.Auction;
import sf282015.osa.projectOSA.entity.Bid;

public class AuctionDTO {
	
	private long id;
	private float startPrice;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
	private Date startDate;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
	private Date endDate;
	private long item_id;
	private long user_id;
//	private Set<Bid> bids = new HashSet<Bid>();
	private List<Bid> bids = new ArrayList<>();
	@JsonProperty(access=Access.READ_ONLY)
	private boolean over;
	
	public AuctionDTO(){}
	
	public AuctionDTO(Auction auction){
		this.id = auction.getId();
		this.startPrice = auction.getStartPrice();
		this.startDate = auction.getStartDate();
		this.endDate = auction.getEndDate();
		this.item_id = auction.getItem().getId();
		this.user_id = auction.getUser().getId();
		this.bids = auction.getBids();
		this.over = auction.isOver();
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getStartPrice() {
		return startPrice;
	}

	public void setStartPrice(float startPrice) {
		this.startPrice = startPrice;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public long getItem_id() {
		return item_id;
	}

	public void setItem_id(long item_id) {
		this.item_id = item_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
//	
//	public Set<Bid> getBids() {
//		return bids;
//	}
//
//	public void setBids(Set<Bid> bids) {
//		this.bids = bids;
//	}
	

	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}
	

	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}


}
