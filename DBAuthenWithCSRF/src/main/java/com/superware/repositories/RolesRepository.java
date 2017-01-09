package com.superware.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.superware.domain.Role;

@Repository
public interface RolesRepository extends CrudRepository<Role, Long> {

	public Role findByName(String name);
}
