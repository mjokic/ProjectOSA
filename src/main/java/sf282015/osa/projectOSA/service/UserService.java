package sf282015.osa.projectOSA.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import sf282015.osa.projectOSA.entity.User;
import sf282015.osa.projectOSA.repository.UserRepository;

@Service
public class UserService {
	
	@Autowired
	UserRepository userRepository;
	
	public List<User> getUsers(){
		return (List<User>) userRepository.findAll();
	}
	
	public User getUser(long id){
		return userRepository.findOne(id);
	}
	
	public User getUserByEmail(String email){
		return userRepository.findByEmail(email);
	}

	public User addUser(User user){
		return userRepository.save(user);
	}
	
	public User editUser(long id, User user){
		user.setId(id);
		return userRepository.save(user);
	}
	
	public void deleteUser(long id){
		userRepository.delete(id);
	}

	
	public void addDeviceToken(String email, String token) {
		userRepository.updateUserDeviceToken(token, email);
	}
	
}
