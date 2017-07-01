package sf282015.osa.projectOSA.controller;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import sf282015.osa.projectOSA.entity.User;
import sf282015.osa.projectOSA.service.UserService;
import sf282015.osa.projectOSA.service.storage.StorageService;

@Controller
@RequestMapping(path="")
public class ImageUploadController {

	@Autowired
	private StorageService storageService;
	
	@Autowired
	private UserService userService;
	

	@RequestMapping(path="/upload", method=RequestMethod.POST)
	private ResponseEntity<String> userAvatarUpload(@RequestParam("file") MultipartFile file, 
			Principal currentUser){
		
		User user = userService.getUserByEmail(currentUser.getName());
		
		if(user == null) return new ResponseEntity<String>("You're not logged in!", HttpStatus.FORBIDDEN);
		
		System.out.println(file.getOriginalFilename() + "<-- Orig");
		
//		String fileName = storageService.storeItemPicture(file);
		String fileName = storageService.storeUserAvatar(file);
		if(fileName == null) 
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);
		
		user.setPicture(fileName);
		userService.editUser(user.getId(), user);
		
		return new ResponseEntity<String>(fileName, HttpStatus.OK);
	}
	

	@RequestMapping(path="/upload1", method=RequestMethod.POST)
	private ResponseEntity<String> itemImagesUpload(@RequestParam("file") MultipartFile file, Principal currentUser){
		
		User user = userService.getUserByEmail(currentUser.getName());
		if(user == null) return new ResponseEntity<String>("You're not logged in!", HttpStatus.FORBIDDEN);

		System.out.println(file.getOriginalFilename() + "<-- Orig");
		
		String fileName = storageService.storeItemPicture(file);
//		String fileName = storageService.storeUserAvatar(file);
		if(fileName == null) 
			return new ResponseEntity<String>("Something went wrong!", HttpStatus.BAD_REQUEST);

		
		return new ResponseEntity<String>(fileName, HttpStatus.OK);
	}

}
