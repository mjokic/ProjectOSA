package sf282015.osa.projectOSA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sf282015.osa.projectOSA.entity.Bid;
import sf282015.osa.projectOSA.repository.BidRepository;

@Service
public class BidService {
	
	@Autowired
	private BidRepository bidRepository;

	
	public List<Bid> getBids(){
		return (List<Bid>) bidRepository.findAll();
	}
	
	public List<Bid> getBidsByAuctionId(long auctionId){
		return bidRepository.findByAuctionId(auctionId);
	}
	
	public List<String> getBiddersByAuctionId(long auctionId){
		return bidRepository.getBiddersByAuctionId(auctionId);
	}
	
	
	public Bid getBid(long id){
		return bidRepository.findOne(id);
	}
	
	public Bid addBid(Bid bid){
		return bidRepository.save(bid);
	}
	
	public Bid editBid(long id, Bid bid){
		bid.setId(id);
		return bidRepository.save(bid);
	}
	
	public void deleteBid(long id){
		bidRepository.delete(id);
	}
	
}
