package com.cypherx.userutils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;
import org.web3j.utils.Convert;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.apache.commons.lang.StringUtils;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.cypherx.dto.RegisterDTO;
import com.cypherx.dto.TokenDTO;
import com.cypherx.model.ConfigInfo;
import com.cypherx.model.RegisterInfo;
import com.cypherx.repository.ConfigInfoRepository;
import com.cypherx.repository.UserRegisterInfoRepository;
import com.cypherx.service.TokenService;
import com.cypherx.session.SessionCollector;
import com.cypherx.soliditytojava.CYPHERX;

import ch.qos.logback.core.net.SyslogOutputStream;


@Service
public class UserUtils {
	
	static final Logger LOG = LoggerFactory.getLogger(UserUtils.class);
	
	
	@Autowired
	private Environment env;
	
	@Autowired
	private UserRegisterInfoRepository userRegisterInfoRepository;
	
	@Autowired
	private SessionCollector sessionCollector;
	
	
	
	@Autowired
	private TokenService tokenService;
	
	
	@Autowired
	ConfigInfoRepository configInfoRepository;
	
	
	private final Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io"));
	
	  private BigInteger gasPrice = BigInteger.valueOf(100000000000L);
	  private BigInteger gasLimit = BigInteger.valueOf(6500000);
	
	
	
	
	
	static final String regex = "[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$";
	
	public boolean validateRegistration(RegisterDTO registerDTO) {

		if (registerDTO.getUserName() != null && StringUtils.isNotBlank(registerDTO.getUserName())
				&& registerDTO.getEmailId() != null && StringUtils.isNotBlank(registerDTO.getEmailId())
				&& registerDTO.getPassword() != null && StringUtils.isNotBlank(registerDTO.getPassword())
				&& registerDTO.getConfirmPassword() != null && StringUtils.isNotBlank(registerDTO.getConfirmPassword())
				&& registerDTO.getBitcoinWalletPassword()!=null && StringUtils.isNotBlank(registerDTO.getBitcoinWalletPassword())){
			return true;
			}
		return false;
	}
	
	public boolean validateUserName(RegisterDTO registerDTO) {
			
			Pattern pattern = Pattern.compile("[a-zA-Z0-9._-]{4,20}$");
			Matcher matcher = pattern.matcher(registerDTO.getUserName());
			if (matcher.matches()) {
			LOG.info(registerDTO.getUserName());
			return true;
			} 
			 
			return true;
		}
	
	public boolean validateEmail(RegisterDTO registerDTO) {

		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(registerDTO.getEmailId());
		LOG.info(registerDTO.getEmailId() + " : " + matcher.matches());
		if (matcher.matches()) {
			return true;
		}
			return false;
	}
	
	
	public boolean validatePassword(RegisterDTO registerDTO) {

		Pattern pattern = Pattern.compile("^.*(?=.{5,})(?=..*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$");
		Matcher matcher = pattern.matcher(registerDTO.getPassword());
		if (matcher.matches()) {
			return true;
		}
			return false;
	}
	
	public boolean validateConfirmPassword(RegisterDTO registerDTO) {
		if (registerDTO.getPassword().equals(registerDTO.getConfirmPassword())) {
			return true;
		}
		return false;
	}
	
