package br.com.mdd.application.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import br.com.mdd.application.service.SecurityService;
import br.com.mdd.application.service.UserService;
import br.com.mdd.application.validator.UserValidator;
import br.com.mdd.domain.model.User;

@Controller
public class UserController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private SecurityService securityService;
	
	@Autowired
	private UserValidator userValidator;
	
	@RequestMapping(value="/logout", method = RequestMethod.GET)
	public String logoutPage (HttpServletRequest request, HttpServletResponse response) {
	    Authentication auth = SecurityContextHolder.getContext().getAuthentication();
	    if (auth != null){    
	        new SecurityContextLogoutHandler().logout(request, response, auth);
	    }
	    return "redirect:/login?logout";
	}
	
	@RequestMapping(value = "/registration", method = RequestMethod.GET)
	public String registration(Model model) {
		model.addAttribute("user", new User());
		
		return "registration";
	}
	
	@Transactional(readOnly = false)
	@RequestMapping(value = "/registration", method = RequestMethod.POST)
	public String registration(@ModelAttribute("user") User user, BindingResult bindingResult, Model model) {
		userValidator.validate(user, bindingResult);
		
		if (bindingResult.hasErrors()) {
			return "registration";
		}
		
		String password = user.getPassword();
		userService.save(user);
		securityService.autologin(user.getUsername(), password);
		
		return "redirect:/home";
	}
	
	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String login(Model model, String error, String logout) {
		if (error != null)
            model.addAttribute("error", "Your username and password is invalid.");

        if (logout != null)
            model.addAttribute("message", "You have been logged out successfully.");
        
		return "login";
	}

}
