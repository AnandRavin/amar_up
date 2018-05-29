package com.cypherx.solidity.handler;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;

import com.cypherx.dto.TokenDTO;
import com.cypherx.model.ConfigInfo;
import com.cypherx.model.RegisterInfo;
import com.cypherx.model.TokenInfo;
import com.cypherx.model.TransactionInfo;
import com.cypherx.repository.ConfigInfoRepository;
import com.cypherx.repository.TokenInfoRepository;
import com.cypherx.repository.TransactionInfoRepository;
import com.cypherx.repository.UserRegisterInfoRepository;
import com.cypherx.session.SessionCollector;
import com.cypherx.soliditytojava.CYPHERX;
import com.cypherx.userutils.EncryptDecrypt;
import com.cypherx.userutils.UserUtils;

@Service
public class SolidityHandler {
	
   public static CYPHERX Token;
	
	@Autowired
	private UserUtils userUtils;

	@Autowired
	private Environment env;
	
	@Autowired
	private ConfigInfoRepository configInfoRepository;

	@Autowired
	private UserRegisterInfoRepository userRegisterInfoRepository;

	@Autowired
	private TransactionInfoRepository transactionInfoRepository;

	@Autowired
	private TokenInfoRepository tokenInfoRepository;

	

	private final Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io"));

	private BigInteger gasPrice = BigInteger.valueOf(100000000000L);
	private BigInteger gasLimit = BigInteger.valueOf(6500000);
//	
//	private BigInteger gasPrice =BigInteger.valueOf(2200000000L);
//	private BigInteger gasLimit = BigInteger.valueOf(6700000);
	
//	private BigInteger gasPrice = BigInteger.valueOf(20000000000L);
//	private BigInteger gasLimit = BigInteger.valueOf(200000);
	
	
	
	
	
	
	public String transferToken(TokenDTO tokenDTO) throws Exception {

		
		ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("walletFile");

		HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
		System.out.println("Session ID " + tokenDTO.getSessionId());

		String mail = (String) session.getAttribute("emailId");

		RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
		
		String fromAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
				EncryptDecrypt.decrypt(registerInfo.getEtherWalletAddress()));

		System.out.println("configInfo.getConfigValue() + EncryptDecrypt.decrypt(registerInfo.getEtherWalletAddress())");
		
		Credentials credentials = WalletUtils.loadCredentials(tokenDTO.getEtherWalletPassword(),
				configInfo.getConfigValue() + EncryptDecrypt.decrypt(registerInfo.getEtherWalletAddress()));

		Token = CYPHERX.load(this.env.getProperty("token.address"), web3j, credentials, gasPrice,
				gasLimit);
		
		
		if (Token != null) {

			System.out.println(tokenDTO.getAmount() * 100000000);

			BigInteger amount = BigDecimal.valueOf(tokenDTO.getAmount() * 100000000).toBigInteger();
			TransactionReceipt transactionReceipt = Token.transfer(tokenDTO.getToAddress().trim(), amount).sendAsync().get();
			RegisterInfo registerinfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
			if (registerinfo.getEmailId() != null) {

				if (transactionReceipt != null) {
					TransactionInfo transactionHistory = new TransactionInfo();
					transactionHistory.setFromAddress(fromAddress);
					transactionHistory.setToAddress(tokenDTO.getToAddress());
					transactionHistory.setAmount(tokenDTO.getAmount());
					transactionHistory.setCreatedDate(new Date());
					transactionHistory.setTransferStatus(true);
					transactionHistory.setTransferMode("CYX");
					transactionInfoRepository.save(transactionHistory);

					TokenInfo tokenInfo = tokenInfoRepository.findTokenInfoById(1);
					tokenInfo.setSoldTokens(tokenInfo.getSoldTokens() + tokenDTO.getAmount());
					tokenInfoRepository.save(tokenInfo);
					
					return transactionReceipt.toString();
				}
			}
			

		}
		
		
		return null;
	}

	

}
