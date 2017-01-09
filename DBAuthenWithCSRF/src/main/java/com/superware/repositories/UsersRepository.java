package com.superware.repositories;

import org.springframework.stereotype.Repository;

import com.superware.domain.User;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
@Repository
public interface UsersRepository extends PagingAndSortingRepository<User, Long>{

	//public Page<User> findByNameIgnoreCase(String name, Pageable page);
	
	public Page<User> findAll(Pageable page);
	
	public User findByName(String name);
}
