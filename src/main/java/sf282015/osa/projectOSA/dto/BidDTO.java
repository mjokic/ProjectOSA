package sf282015.osa.projectOSA.dto;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;

import sf282015.osa.projectOSA.entity.Bid;

public class BidDTO {

	private long id;
	private float price;
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
	private Date dateTime;
	private long auction_id;
	private long user_id;
	
	public BidDTO() {}
	
	public BidDTO(Bid bid){
		this.id = bid.getId();
		this.price = bid.getPrice();
		this.dateTime = bid.getDateTime();
		this.auction_id = bid.getAuction().getId();
		this.user_id = bid.getUser().getId();
	}
	

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public float getPrice() {
		return price;
	}

	public void setPrice(float price) {
		this.price = price;
	}

	public Date getDateTime() {
		return dateTime;
	}

	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	public long getAuction_id() {
		return auction_id;
	}

	public void setAuction_id(long auction_id) {
		this.auction_id = auction_id;
	}

	public long getUser_id() {
		return user_id;
	}

	public void setUser_id(long user_id) {
		this.user_id = user_id;
	}
	
}
