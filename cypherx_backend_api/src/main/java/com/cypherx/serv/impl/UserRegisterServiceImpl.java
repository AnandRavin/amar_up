package com.cypherx.serv.impl;

import java.awt.Image;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

import com.cypherx.dto.LoginDTO;
import com.cypherx.dto.RegisterDTO;
import com.cypherx.dto.TokenDTO;
import com.cypherx.model.ConfigInfo;
import com.cypherx.model.QRcode;
import com.cypherx.model.RegisterInfo;
import com.cypherx.model.TransactionInfo;

import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;

import com.cypherx.repository.ConfigInfoRepository;
import com.cypherx.repository.QRCodeRepository;
import com.cypherx.repository.TransactionInfoRepository;
import com.cypherx.repository.UserRegisterInfoRepository;
import com.cypherx.service.UserRegisterService;
import com.cypherx.session.SessionCollector;
import com.cypherx.userutils.BitcoinUtils;
import com.cypherx.userutils.EncryptDecrypt;
import com.cypherx.userutils.QR_Code;
import com.cypherx.userutils.UserUtils;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


@Service
public class UserRegisterServiceImpl implements UserRegisterService
{
	
	private final Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io"));
	
	static final Logger LOG = LoggerFactory.getLogger(UserRegisterServiceImpl.class);
	
	@Autowired
	private Environment env;
	
	@Autowired
	UserRegisterService userRegisterService;
	
	@Autowired
	UserRegisterInfoRepository userRegisterInfoRepository;
	
	@Autowired
	TransactionInfoRepository transactionInfoRepository;
	
	@Autowired
	ConfigInfoRepository configInfoRepository;
	
	@Autowired
	private QRCodeRepository QRCodeRepository;
	
	@Autowired
	SessionCollector sessionCollector;
	
	@Autowired
	UserUtils userUtils;
	
	@Autowired
	BitcoinUtils bitcoinUtils;

	@Override
	public boolean isAccountExistCheckByEmailId(String emailId) {
		// TODO Auto-generated method stub
		Integer isEmailExit = userRegisterInfoRepository.countUserModelInfoByEmailIdIgnoreCase(emailId);
		if (isEmailExit > 0) {
			return true;
		}
		return false;
	}

