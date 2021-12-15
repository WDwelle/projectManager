package com.wdwelle.projectManager.services;

import java.util.Optional;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.BindingResult;

import com.wdwelle.projectManager.models.LoginUser;
import com.wdwelle.projectManager.models.User;
import com.wdwelle.projectManager.repositories.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository userRepo;
	
	public User register(User newUser, BindingResult result) {
		if(userRepo.findByEmail(newUser.getEmail()).isPresent()) {
			result.rejectValue("email", "Unique", "This email is already in use!");
		}
		if(!newUser.getPassword().equals(newUser.getConfirm())) { 
			result.rejectValue("confirm", "Matches", "The confirmed password does not match.");
		}
		if(result.hasErrors()) { 
			return null;
		}else {
			String hashed = BCrypt.hashpw(newUser.getPassword(), BCrypt.gensalt());
			newUser.setPassword(hashed);
			return userRepo.save(newUser);
		}
	}
	
		public User login(LoginUser newLogin, BindingResult result) {
			if(result.hasErrors()) {
				return null;
			}
			
			Optional<User> potentialUser = userRepo.findByEmail(newLogin.getEmail());
			if(!potentialUser.isPresent()){
				result.rejectValue("email", "Unqiue", "Unknown Email");
				return null;
			}
			User user = potentialUser.get();
			if(!BCrypt.checkpw(newLogin.getPassword(), user.getPassword())) {
				result.rejectValue("password", "Matches", "Invalid password.");
			}
			if(result.hasErrors()) {
				return null;
			}else {
				return user;
			}	
		}	
		
		public User getUserById(Long id) {
			Optional<User> optionalUser =userRepo.findById(id);
			if(optionalUser.isPresent()) {
				return optionalUser.get();
			}else {
				return null;
			}
		}
}
