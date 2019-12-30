package com.stg.campaign.email.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.stg.campaign.email.entity.Template;

public interface TemplateRepository extends JpaRepository<Template, Integer> {
	/*
	 * @Modifying(clearAutomatically = true)
	 * 
	 * @Transactional
	 * 
	 * @Query(value =
	 * "update email  set status = ?,chgstatus=? where defect_id = ?", nativeQuery =
	 * true) public int updateEmail(String status,String chgstatus,Long emailId);
	 */
}
