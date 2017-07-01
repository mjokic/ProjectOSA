package sf282015.osa.projectOSA.entity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonProperty.Access;


@Entity
public class Auction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	
	@Column(nullable = false)
	private float startPrice;
	
	@Column(nullable = false)
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
	private Date startDate;

	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss", timezone="CET")
	@Column(nullable = false)
	private Date endDate;
	
	@OneToMany(cascade=CascadeType.ALL, fetch=FetchType.LAZY, mappedBy="auction")
//	private Set<Bid> bids = new HashSet<Bid>();
	private List<Bid> bids = new ArrayList<>();
	
	@ManyToOne
	@JoinColumn(name="item_id", referencedColumnName="id", nullable=false)
	private Item item;

	@ManyToOne
	@JoinColumn(name="user_id", referencedColumnName="id", nullable=false)
	private User user;
	
	@Column(nullable = false)
	@JsonProperty(access=Access.READ_ONLY)
	private boolean over;
	
	
	public Auction(){}

	public Auction(float startPrice, Date startDate, Date endDate) {
		this.startPrice = startPrice;
		this.startDate = startDate;
		this.endDate = endDate;
		this.over = false;
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

//	public Set<Bid> getBids() {
//		return bids;
//	}
//
//	public void setBids(Set<Bid> bids) {
//		this.bids = bids;
//	}
	
	

	public Item getItem() {
		return item;
	}

	public void setItem(Item item) {
		this.item = item;
	}
	
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}


	public List<Bid> getBids() {
		return bids;
	}

	public void setBids(List<Bid> bids) {
		this.bids = bids;
	}


	// maybe remove
	public boolean isOver() {
		return over;
	}

	public void setOver(boolean over) {
		this.over = over;
	}
	

	@Override
	public String toString() {
		return "Auction [id=" + id + ", startPrice=" + startPrice + ", startDate=" + startDate + ", endDate=" + endDate
				+ ", over=" + over + "]";
	}

}
