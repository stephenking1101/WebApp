package com.superware.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.superware.domain.Menu;

@Repository
public interface MenuRepository extends CrudRepository<Menu, Long> {

}
