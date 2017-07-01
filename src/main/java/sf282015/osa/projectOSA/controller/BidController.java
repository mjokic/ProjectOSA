package sf282015.osa.projectOSA.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sf282015.osa.projectOSA.SendNotification;
import sf282015.osa.projectOSA.dto.BidDTO;
import sf282015.osa.projectOSA.entity.Auction;
import sf282015.osa.projectOSA.entity.Bid;
import sf282015.osa.projectOSA.entity.User;
import sf282015.osa.projectOSA.service.AuctionService;
import sf282015.osa.projectOSA.service.BidService;
import sf282015.osa.projectOSA.service.UserService;

@RestController
@RequestMapping(path="/bids")
public class BidController {

	@Autowired
	private BidService bidService;

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuctionService auctionService;
	
	
	@RequestMapping(path="")
	public List<BidDTO> getBids(Principal currentUser){
		List<Bid> bidList = bidService.getBids();
		List<BidDTO> bidDTOList = new ArrayList<>();
		
		User user = userService.getUserByEmail(currentUser.getName());
		
		for (Bid b : bidList) {
			if(user.getRole().equals("ADMIN") || b.getUser().getId() == user.getId()){
				bidDTOList.add(new BidDTO(b));
			}
		}
		
		return bidDTOList;
	}
	
	@RequestMapping(path="/{id}")
	public ResponseEntity<BidDTO> getBid(@PathVariable("id") long id, Principal currentUser){
		Bid bid = bidService.getBid(id);
		User user = userService.getUserByEmail(currentUser.getName());
		
		if(bid != null){
			if(user.getRole().equals("ADMIN") || bid.getUser().getId() == user.getId()){
				return new ResponseEntity<BidDTO>(new BidDTO(bid), HttpStatus.OK);	
			}else{
				return new ResponseEntity<BidDTO>(HttpStatus.FORBIDDEN);
			}
			
		}else{
			return new ResponseEntity<BidDTO>(HttpStatus.NOT_FOUND);
		}
	}
	
	
	@RequestMapping(path="", method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<?> addBid(@RequestBody BidDTO bidDTO, Principal currentUser){
		User cUser = userService.getUserByEmail(currentUser.getName());
		
		Auction auction = auctionService.getAuction(bidDTO.getAuction_id());
		User user = userService.getUser(bidDTO.getUser_id());

		if(user == null || auction == null) return new ResponseEntity<String>("User or auction doesn't exists!",HttpStatus.NOT_FOUND);
		
		if(!cUser.getRole().equals("ADMIN")  // admin can do anything
				&& !(auction.getUser().getId() != user.getId() && cUser.getId() == user.getId())){
			return new ResponseEntity<String>("You're not allowed to do this!",HttpStatus.FORBIDDEN);
		}
		
		Date bidDate = new Date();
		System.out.println(bidDate + "<--datum");
		
		// datum bid-a mora biti izmedju pocetka i kraja aukcije
		if(bidDate.after(auction.getEndDate()) ||
				bidDate.before(auction.getStartDate()))
			return new ResponseEntity<String>("Auction is expired or not even started yet!", HttpStatus.BAD_REQUEST);
		
//		ArrayList<Bid> bids = new ArrayList<>();
//		bids.addAll(auction.getBids());
		
		List<Bid> bids = bidService.getBidsByAuctionId(auction.getId());
		
		System.out.println("FIRST");
		for(Bid b : bids){
			System.out.println(b.getPrice());
		}
		
		User previousHigestBidder = null;
		
		double highestPrice = 0;
		if(bids.size() != 0) {
			Bid highestBid = bids.get(bids.size() - 1);
			highestPrice = highestBid.getPrice();
			
			previousHigestBidder = highestBid.getUser();
			
		}
		System.out.println(highestPrice + "<-- najveca cena");
		
		// ako je novi bid manji od najveceg baci error
		if(bidDTO.getPrice() <= highestPrice) 
			return new ResponseEntity<String>("Price is not the highest one!", HttpStatus.BAD_REQUEST);
		
		Bid bid = new Bid(bidDTO.getPrice(), bidDate);
		bid.setUser(user);
		bid.setAuction(auction);
		
		if(previousHigestBidder != null){
			// send notification that this user is outbidded
			List<String> device = new ArrayList<>();
			device.add(previousHigestBidder.getDevice_token());
			try {
				SendNotification
					.sendNotification("Info", "You've been outbidded!", device, auction.getId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		return new ResponseEntity<>(bidService.addBid(bid), HttpStatus.CREATED);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<Bid> editBid(@PathVariable("id") long id, @RequestBody BidDTO bidDTO){
		
		Bid b = bidService.getBid(id);
		if(b == null) return new ResponseEntity<Bid>(HttpStatus.NOT_FOUND);
		
		User user = userService.getUser(bidDTO.getUser_id());
		Auction auction = auctionService.getAuction(bidDTO.getAuction_id());
		
		Bid bid = new Bid(bidDTO.getPrice(), bidDTO.getDateTime());
		bid.setUser(user);
		bid.setAuction(auction);
		
		return new ResponseEntity<Bid>(bidService.editBid(id, bid), HttpStatus.OK);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteBid(@PathVariable("id") long id){
		Bid bid = bidService.getBid(id);
		if(bid != null){
			bidService.deleteBid(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}else{
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
		
	}
	
}
