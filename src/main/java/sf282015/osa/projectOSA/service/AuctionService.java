package sf282015.osa.projectOSA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sf282015.osa.projectOSA.entity.Auction;
import sf282015.osa.projectOSA.repository.AuctionRepository;

@Service
public class AuctionService {
	
	@Autowired
	private AuctionRepository auctionRepository;

	
	public List<Auction> getAuctions(){
		return (List<Auction>) auctionRepository.findAll();
	}
	
	public List<Auction> getActiveAuctions(){
		return auctionRepository.findByOver(false);
	}
	
	public Auction getAuction(long id){
		return auctionRepository.findOne(id);
	}

	public List<Auction> getAuctionsByUserId(long id){
		return auctionRepository.findByUserId(id);
	}
	
	public Auction addAuction(Auction auction){
		return auctionRepository.save(auction);
	}
	
	public Auction editAuction(long id, Auction auction){
		auction.setId(id);
		return auctionRepository.save(auction);
	}
	
	public void deleteAuction(long id){
		auctionRepository.delete(id);
	}
	
}
