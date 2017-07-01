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

import sf282015.osa.projectOSA.dto.AuctionDTO;
import sf282015.osa.projectOSA.entity.Auction;
import sf282015.osa.projectOSA.entity.Bid;
import sf282015.osa.projectOSA.entity.Item;
import sf282015.osa.projectOSA.entity.User;
import sf282015.osa.projectOSA.service.AuctionService;
import sf282015.osa.projectOSA.service.BidService;
import sf282015.osa.projectOSA.service.ItemService;
import sf282015.osa.projectOSA.service.UserService;

@RestController
@RequestMapping(path="/auctions")
public class AuctionControler {
	
	@Autowired
	private AuctionService auctionService;
	
	@Autowired
	private ItemService itemService;
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private BidService bidService;
	
	
	@RequestMapping(path="")
	public List<Auction> getAuctions(Principal currentUser){
		List<Auction> filteredAuctions = new ArrayList<>(); // finished and sold auctions
		List<Auction> allAuctions = auctionService.getAuctions();
		
		Date currentDateTime = new Date();
		
		User user = userService.getUserByEmail(currentUser.getName());
		if(user.getRole().equals("ADMIN")){
			return allAuctions;
		}else{
			for(Auction a : allAuctions){
//				if(a.getItem().isSold() == false && currentDateTime.before(a.getEndDate())){
				if(a.getItem().isSold() == false && a.isOver() == false 
						&& a.getStartDate().before(currentDateTime)){
					filteredAuctions.add(a);
				}
			}
		}
		
		return filteredAuctions;
	}
	
	@RequestMapping(path="/{id}")
	public ResponseEntity<Auction> getAuction(@PathVariable("id") long id){
		Auction auction = auctionService.getAuction(id);
		if(auction != null) return new ResponseEntity<Auction>(auction, HttpStatus.OK);
		return new ResponseEntity<Auction>(HttpStatus.NOT_FOUND);
	}
	
