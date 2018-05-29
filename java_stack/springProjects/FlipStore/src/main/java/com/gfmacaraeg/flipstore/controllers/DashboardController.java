package com.gfmacaraeg.flipstore.controllers;

import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.gfmacaraeg.flipstore.models.Role;
import com.gfmacaraeg.flipstore.models.User;
import com.gfmacaraeg.flipstore.services.RoleService;
import com.gfmacaraeg.flipstore.services.UserService;
import com.gfmacaraeg.flipstore.validations.UserValidator;

@Controller
public class DashboardController {
	private UserService userService;
	@Autowired
	private RoleService roleService;
	private UserValidator userValidator;
	
	public DashboardController(UserService userService,RoleService roleService,UserValidator userValidator){
		this.userService = userService;
		this.roleService = roleService;
		this.userValidator = userValidator;
	}
	@RequestMapping(value={"/login","/register"})
	public String login(Model model,@RequestParam(value="error",required=false) String error,@RequestParam(value="logout",required=false) String logout){
		if(error != null){model.addAttribute("errorMessage","Invalid Credentials.");}
		if(logout != null){model.addAttribute("logoutMessage","Logout Successful");}
		
		model.addAttribute("user",new User());
		return "login_register";
	}
	
	@PostMapping("/register")
	public String register(@Valid @ModelAttribute("user") User user,BindingResult res,Model model){
		
		userValidator.validate(user,res);
		System.out.println("Im heresdafasdfasdfasdfas");
		if(res.hasErrors()){
			System.out.println("I have errors");
			return "login_register";}
		else if(roleService.findSuperAdmin().isEmpty()) {
			System.out.println("Im in elseif");
//			userService.saveUserWithSuperAdminRole(user);
			userService.create(new String[]{"ROLE_USER","ROLE_ADMIN"}, user);
		}else{
//			userService.saveWithUserRole(user);
			userService.create(new String[]{"ROLE_USER"}, user);
		}
//		else if(roleService.findByName("ROLE_SUPERADMIN").getUsers().isEmpty()){ // Less than one admin? Make them admin, else user.
//			System.out.println("Im in elseif");
//			userService.saveUserWithSuperAdminRole(user);
//		}else{
//			userService.saveWithUserRole(user);
//		}
		return "redirect:/login";
	}
	@RequestMapping("/superadmin")
	public String superadmin(Principal principal,Model model){
		model.addAttribute("user",userService.findByEmail(principal.getName()));
		return "superadmin";
	}
	
	@RequestMapping("/admin")
	public String admin(Principal principal,Model model){		
		model.addAttribute("user",userService.findByEmail(principal.getName()));
		model.addAttribute("users",userService.all());
		return "admin";
	}
	
	@RequestMapping("/admin/delete/{id}")
	public String delete(@PathVariable("id") long id){
		userService.destroy(id);
		return "redirect:/admin";
	}
	
	@RequestMapping("/admin/promote/{id}")
	public String promote(@PathVariable("id") long id){
		Optional<User> checkUser = userService.getById(id);
		User user = new User();
		if(checkUser.isPresent()) {
			user = checkUser.get();
		}else {
			//happens when user is not found
		}
		List<Role> userRoles = user.getRoles();
		userRoles.add(roleService.findByName("ROLE_ADMIN"));
		userService.update(user);
		
		return "redirect:/admin";
	}
	
	@RequestMapping(value={"/","/dashboard"})
	public String dashboard(Principal principal,Model model){
		User user = userService.findByEmail(principal.getName());
		model.addAttribute("user",user);
		user.setUpdatedAt(new Date());
		userService.update(user);
		
		if(user.isSuperAdmin()){
			return "redirect:/superadmin";
		}else if(user.isAdmin()){
			return "redirect:/admin";
		}else{
			return "dashboard";
		}
	}
}
