package com.cypherx.repository;

import org.springframework.data.repository.CrudRepository;

import com.cypherx.model.QRcode;

public interface QRCodeRepository extends CrudRepository<QRcode, Integer> {

	public QRcode findQRcodeByQrKey(String qrKey);
	
}

