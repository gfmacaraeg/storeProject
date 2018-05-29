package com.gfmacaraeg.flipstore.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gfmacaraeg.flipstore.models.User;

@Repository
public interface UserRepository extends CrudRepository<User,Long>{
	public User findByEmail(String email);
	
}