	@RequestMapping(path="", method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<?> addAuction(@RequestBody AuctionDTO auctionDTO, Principal currentUser){
		Auction auction = new Auction(auctionDTO.getStartPrice(), 
				auctionDTO.getStartDate(), auctionDTO.getEndDate());
		
		User cUser = userService.getUserByEmail(currentUser.getName());
		
		long item_id = auctionDTO.getItem_id();
		Item item = itemService.getItem(item_id);
		
		long user_id = auctionDTO.getUser_id();
		User user = userService.getUser(user_id);
		
		if(item == null || user == null) return new ResponseEntity<Auction>(HttpStatus.BAD_REQUEST);

		// can't make new auction with sold item
		if(item.isSold()) return new ResponseEntity<Auction>(HttpStatus.CONFLICT);
		if(item.isOnAuction()) return new ResponseEntity<String>("Item is already on auction!", HttpStatus.CONFLICT);
		
		// postponing current time for 5 mins
		// because bellow, auction can't be created in past
		Date dateTimeNow = new Date(System.currentTimeMillis() - 300000);
		
		if(auctionDTO.getStartDate().before(dateTimeNow) || // ne moze da se postavi da je aukcija juce
				auctionDTO.getStartDate().after(auctionDTO.getEndDate())) // obavezan start < end date
			return new ResponseEntity<String>("Bad date format!", HttpStatus.FORBIDDEN);
		
		// ako user nije admin
		// ili ako item nije od usera i ako userId nije od currentUser
		if(!cUser.getRole().equals("ADMIN") &&
				!(item.getUser().getId() == user.getId() && user.getId() == cUser.getId())
				){

			return new ResponseEntity<>(HttpStatus.FORBIDDEN);
		}
		
		item.setOnAuction(true);
		auction.setItem(item);
		auction.setUser(user);
		
		return new ResponseEntity<Auction>(auctionService.addAuction(auction), HttpStatus.CREATED);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<?> editAuction(@PathVariable("id") long id, 
			@RequestBody AuctionDTO auctionDTO, Principal currentUser){
		User cUser = userService.getUserByEmail(currentUser.getName());
		
		Auction auction = auctionService.getAuction(id);
		if(auction == null) return new ResponseEntity<Auction>(HttpStatus.NOT_FOUND);
		
		
//		// ako je aukcija vec pocela ne moze se menjati
//		Date dateTimeNow = new Date();
//		boolean after = dateTimeNow.after(auction.getStartDate());
//		if(after) return new ResponseEntity<Auction>(HttpStatus.FORBIDDEN);
//		
//		
//		if(auctionDTO.getStartDate().before(dateTimeNow) || // ne moze da se postavi da je aukcija juce
//				auctionDTO.getStartDate().after(auctionDTO.getEndDate())) // obavezan start < end date
//			return new ResponseEntity<Auction>(HttpStatus.FORBIDDEN);
		
		// postponing current time for 5 mins
				// because bellow, auction can't be created in past
//		Date dateTimeNow = new Date(System.currentTimeMillis() - 300000);
		Date dateTimeNow = new Date(System.currentTimeMillis());
		if(auction.getStartDate().before(dateTimeNow))
			return new ResponseEntity<String>("Auction already started, can't edit!", HttpStatus.FORBIDDEN);
		
		if(auctionDTO.getStartDate().before(dateTimeNow) || // ne moze da se postavi da je aukcija juce
				auctionDTO.getStartDate().after(auctionDTO.getEndDate())) // obavezan start < end date
			return new ResponseEntity<String>("You can't do that!", HttpStatus.FORBIDDEN);
		
		long item_id = auctionDTO.getItem_id();
		Item item = itemService.getItem(item_id);
		
		long user_id = auctionDTO.getUser_id();
		User user = userService.getUser(user_id);
		
		if(item == null || user == null) return new ResponseEntity<Auction>(HttpStatus.BAD_REQUEST);
		
		// ako user nije admin
		// i ako item nije od usera i ako userId nije od currentUser
		if(!cUser.getRole().equals("ADMIN") &&
				!(item.getUser().getId() == user.getId() && user.getId() == cUser.getId())
				){

			return new ResponseEntity<Auction>(HttpStatus.FORBIDDEN);
		}
		
		auction.setUser(user);
		auction.setItem(item);
		auction.setStartPrice(auctionDTO.getStartPrice());
		auction.setStartDate(auctionDTO.getStartDate());
		auction.setEndDate(auctionDTO.getEndDate());
		
		return new ResponseEntity<Auction>(auctionService.editAuction(id, auction), HttpStatus.OK);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteAuction(@PathVariable("id") long id, Principal currentUser){
		
		User cUser = userService.getUserByEmail(currentUser.getName());
		
		Auction auction = auctionService.getAuction(id);
		
		if(auction != null){
			
			if(!cUser.getRole().equals("ADMIN") && auction.getUser().getId() != cUser.getId()){
				return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
			}
			
			Date currentDate = new Date();
			if(auction.getStartDate().before(currentDate) 
					&& auction.getEndDate().after(currentDate)){
				return new 
						ResponseEntity<String>("Auction already started! Can't delete!", HttpStatus.FORBIDDEN);
				
			}else if(auction.isOver()){
				return new 
						ResponseEntity<String>("Auction is already finished! Can't delete!", HttpStatus.FORBIDDEN);
			}
			
			auctionService.deleteAuction(id);
			
			// ako se obrise aukcija sa itemom
			// taj item vise nije na aukciji onAuction='false'
			Item item = auction.getItem();
			item.setOnAuction(false);
			itemService.editItem(item.getId(), item);
			
			return new ResponseEntity<Void>(HttpStatus.OK);
		}else{
			return new ResponseEntity<String>("Auction not found!", HttpStatus.NOT_FOUND);
		}
		
		
	}

	@RequestMapping(path="/{id}/bids")
	public ResponseEntity<?> getBidsOnThisAuction(@PathVariable("id") long id, Principal currentUser){
		Auction auction = auctionService.getAuction(id);
		
		if(auction == null){
			return new ResponseEntity<String>("Auction not found!", HttpStatus.NOT_FOUND);
		}
		
		List<Bid> bids = bidService.getBidsByAuctionId(auction.getId());
		
//		System.out.println("u aukcijama...");
//		for(Bid b : bids){
//			System.out.println(b.getPrice());
//		}
		
		return new ResponseEntity<List<Bid>>(bids, HttpStatus.OK);
		
	}

}
