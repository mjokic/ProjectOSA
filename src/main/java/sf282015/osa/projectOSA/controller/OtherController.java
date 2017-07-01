package sf282015.osa.projectOSA.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sf282015.osa.projectOSA.SendNotification;
import sf282015.osa.projectOSA.dto.UserDTO;
import sf282015.osa.projectOSA.entity.Auction;
import sf282015.osa.projectOSA.entity.User;
import sf282015.osa.projectOSA.service.AuctionService;
import sf282015.osa.projectOSA.service.BidService;
import sf282015.osa.projectOSA.service.UserService;

@RestController
@RequestMapping(path="")
public class OtherController {

	@Autowired
	private UserService userService;
	
	@Autowired
	private AuctionService auctionService;

	@Autowired
	private BidService bidService;
	
	@RequestMapping(path="/register", method=RequestMethod.POST)
	public User register(@RequestBody User user){
		user.setPicture("default.png");
		user.setRole("USER");
		user.setActive(true); // set false if you add email confirmation
		return userService.addUser(user);
	}
	
	@RequestMapping(path="/me")
	public User getMe(Principal user){
		return userService.getUserByEmail(user.getName());
	}
	
	@RequestMapping(path="/me1")
	public Principal getMe1(Principal user){
		return user;
	}
	
	@RequestMapping(path="/me/auctions")
	public ResponseEntity<List<Auction>> getMyAuctions(Principal currentUser){
		User user = userService.getUserByEmail(currentUser.getName());
		List<Auction> auctions = auctionService.getAuctionsByUserId(user.getId());
		
		return new ResponseEntity<List<Auction>>(auctions, HttpStatus.OK);
		
	}
	
	@RequestMapping(path="/me", method=RequestMethod.PUT)
	public ResponseEntity<User> updateMe(Principal currentUser, @RequestBody UserDTO userDTO){
		User user = userService.getUserByEmail(currentUser.getName());
		
		
		if(user != null){
			
			if(userDTO.getPassword() == null) userDTO.setPassword(user.getPassword());
			if(userDTO.getPicture() == null) userDTO.setPicture(user.getPicture());
			
			user.applyUserDTO(userDTO);
			return new ResponseEntity<User>(userService.editUser(user.getId(), user), HttpStatus.OK);
		}

		return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		
	}
	
	// testing firebase notification
	@RequestMapping(path="/test", method=RequestMethod.POST)
	public ResponseEntity<?> sendNotification(){
		
//		List<String> bidderDeviceTokens = bidService.getBiddersByAuctionId(8);
//		for(String ss : bidderDeviceTokens){
//			System.out.println(ss);
//		}
		List<String> devices = new ArrayList<>();
//		devices.add("caGjFiSPkNI:APA91bHIyNAPd28Gvllzd4eK8hP7DKiuIGUdrrHz-V9kfmBy6CZbIevZwiGPSQyhGxkErbSx8Z7-QvFe_AU8zUCknc6sMMKCy6FFYgpX6m9Eb1w6zrmXKV-GY3qMYj5iQ033igPGkp0c");
//		devices.add("cL5Ov6uSZUs:APA91bF1oEx7qPX5NMgoR5JHsZr_qhFS96DO59wIOuhQoPSSfLZKHtRV00-xpK3Sc-FgDhpcsR8P6P-JDwiTo1Ng5kWlcY-ha5wcWaKy8q7QJ0sqQKakPKuKAJ7S1Ofb1Zddq_3L4Tuu");
		devices.add("eKIMnxd1cXM:APA91bHVVvtir1isRJsdAOfh9ZZJos7AKZ6aMfkJ7k5RxaEx-GYoJA8X-rrYk6TWCy_g87v_zteXAsfVj9_u8Dwr2TEq7PJkmKTJ8YuOSdwl06Ww9ye1QjPgMDG1YhZlrnZ2fF9-mk-Y");
		
		
		try {
			SendNotification.sendNotification("Test", "TROLOLOLO", devices, 1);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
//		try {
//			SendNotification.sendNotification("test", "Test Title", "Moja Poruka");
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		
		return new ResponseEntity<String>("Notification sent!", HttpStatus.OK);
	}
	
}
