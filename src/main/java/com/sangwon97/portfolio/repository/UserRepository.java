package com.sangwon97.portfolio.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sangwon97.portfolio.domain.entity.User;

public interface UserRepository extends JpaRepository<User,String>{
   
}
