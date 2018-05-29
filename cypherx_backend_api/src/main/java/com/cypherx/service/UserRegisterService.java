package com.cypherx.service;

import java.awt.Image;
import java.io.IOException;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Service;

import com.cypherx.dto.LoginDTO;
import com.cypherx.dto.RegisterDTO;
import com.cypherx.dto.TokenDTO;
import com.google.zxing.WriterException;

@Service
public interface UserRegisterService {
	
	public boolean isAccountExistCheckByEmailId(String emailId);
	
	public boolean isEtherWalletCreated(RegisterDTO registerDTO);
	
	public BigDecimal getEtherBalance(RegisterDTO registerDTO) throws Exception;
	
	public boolean etherTransfer(TokenDTO tokenDTO) throws InterruptedException, Exception;
	
	public boolean saveRegisterUser(RegisterDTO registerDTO, String encryptedPassword) throws WriterException, IOException, Exception;
	
	public LoginDTO isEmailAndPasswordExit(RegisterDTO registerDTO, HttpServletRequest request) throws Exception;
	
	public Integer UsersCount(Integer roleId);
	
	public Image getImage(String id);

	
	
}