	@Override
	public boolean isEtherWalletCreated(RegisterDTO registerDTO) {
		// TODO Auto-generated method stub
		ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("WalletFile");
		System.out.println("configInfo.getvalue::::::::::::::" + configInfo.getConfigValue());

		try {
			String fileName = WalletUtils.generateNewWalletFile(registerDTO.getEtherWalletPassword(),
					new File(configInfo.getConfigValue()), false);

			System.out.println("fileName:::::::::::::" + fileName);

			if (fileName != null) {
				String encryptedEtherWalletAddress = EncryptDecrypt.encrypt(fileName);
				String encryptedEtherWalletPassword = EncryptDecrypt.encrypt(registerDTO.getEtherWalletPassword());
				registerDTO.setEtherWalletAddress(encryptedEtherWalletAddress);
				registerDTO.setEtherWalletPassword(encryptedEtherWalletPassword);
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		
		return false;
	}
	
	
	@Override
	public boolean saveRegisterUser(RegisterDTO registerDTO, String encryptedPassword)
			throws WriterException, IOException, Exception {
		// TODO Auto-generated method stub
		RegisterInfo registerInfo = new RegisterInfo();
		registerInfo.setUserName(registerDTO.getUserName().trim());
		registerInfo.setPassword(encryptedPassword);
		registerInfo.setRoleId(1);
		registerInfo.setEmailId(registerDTO.getEmailId());
		registerInfo.setEtherWalletAddress(registerDTO.getEtherWalletAddress());
		registerInfo.setEtherWalletPassword(registerDTO.getEtherWalletPassword());
		registerInfo.setBitcoinWalletAddress("");
		registerInfo.setBitcoinWalletPassword(EncryptDecrypt.encrypt(registerDTO.getBitcoinWalletPassword()));
		registerInfo.setCreatedDate(new Date());
		registerInfo.setActivation(false);
		userRegisterInfoRepository.save(registerInfo);
		int id = registerInfo.getId();
		String dynamicQRFolder = Integer.toString(id);
		QRcode qrcode = QRCodeRepository.findQRcodeByQrKey("QRKey");
		String qrFileLocation = null;
		if (qrcode != null) {
			qrFileLocation = qrcode.getQrcodeValue();
			File createfolder = new File(qrFileLocation.concat(dynamicQRFolder));
			if (!createfolder.exists()) {
				createfolder.mkdir();
				qrFileLocation = createfolder.getPath().replace("//", "/");
				qrFileLocation = qrFileLocation.concat("/");
			}
		}

		String DecryptedWalletAddress = EncryptDecrypt.decrypt(registerDTO.getEtherWalletAddress());
		System.out.println("Wallet Address ::::::::::::::::::::: Encryption Details::::::::::::::::"
				+ registerDTO.getEtherWalletAddress());
		ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("WalletFile");

		String WalletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(), DecryptedWalletAddress);
		System.out.println("Wallet Address ::::::::::::::::::::: Encryption Details::::::::::::::::" + WalletAddress);
		String qrCodeData = WalletAddress;
		String filePath = qrFileLocation + id + ".png";
		String charset = "UTF-8"; // or "ISO-8859-1"
		Map hintMap = new HashMap();
		hintMap.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.L);

		QR_Code.createQRCode(qrCodeData, filePath, charset, hintMap, 200, 200);

		System.out.println("QR Code image created successfully!");

		userRegisterInfoRepository.save(registerInfo);

		if (registerInfo != null) {
			System.out.println(registerInfo.getId());

			String bitcoinWallet = bitcoinUtils.createBitcoinWallet(registerInfo);
			System.out.println("BitcoinWallet" + bitcoinWallet);
			String EncryptedBitcoinAddress=EncryptDecrypt.encrypt(bitcoinWallet);
			registerInfo.setBitcoinWalletAddress(EncryptedBitcoinAddress);
			userRegisterInfoRepository.save(registerInfo);

			if (bitcoinWallet == null) {

				userRegisterInfoRepository.delete(registerInfo);
			} 
			else 
			return true;
		
		}
		return false; 
		
	}

