package com.cypherx.repository;

import org.springframework.data.repository.CrudRepository;

import com.cypherx.model.RegisterInfo;

public interface UserRegisterInfoRepository extends CrudRepository<RegisterInfo,Integer> {
	
	public Integer countUserModelInfoByEmailIdIgnoreCase(String emailId);
	
	public  RegisterInfo findRegisterInfoByEmailId(String emailId);
	
	public Integer countUserModelInfoByRoleId(Integer roleId);
	


}
