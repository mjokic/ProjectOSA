package sf282015.osa.projectOSA.controller;

import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sf282015.osa.projectOSA.entity.Auction;
import sf282015.osa.projectOSA.entity.Bid;
import sf282015.osa.projectOSA.entity.User;
import sf282015.osa.projectOSA.service.UserService;

@RestController
@RequestMapping(path="/users")
public class UserController {

	@Autowired
	private UserService userService;
	

	@RequestMapping(path="")
	public List<User> getUsers(HttpServletRequest request){
		return userService.getUsers();
	}
	
	@RequestMapping(path="/{id}")
	public ResponseEntity<User> getUser(@PathVariable("id") long id){
		User user = userService.getUser(id);
		if(user != null){
			return new ResponseEntity<User>(user, HttpStatus.OK);
		}else{
			return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(path="", method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<User> addUser(@RequestBody User user){
		if(user.getPicture() == null) user.setPicture("default.png");
		return new ResponseEntity<User>(userService.addUser(user), HttpStatus.CREATED);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<User> updateUser(@PathVariable("id") long id, @RequestBody User user){
		User u = userService.getUser(id);

		// if you don't send password it will use old one
		if(user.getPassword() == null) user.setPassword(u.getPassword());
		
		if(user.getPicture() == null) user.setPicture(u.getPicture());
		
		if(u == null) return new ResponseEntity<User>(HttpStatus.NOT_FOUND);
		
		return new ResponseEntity<User>(userService.editUser(id, user), HttpStatus.OK);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> deleteUser(@PathVariable("id") long id){
		User user = userService.getUser(id);
		
		if(user != null){
			userService.deleteUser(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}else{
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(path="/{uId}/bids")
	public Set<Bid> getBids(@PathVariable("uId") long user_id){
		User user = userService.getUser(user_id);
		return user.getBids();
	}
	
	@RequestMapping(path="/{uId}/auctions")
	public Set<Auction> getAuctions(@PathVariable("uId") long user_id){
		User user = userService.getUser(user_id);
		return user.getAuctions();
	}
	
	
	
}
