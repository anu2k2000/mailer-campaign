package com.stg.campaignmgmt.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stg.campaignmgmt.user.entity.User;

public interface UserRepository extends JpaRepository<User, Integer> {
	User findByUserName(String userName);
}
