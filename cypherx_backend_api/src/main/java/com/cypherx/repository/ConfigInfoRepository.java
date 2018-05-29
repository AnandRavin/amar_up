package com.cypherx.repository;

import org.springframework.data.repository.CrudRepository;

import com.cypherx.model.ConfigInfo;

public interface ConfigInfoRepository extends CrudRepository<ConfigInfo,Integer> {
	
	public ConfigInfo findConfigInfoByConfigKey(String string);

}
