package sf282015.osa.projectOSA;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import sf282015.osa.projectOSA.entity.Auction;
import sf282015.osa.projectOSA.entity.Bid;
import sf282015.osa.projectOSA.entity.Item;
import sf282015.osa.projectOSA.entity.User;
import sf282015.osa.projectOSA.service.AuctionService;
import sf282015.osa.projectOSA.service.BidService;
import sf282015.osa.projectOSA.service.ItemService;
import sf282015.osa.projectOSA.service.UserService;

@Component
public class ScheduledTasks {
	
	@Autowired
	AuctionService auctionService;
	
	@Autowired
	ItemService itemService;
	
	@Autowired
	BidService bidService;
	
	@Autowired
	UserService userService;
	
	@Scheduled(fixedRate=60000)
	public void getTime() throws Exception{
		Date currentDateTime = new Date();
		
		
		List<Auction> auctions = auctionService.getActiveAuctions();
		
		for(Auction auction : auctions){
			List<Bid> bids = new ArrayList<>();
			
			long auctionId = auction.getId();
			
			if(auction.getEndDate().before(currentDateTime)){
				auction.setOver(true);
				bids = bidService.getBidsByAuctionId(auction.getId());
				
				User owner = auction.getUser();
				System.out.println("Owner id: " + owner.getId());
				
				if(bids.size() > 0){
					Item item = auction.getItem();
					item.setSold(true);
					itemService.editItem(item.getId(), item);
					
					User winner = bids.get(bids.size() - 1).getUser();
					System.out.println("Winner id: " + winner.getId());
					
					String message = "Item sold!";
					
					// send owner notification that item is sold
					List<String> devices = new ArrayList<>();
					devices.add(owner.getDevice_token());
					SendNotification
						.sendNotification("Your auction has finished!", message, devices, auctionId);
					
					
					// send others notification that they didn't win
					List<String> others = bidService.getBiddersByAuctionId(auction.getId());
					others.remove(winner.getDevice_token()); // removing winner from others
					SendNotification
						.sendNotification("You didn't win!", "Better luck next time!", others, auctionId);

					
					// send notification to winner that he won
					List<String> device = new ArrayList<>();
					device.add(winner.getDevice_token());
					SendNotification
						.sendNotification("You've won!", "You've won!", device, auctionId);
					
				}else{
					Item item = auction.getItem();
					item.setOnAuction(false);
					itemService.editItem(item.getId(), item);
					
					// send notification to owner that auction is over
					// and item didn't sell
					List<String> ownerDevice = new ArrayList<>();
					ownerDevice.add(owner.getDevice_token());
					
					SendNotification
						.sendNotification("Your auction has finished!", 
								"You haven't sold your item", ownerDevice, auctionId);
				}
				
				auctionService.editAuction(auction.getId(), auction);
				
//				// send notification to everyone that auction is over
//				List<String> bidderDeviceTokens = bidService.getBiddersByAuctionId(auction.getId());
//				bidderDeviceTokens.add(owner.getDevice_token());
//				if(!bidderDeviceTokens.isEmpty()){
//					SendNotification
//					.sendNotification("Auction is finished!", "Auction is finished!", bidderDeviceTokens);
//				}
			}
			
		}
		
		System.out.println("CHECKED!");
		
		
	}

}
