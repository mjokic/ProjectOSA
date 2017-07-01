package sf282015.osa.projectOSA.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import sf282015.osa.projectOSA.entity.Auction;

public interface AuctionRepository extends CrudRepository<Auction, Long> {

	List<Auction> findByUserId(long userId);
	
	List<Auction> findByOver(boolean over);
	
}