	@Override
	public LoginDTO isEmailAndPasswordExit(RegisterDTO registerDTO,HttpServletRequest request) throws Exception {
		// TODO Auto-generated method stub
		LoginDTO responseDTO = new LoginDTO();
		RegisterInfo registerInfoModel = userRegisterInfoRepository
				.findRegisterInfoByEmailId(registerDTO.getEmailId().trim());

		ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("WalletFile");
		if (registerInfoModel != null) {
			if (registerInfoModel.getRoleId() == 2) {
				String password = registerInfoModel.getPassword();
				System.out.println("Password::::::::::::" + password);

				String decryptPassword = EncryptDecrypt.decrypt(password);
				System.out.println("decryptPassword:::::::::::" + decryptPassword);

				QRcode qrCode = QRCodeRepository.findQRcodeByQrKey("QRKey");
				String qrCodePath = qrCode.getQrcodeValue();
				qrCodePath = qrCodePath + registerInfoModel.getId() + "//" + registerInfoModel.getId() + ".png";

				if (registerDTO.getPassword().equals(decryptPassword)) {
					// artcoinUserInfoModel.setStatus(true);
					responseDTO.setUserName(registerInfoModel.getUserName().trim());
					responseDTO.setEmailId(registerInfoModel.getEmailId().trim());

					String decryptEtherWalletAddress = EncryptDecrypt
							.decrypt(registerInfoModel.getEtherWalletAddress());
					System.out.println("decryptWalletAddress::::::::" + decryptEtherWalletAddress);
					String etherWalletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
							decryptEtherWalletAddress);
					String bitcoinReceivingAddress = bitcoinUtils.createBitcoinWallet(registerInfoModel);
					if (etherWalletAddress != null) {
						responseDTO.setEtherWalletAddress(etherWalletAddress);
					}
					if (bitcoinReceivingAddress != null) {
						responseDTO.setBitcoinWalletReceivingAddress(bitcoinReceivingAddress);
					}

					responseDTO.setRoleId(registerInfoModel.getRoleId());
					responseDTO.setStatus("Success");
					responseDTO.setQRCode(qrCodePath);
//					TokenInfo tokenInfo = tokenInfoRepository.findTokenInfoById(1);
//					responseDTO.setTtCoinRate(tokenInfo.getTtCoinRate());
//
//					FreezedTokenInfo freezedTokenInfo = freezedTokenInfoRepository
//							.findFreezedTokenInfoByEtherWalletAddress(etherWalletAddress);
//					if (freezedTokenInfo == null) {
//						responseDTO.setFreezedTokens(0.00000000);
//					} else {
//						responseDTO.setFreezedTokens(freezedTokenInfo.getFreezedTokens());
//					}

					HttpSession session = request.getSession(true);
					System.out.println("Getting Session Id:::::::" + session.getId());
					session.setAttribute("id", registerInfoModel.getId());
					session.setAttribute("emailId", registerInfoModel.getEmailId());
					HttpSessionEvent event = new HttpSessionEvent(session);
					sessionCollector.sessionCreated(event);
					responseDTO.setSessionId(session.getId());
					return responseDTO;
				}
			} else if (registerInfoModel.getRoleId() == 1) {

				String password = registerInfoModel.getPassword();
				System.out.println("Password::::::::::::" + password);

				String decryptPassword = EncryptDecrypt.decrypt(password);
				System.out.println("decryptPassword:::::::::::" + decryptPassword);

				QRcode qrCode = QRCodeRepository.findQRcodeByQrKey("QRKey");
				String qrCodePath = qrCode.getQrcodeValue();
				qrCodePath = qrCodePath + registerInfoModel.getId() + "//" + registerInfoModel.getId() + ".png";

				String imageURL = "<a href='"+env.getProperty("qrcode.url")+ qrCodePath +"'>" +"</a><br><br>";
				
				String images = "<img src=" + "http://localhost:8080/" +qrCodePath +">" ;				
				
				
				
				if (registerDTO.getPassword().equals(decryptPassword)) {
					// artcoinUserInfoModel.setStatus(true);
					responseDTO.setUserName(registerInfoModel.getUserName().trim());
					responseDTO.setEmailId(registerInfoModel.getEmailId().trim());

					String decryptEtherWalletAddress = EncryptDecrypt
							.decrypt(registerInfoModel.getEtherWalletAddress());
					System.out.println("decryptWalletAddress::::::::" + decryptEtherWalletAddress);
					String etherWalletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
							decryptEtherWalletAddress);
					String bitcoinReceivingAddress = bitcoinUtils.createBitcoinWallet(registerInfoModel);
					Integer totalNumberOfUsers = userRegisterService.UsersCount(2);
					if (etherWalletAddress != null) {
						responseDTO.setEtherWalletAddress(etherWalletAddress);
					}
					if (bitcoinReceivingAddress != null) {
						responseDTO.setBitcoinWalletReceivingAddress(bitcoinReceivingAddress);
					}
					responseDTO.setNumberOfUsers(totalNumberOfUsers);
					responseDTO.setRoleId(registerInfoModel.getRoleId());
					responseDTO.setStatus("Success");
					responseDTO.setQRCode(images);

//					TokenInfo tokenInfo = tokenInfoRepository.findTokenInfoById(1);
//					System.out.println(tokenInfo.getAvailableTokens());
//
//					responseDTO.setSoldTokens(tokenInfo.getSoldTokens());
//					responseDTO.setTotalTokens(tokenInfo.getTotalTokens());
//					responseDTO.setBurnedTokens(tokenInfo.getBurnedTokens());
//					responseDTO.setFreezedTokens(tokenInfo.getFreezedTokens());

					HttpSession session = request.getSession(true);
					System.out.println("Getting Session Id:::::::" + session.getId());
					session.setAttribute("id", registerInfoModel.getId());
					session.setAttribute("emailId", registerInfoModel.getEmailId());
					HttpSessionEvent event = new HttpSessionEvent(session);
					sessionCollector.sessionCreated(event);
					responseDTO.setSessionId(session.getId());
					return responseDTO;

				}
			}
		}
		responseDTO.setStatus("failed");
		return responseDTO;
	
	
}	
	@Override
	public Integer UsersCount(Integer roleId) {
		// TODO Auto-generated method stub
		if (roleId != null) {
			Integer totalUsers = userRegisterInfoRepository.countUserModelInfoByRoleId(roleId);
			System.out.println(totalUsers);
			return totalUsers;
		}
		return null;
		
	}

