package com.cypherx.repository;

import org.springframework.data.repository.CrudRepository;

import com.cypherx.model.BitcoinConfigInfo;

public interface BitcoinConfigInfoRepository extends CrudRepository<BitcoinConfigInfo, Integer> {
	
	public BitcoinConfigInfo findBitcoinConfigByConfigKey(String configKey);

}