	public boolean validateEtherWalletAddress(TokenDTO tokenDTO) {
		Pattern pattern = Pattern.compile("^0x.{40}$");
		Matcher matcher = pattern.matcher(tokenDTO.getEtherWalletAddress().trim());
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public String getEtherWalletAddress(String fileLocation, String fileName)
			throws FileNotFoundException, IOException, ParseException {

		fileLocation = fileLocation.replace("/", "\\");
		System.out.println("WalletCreated:::" + fileLocation);
		System.out.println("FileName:::" + fileName);

		JSONParser parser = new JSONParser();
		Object object;
		object = parser.parse(new FileReader(fileLocation + "//" + fileName));
		JSONObject jsonObject = (JSONObject) object;
		String address = "0x" + (String) jsonObject.get("address");
		System.out.println("FileName::::::" + fileName);
		System.out.println("Wallet Address :::::::" + address);

		return address;
}
	
public boolean etherBalanceCheck(TokenDTO tokenDTO) throws Exception {
		
		HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
		ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("walletfile");
		String mail = (String) session.getAttribute("emailId");
		RegisterInfo register = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
		String walletAddress;
		if (register != null) {
			String decryptWalletAddress = EncryptDecrypt.decrypt(register.getEtherWalletAddress());
			walletAddress = getEtherWalletAddress(configInfo.getConfigValue(), decryptWalletAddress);

			if (walletAddress == null) {
				return false;
			}
			tokenDTO.setCentralAdmin(walletAddress);
			EthGetBalance ethGetBalance;
			ethGetBalance = web3j.ethGetBalance(tokenDTO.getCentralAdmin(), DefaultBlockParameterName.LATEST)
					.sendAsync().get();
			BigInteger wei = ethGetBalance.getBalance();
			System.out.println("ether bal:::::::::::" + wei);
			BigDecimal amountCheck = Convert.fromWei(wei.toString(), Convert.Unit.ETHER);
			System.out.println("ether bal:::::::::::" + amountCheck);
			if(tokenDTO.getEtherTransferAmount().doubleValue() <amountCheck.doubleValue()) {
				return true;
			}
		}
		return false;
	}


	
	public boolean validateverificationParams(RegisterDTO registerDTO) {
		if (registerDTO.getEmailId() != null && StringUtils.isNotBlank(registerDTO.getEmailId())) {
			return true;
		}
		return false;
	}
	
	public boolean isStatusActive(RegisterDTO registerDTO) throws Exception {
		System.out.println("EmailId : ::::::::::::::::::" + registerDTO.getEmailId());
		String decryptedEmail = EncryptDecrypt.decrypt(registerDTO.getEmailId().replaceAll("\\s", "+"));

		RegisterInfo registerInfoModel = userRegisterInfoRepository.findRegisterInfoByEmailId(decryptedEmail);
		if (registerInfoModel != null) {
			System.out.println("EmailId :::::::::::::: Activation:::::::::" + decryptedEmail);
			if (registerInfoModel.getActivation() == true) {
				return true;
			}
		}
		return false;
	}

	
	public boolean validateEmailLink(RegisterDTO registerDTO) throws Exception {

		String decryptedEmail = EncryptDecrypt.decrypt(registerDTO.getEmailId().replaceAll("\\s", "+"));

		System.out.println("EmailId : ::::::::::::::::::" + decryptedEmail);

		RegisterInfo registerInfoModel = userRegisterInfoRepository.findRegisterInfoByEmailId(decryptedEmail);

		if (registerInfoModel != null) {
			registerInfoModel.setActivation(true);
			userRegisterInfoRepository.save(registerInfoModel);
			return true;
		}

		return false;
	}
	
	
	public boolean validateLoginParam(RegisterDTO registerDTO) {
		if (registerDTO.getEmailId() != null && StringUtils.isNotBlank(registerDTO.getEmailId())
				&& registerDTO.getPassword() != null && StringUtils.isNotBlank(registerDTO.getPassword())) {
			return true;
		}
		return false;
	}
	
	public boolean validateActivation(RegisterDTO registerDTO) {

		System.out.println("Inside Validate Activation");
		System.out.println("validateActivation Email:::::::::"+registerDTO.getEmailId());
		
		RegisterInfo artcoinUserInfoModel = userRegisterInfoRepository
				.findRegisterInfoByEmailId(registerDTO.getEmailId().trim());
		if(artcoinUserInfoModel != null) {
		System.out.println("Activation Status:::::::::::::::" + artcoinUserInfoModel.getActivation());
		if(artcoinUserInfoModel.getActivation() == true) {
			return true;
		}
		}
		return false;
	}
	
	public boolean logoutParam(RegisterDTO registerDTO) {
		if (registerDTO.getSessionId() != null) {
			HttpSession session = SessionCollector.find(registerDTO.getSessionId());
			String email = (String) session.getAttribute("emailId");
			RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(email);
			if (registerInfo != null) {
				HttpSessionEvent event = new HttpSessionEvent(session);
				sessionCollector.sessionDestroyed(event);
				session.invalidate();
				System.out.println("Invalidated");
				return true;
			}
			return false;
		}
		return false;
	}
	
	public boolean validateToAddress(TokenDTO tokenDTO) {
		Pattern pattern = Pattern.compile("^0x.{40}$");
		Matcher matcher = pattern.matcher(tokenDTO.getToAddress().trim());
		if (matcher.matches()) {
			return true;
		} else {
			return false;
		}
	}
	
	public boolean isSameaddress(TokenDTO tokenDTO) {
		HttpSession session = SessionCollector.find(tokenDTO.getSessionId());

		String mail = (String) session.getAttribute("emailId");
		RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
		
		try {
			String decryptedEtherWalletAddress = EncryptDecrypt.decrypt(registerInfo.getEtherWalletAddress());
			String[] fetchAddress = decryptedEtherWalletAddress.split("--");

			String getAddress = fetchAddress[fetchAddress.length - 1].split("\\.")[0];
			String etherWalletAddress = "0x" + getAddress;
			System.out.println("User Ether wallet address:" + tokenDTO.getToAddress());
			System.out.println("User Ether wallet address2:" + etherWalletAddress);

			if (etherWalletAddress.equalsIgnoreCase(tokenDTO.getToAddress())) {
				return true;
			}
		} catch (Exception e) {
			
			e.printStackTrace();
			
		}
		return false;

	}	
	
	public boolean isSamePassword(TokenDTO tokenDTO) throws Exception {
		HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
		String mail = (String) session.getAttribute("emailId");
		RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
		System.out.println(EncryptDecrypt.decrypt(registerInfo.getEtherWalletPassword()));
		if (EncryptDecrypt.decrypt(registerInfo.getEtherWalletPassword()).equals(tokenDTO.getEtherWalletPassword())) {
			return true;
		}else {
		return false;
		}
	}
	
	public boolean isEtherPassword(TokenDTO tokenDTO) throws Exception {
		HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
		String mail = (String) session.getAttribute("emailId");
		RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
		System.out.println(EncryptDecrypt.decrypt(registerInfo.getEtherWalletPassword()));
		if (EncryptDecrypt.decrypt(registerInfo.getEtherWalletPassword()).equals(tokenDTO.getEtherWalletPassword())) {
			return true;
		}else {
		return false;
		}
	}
	
	public boolean isBitcoinPassword(RegisterDTO registerDTO) throws Exception{
		HttpSession session = SessionCollector.find(registerDTO.getSessionId());
		String mail = (String) session.getAttribute("emailId");
		RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
		System.out.println(EncryptDecrypt.decrypt(registerInfo.getBitcoinWalletPassword()));
		if (EncryptDecrypt.decrypt(registerInfo.getBitcoinWalletPassword()).equals(registerDTO.getBitcoinWalletPassword())) {
			return true;
		}else {
		return false;
		}
	}
	
	public boolean tokenAmountValidation(TokenDTO tokenDTO) throws Exception {

		Double tokenBalance = tokenService.tokenBalance(tokenDTO);
		System.out.println(tokenBalance.doubleValue());
		if (tokenDTO.getAmount() != null && tokenDTO.getAmount().toString().trim() != "") {
			double transferAmount = tokenDTO.getAmount().doubleValue();
			System.out.println("Tranfer Amount After: " + transferAmount);
			if (transferAmount <= 0 || transferAmount > tokenBalance) {
				System.out.println("Inside Token Amount Validation");
				return false;
			}
			return true;
		}
		return false;
	}

	 public Double tokenBalanceValidation(TokenDTO tokenDTO) throws Exception {
		  
		  Credentials credentials;
		  HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
		  String mail = (String) session.getAttribute("emailId");
		  RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);		    
		  
		  ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("WalletFile");

		  String WalletAddress = getEtherWalletAddress(configInfo.getConfigValue(),
				  EncryptDecrypt.decrypt(registerInfo.getEtherWalletAddress()));

		  if(WalletAddress == null) {
		   return null;
		  }
		   
		  credentials = WalletUtils.loadCredentials(env.getProperty("credentials.password"), env.getProperty("credentials.address"));
		   
		  CYPHERX Token = CYPHERX.load(env.getProperty("token.address"), web3j, credentials, gasPrice, gasLimit);
		   
		  BigInteger balance = Token.balanceOf(WalletAddress).send();
		  System.out.println("balllll::::::::::"+balance.doubleValue()/100000000);
		  tokenDTO.setTokenBalance(balance.doubleValue()/100000000);
		  
		  if (tokenDTO.getNoOfToken() != null && tokenDTO.getNoOfToken().toString().trim() != "") {
		   
		   Double tokenBalance1 = tokenDTO.getNoOfToken();
		   
		   System.out.println("TokenBalance: " + balance.doubleValue()/100000000);
		   
		   if (tokenBalance1 > 0 && tokenBalance1 <= balance.doubleValue()/100000000) {
		    
		    System.out.println("Outside Token Balance Validation");
		    return balance.doubleValue()/100000000;
		    
		   }
		   return null;
		  }
		  return null;
		 }
	 
	 
	 public boolean validateTokenParam(TokenDTO tokenDTO) {
			if (tokenDTO.getRequestTokens() != null && tokenDTO.getRequestTokens().toString().trim() != "" && tokenDTO.getRequestTokens() != 0) {
				return true;
			} else {
				return false;
			}
		}
	 
	 public boolean validatePasswordPrams(TokenDTO tokenDTO) throws Exception {
			HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
			String mail = (String) session.getAttribute("emailId");
			// String etherWalletPassword = (String)
			// session.getAttribute("etherWalletPassword");
			String etherWalletPassword = tokenDTO.getEtherWalletPassword();
			// String decryptWalletPassword1;
			//
			// decryptWalletPassword1 = EncryptDecrypt.decrypt(etherWalletPassword);

			System.out.println("etherWalletPassword::::::::::::::::::::" + etherWalletPassword);
			RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
			try {
				if (registerInfo != null) {

					String decryptWalletPassword = EncryptDecrypt.decrypt(registerInfo.getEtherWalletPassword());
					System.out.println("decryptWalletPassword:::::::::::::::::::::" + decryptWalletPassword);
					System.out.println("getEtherwalletPassword:::::::::::::::::::" + etherWalletPassword);
					if (decryptWalletPassword.equals(etherWalletPassword)) {
						tokenDTO.setEtherWalletPassword(etherWalletPassword);

						return true;

					}
					return false;

				}
			} catch (Exception e) {

			}
			return false;
		}
	

	
}
	
	
	
	


