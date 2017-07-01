package sf282015.osa.projectOSA.repository;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import sf282015.osa.projectOSA.entity.Bid;

public interface BidRepository extends CrudRepository<Bid, Long> {

	List<Bid> findByAuctionId(long auctionId);
	
	@Query("select device_token from User where id in (select distinct user from Bid where auction_id=?1)")
	List<String> getBiddersByAuctionId(long auctionId);
	
}