public Image getImage(String id) {
		
		QRcode qrcode = QRCodeRepository.findQRcodeByQrKey("QRKey");
		String imagepath = qrcode.getQrcodeValue() + id;
		System.out.println("imagepath" + imagepath);
		Image image = Toolkit.getDefaultToolkit().getImage(qrcode.getQrcodeValue() + id +".png");
//		Image images = new ImageIcon(this.getClass().getResource(qrcode.getQrcodeValue() + id +".png")).getImage();
		System.out.println(image);
		return image;
	}

@Override
public BigDecimal getEtherBalance(RegisterDTO registerDTO) throws Exception {
	HttpSession session = SessionCollector.find(registerDTO.getSessionId());

	ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("walletfile");

	String mail = (String) session.getAttribute("emailId");
	System.out.println("mail:::::::::" + mail);

	RegisterInfo register = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
	String walletAddress;

	if (register != null) {
		String decryptWalletAddress = EncryptDecrypt.decrypt(register.getEtherWalletAddress());
		walletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(), decryptWalletAddress);

		if (walletAddress == null) {
			return null;
		}
		System.out.println("Ether Address::::::::::::::::" + walletAddress);
		registerDTO.setCentralAdmin(walletAddress);

		EthGetBalance ethGetBalance;
		ethGetBalance = web3j.ethGetBalance(registerDTO.getCentralAdmin(), DefaultBlockParameterName.LATEST)
				.sendAsync().get();
		BigInteger wei = ethGetBalance.getBalance();
		System.out.println("ether bal:::::::::::" + wei);
		BigDecimal amountCheck = Convert.fromWei(wei.toString(), Convert.Unit.ETHER);
		System.out.println("ether bal:::::::::::" + amountCheck);
		registerDTO.setEtherBalance(amountCheck);
		return amountCheck;
	} else
		return null;
}


@Override
public boolean etherTransfer(TokenDTO tokenDTO) throws InterruptedException, Exception{
	
	

	   ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("walletFile");

	   HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
	   System.out.println("Session ID :::::::::::::::" + tokenDTO.getSessionId());

	   String mail = (String) session.getAttribute("emailId");

	   System.out.println("email::::::::::::::::" + mail);
	   
	   RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
	   
	   String fromAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),EncryptDecrypt.decrypt(registerInfo.getEtherWalletAddress()));
	   String etherWalletAddress = registerInfo.getEtherWalletAddress();
	   String etherWalletPassword = registerInfo.getEtherWalletPassword();
	   System.out.println("etherWalletPassword:::::::::::" + etherWalletPassword);
	   BigDecimal amount=tokenDTO.getEtherTransferAmount();
	   System.out.println("path" + configInfo.getConfigValue() + EncryptDecrypt.decrypt(etherWalletAddress));
	   Credentials credentials = WalletUtils.loadCredentials(EncryptDecrypt.decrypt(etherWalletPassword),
	   configInfo.getConfigValue() + EncryptDecrypt.decrypt(etherWalletAddress));

	   TransactionReceipt transactionreceipt = Transfer.sendFunds(web3j, credentials, tokenDTO.getEtherWalletAddress(),amount, Convert.Unit.ETHER).sendAsync().get();
	   System.out.println("Ether Transaction::::::::::" + transactionreceipt.toString());
	   
	   Double amount1=amount.doubleValue();
	   
	   RegisterInfo registerinfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
	  
	   if (registerinfo.getEmailId() != null){
		  
	   if(transactionreceipt!=null){
	
		   TransactionInfo transactionInfo=new TransactionInfo();
		   transactionInfo.setFromAddress(fromAddress);
		   transactionInfo.setToAddress(tokenDTO.getEtherWalletAddress());
		   transactionInfo.setAmount(amount1);
		   transactionInfo.setCreatedDate(new Date());
		   transactionInfo.setTransferMode("ETH");
		   transactionInfo.setTransferStatus(true);
		   transactionInfoRepository.save(transactionInfo);
		   return true;
		   }
	   }
	   
	return false;
	}  
	 
}

