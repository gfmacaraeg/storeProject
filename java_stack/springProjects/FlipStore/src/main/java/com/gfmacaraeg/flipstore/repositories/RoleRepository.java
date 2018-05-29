package com.gfmacaraeg.flipstore.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.gfmacaraeg.flipstore.models.Role;

@Repository
public interface RoleRepository extends CrudRepository<Role,Long>{
	@Query(value="SELECT * from ROLE WHERE name = 'ROLE_SUPERADMIN'", nativeQuery=true)
	List<Role> findSuperAdmin();
	
	public Role findByName(String name);
	public List<Role> findAll();
	
	
}
