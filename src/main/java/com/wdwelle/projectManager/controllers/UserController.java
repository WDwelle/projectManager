package com.wdwelle.projectManager.controllers;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import com.wdwelle.projectManager.models.LoginUser;
import com.wdwelle.projectManager.models.User;
import com.wdwelle.projectManager.services.UserService;

@Controller
public class UserController {
	@Autowired
	private UserService userService;

	@GetMapping("/")
	public String index(Model model, HttpSession session) {
		if(session.getAttribute("userId") != null) {
			return "redirect:/dashboard";
		}
		model.addAttribute("newUser", new User()); //for register form
		model.addAttribute("newLogin", new LoginUser()); // for login form
		return "index.jsp";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("newUser") User newUser, BindingResult result, 
			Model model, HttpSession session) {
		userService.register(newUser, result);
		if(result.hasErrors()) {
			model.addAttribute("newLogin", new LoginUser()); // only if we have login
			return "index.jsp";
		}
		session.setAttribute("userId", newUser.getId());
		session.setAttribute("firstname", newUser.getFirstname());
		return "redirect:/dashboard";	
	}	
	
	@PostMapping("/login")
	public String login(@Valid @ModelAttribute("newLogin") LoginUser newLogin, BindingResult result, 
			Model model, HttpSession session) {
		User user = userService.login(newLogin, result);
		if(result.hasErrors()) {
			model.addAttribute("newUser", new User()); // only if the page has register form
			return "index.jsp";
		}
		session.setAttribute("userId", user.getId());
		session.setAttribute("firstname", user.getFirstname());
		return "redirect:/dashboard";
	}
	
	@GetMapping("/logout")
	public String logout(HttpSession session) {
		session.invalidate(); // clear session
		return "redirect:/";
	}
}
