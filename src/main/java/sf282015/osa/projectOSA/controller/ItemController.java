package sf282015.osa.projectOSA.controller;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import sf282015.osa.projectOSA.dto.ItemDTO;
import sf282015.osa.projectOSA.entity.Item;
import sf282015.osa.projectOSA.entity.User;
import sf282015.osa.projectOSA.service.ItemService;
import sf282015.osa.projectOSA.service.UserService;

@RestController
@RequestMapping(path="/items")
public class ItemController {

	@Autowired
	private ItemService itemService;
	
	@Autowired
	private UserService userService;

	
	@RequestMapping(path="")
	public List<ItemDTO> getItems(Principal currentUser){
		User user = userService.getUserByEmail(currentUser.getName());
		
		List<Item> items = itemService.getItems();
		List<ItemDTO> itemsDTO = new ArrayList<>();
		
		for (Item item : items) {
			if(user.getRole().equals("ADMIN") || item.getUser().getId() == user.getId()){
				itemsDTO.add(new ItemDTO(item));
			}
		}
		
		return itemsDTO;
	}
	
	@RequestMapping(path="/{id}")
	public ResponseEntity<ItemDTO> getItem(@PathVariable("id") long id, Principal currentUser){
		User user = userService.getUserByEmail(currentUser.getName());
		
		Item item = itemService.getItem(id);
		if(item != null){
			if(!user.getRole().equals("ADMIN") && user.getId() != item.getUser().getId()){
				return new ResponseEntity<ItemDTO>(HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<ItemDTO>(new ItemDTO(item), HttpStatus.OK);
		}else{
			return new ResponseEntity<ItemDTO>(HttpStatus.NOT_FOUND);
		}
	}
	
	@RequestMapping(path="", method=RequestMethod.POST, consumes="application/json")
	public ResponseEntity<?> addBid(@RequestBody ItemDTO itemDTO, Principal pUser){
		Item item = new Item();
		item.applyItemDTO(itemDTO);
		
		if(item.getPicture() == null) item.setPicture("default.png");
		
		User cUser = userService.getUserByEmail(pUser.getName());
		User user = userService.getUser(itemDTO.getUserId());
		
		if(!user.getEmail().equals(pUser.getName()) && !cUser.getRole().equals("ADMIN")){
			return new ResponseEntity<String>("You're not allowed to do this!", HttpStatus.FORBIDDEN);
		}
		
		item.setUser(user);
		return new ResponseEntity<Item>(itemService.addItem(item), HttpStatus.CREATED);
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.PUT, consumes="application/json")
	public ResponseEntity<Item> editItem(@PathVariable("id") long id, @RequestBody ItemDTO itemDTO, Principal currentUser){
		User user = userService.getUserByEmail(currentUser.getName());
		Item item = itemService.getItem(id);
		item.applyItemDTO(itemDTO);
		item.setUser(userService.getUser(itemDTO.getUserId()));
		
		if(item != null && user != null){
			if(!user.getRole().equals("ADMIN") && item.getUser().getId() != user.getId()){
				return new ResponseEntity<Item>(HttpStatus.FORBIDDEN);
			}
			return new ResponseEntity<Item>(itemService.editItem(id, item), HttpStatus.OK);	
		}else{
			return new ResponseEntity<Item>(HttpStatus.NOT_FOUND);
		}
		
		
	}
	
	@RequestMapping(path="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<?> deleteItem(@PathVariable("id") long id, Principal currentUser){
		User user = userService.getUserByEmail(currentUser.getName());
		
		Item i = itemService.getItem(id);
		
		if(i != null){
			if(!user.getRole().equals("ADMIN") && i.getUser().getId() != user.getId()){
				return new ResponseEntity<Void>(HttpStatus.FORBIDDEN);
			}else if(i.getAuctions().size() != 0){
				return new ResponseEntity<String>("This item is on auction, can't delete it!", HttpStatus.BAD_REQUEST);
			}
			
			itemService.deleteItem(id);
			return new ResponseEntity<Void>(HttpStatus.OK);
		}else{
			return new ResponseEntity<Void>(HttpStatus.NOT_FOUND);
		}
	}
	
}
