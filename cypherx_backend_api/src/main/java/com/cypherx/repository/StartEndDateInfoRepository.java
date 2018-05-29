package com.cypherx.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Service;

import com.cypherx.model.StartEndDateInfo;


@Service
public interface StartEndDateInfoRepository extends CrudRepository<StartEndDateInfo, Integer> {

	
}
