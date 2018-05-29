package com.gfmacaraeg.flipstore.services;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.gfmacaraeg.flipstore.models.Role;
import com.gfmacaraeg.flipstore.models.User;
import com.gfmacaraeg.flipstore.repositories.RoleRepository;
import com.gfmacaraeg.flipstore.repositories.UserRepository;

@Service
public class UserService {
	private UserRepository userRepository;
	private RoleRepository roleRepository;
	private BCryptPasswordEncoder bCrypt;
	
	public UserService(UserRepository userRepository,RoleRepository roleRepository,BCryptPasswordEncoder bCrypt){
		this.userRepository = userRepository;
		this.roleRepository = roleRepository;
		this.bCrypt = bCrypt;
	}
	
	public List<User> all(){return (List<User>) userRepository.findAll();}
	public Optional<User> getById(long id){return userRepository.findById(id);}
	public User findByEmail(String email){return userRepository.findByEmail(email);}
	
	public void create(String[] roles,User user){
		List<Role> userRoles = new ArrayList<>();
		
		for(String role:roles){
			Role getRole = roleRepository.findByName(role);
			if(getRole != null){userRoles.add(getRole);}
		}
				
		user.setPassword(bCrypt.encode(user.getPassword()));
		user.setRoles(userRoles);
		userRepository.save(user);
	}
	public void update(User user){userRepository.save(user);}
	public void destroy(long id){userRepository.deleteById(id);}


	public void saveWithUserRole(User user) {
		user.setPassword(bCrypt.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_USER")));
        userRepository.save(user);
    }
     
    public void saveUserWithAdminRole(User user) {
    	user.setPassword(bCrypt.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_ADMIN")));
        userRepository.save(user);
    }   
    
    public void saveUserWithSuperAdminRole(User user) {
    	user.setPassword(bCrypt.encode(user.getPassword()));
        user.setRoles(Arrays.asList(roleRepository.findByName("ROLE_ADMIN"), roleRepository.findByName("ROLE_SUPERADMIN")));
        userRepository.save(user);
    } 
}
