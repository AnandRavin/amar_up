package com.cypherx.serv.impl;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.DefaultBlockParameterName;
import org.web3j.protocol.core.methods.response.EthGetBalance;
import org.web3j.protocol.http.HttpService;
import org.web3j.utils.Convert;

import com.cypherx.dto.TokenDTO;
import com.cypherx.model.ConfigInfo;
import com.cypherx.model.RegisterInfo;
import com.cypherx.model.TransactionInfo;
import com.cypherx.repository.ConfigInfoRepository;
import com.cypherx.repository.TransactionInfoRepository;
import com.cypherx.repository.UserRegisterInfoRepository;
import com.cypherx.service.TokenService;
import com.cypherx.session.SessionCollector;
import com.cypherx.solidity.handler.SolidityHandler;
import com.cypherx.soliditytojava.CYPHERX;
import com.cypherx.userutils.CurrentValueUtils;
import com.cypherx.userutils.EncryptDecrypt;
import com.cypherx.userutils.UserUtils;

@Service
public class TokenServiceImpl implements TokenService {
	
	
	@Autowired
	private Environment env;

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	UserRegisterInfoRepository userRegisterInfoRepository;
	
	@Autowired
	TransactionInfoRepository transactionInfoRepository; 
	
	@Autowired
	private SolidityHandler solidityHandler;
	
	@Autowired
	CurrentValueUtils currentValueUtils;

	

	@Autowired
	private ConfigInfoRepository configInfoRepository;

	private final Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io"));

	  private BigInteger gasPrice = BigInteger.valueOf(100000000000L);
	  private BigInteger gasLimit = BigInteger.valueOf(6500000);

	@Override
	public Double tokenBalance(TokenDTO tokenDTO) throws Exception {
		Credentials credentials;

		HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
		ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("walletFile");
		String mail = (String) session.getAttribute("emailId");

		RegisterInfo registerInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
		if (registerInfo != null && configInfo != null) {
			String decryptEtherWalletAddress = EncryptDecrypt.decrypt(registerInfo.getEtherWalletAddress());
			String dercyptEtherWalletPassword = EncryptDecrypt.decrypt(registerInfo.getEtherWalletPassword());

			String etherWalletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
					decryptEtherWalletAddress);
			if (etherWalletAddress == null) {
				return null;
			}

			credentials = WalletUtils.loadCredentials(dercyptEtherWalletPassword,
					new File(configInfo.getConfigValue() + "//" + decryptEtherWalletAddress));

			CYPHERX Token = CYPHERX.load(this.env.getProperty("token.address"), web3j, credentials, gasPrice, gasLimit);

			BigInteger balance = Token.balanceOf(etherWalletAddress).sendAsync().get();
			System.out.println(balance.doubleValue() / 100000000);
			tokenDTO.setTokenBalance(balance.doubleValue() / 100000000);
			return balance.doubleValue();

		}

		return null;
		
}
	
	
	
	@Override
	public boolean tokenTransfer(TokenDTO tokenDTO) throws Exception {

		String transferToken = solidityHandler.transferToken(tokenDTO);

		if (transferToken != null) {
			return true;
		}
		return false;
	}

	
	@Override
	public boolean validAmount(TokenDTO tokenDTO) throws Exception {
		RegisterInfo artcoinUserModelInfo = new RegisterInfo();
		ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("walletFile");
		
		HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
		System.out.println("SessionId::::::::::::::::::"+tokenDTO.getSessionId());
		String email = (String) session.getAttribute("emailId");
		
		artcoinUserModelInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(email);
		String decryptedWalletAddress = EncryptDecrypt.decrypt(artcoinUserModelInfo.getEtherWalletAddress());
		String etherWalletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(), decryptedWalletAddress);
		tokenDTO.setEtherWalletAddress(etherWalletAddress);
		tokenDTO.setCentralAdmin(tokenDTO.getEtherWalletAddress());
		
		EthGetBalance ethGetBalance;
		ethGetBalance = web3j.ethGetBalance(tokenDTO.getCentralAdmin(), DefaultBlockParameterName.LATEST).sendAsync().get();
		BigInteger wei = ethGetBalance.getBalance();
		BigInteger amountCheck = Convert.fromWei(wei.toString(), Convert.Unit.ETHER).toBigInteger();
		tokenDTO.setEtherBalance(amountCheck);
		
		if(amountCheck != null) {
			BigInteger balance = tokenDTO.getEtherBalance();
			double dbl = tokenDTO.getRequestTokens();
			double artcoin = dbl * currentValueUtils.getEtherValueForOneDollar();
			System.out.println("balance" + balance);
			System.out.println("artcoin" + artcoin);
			BigDecimal val = BigDecimal.valueOf(artcoin);
			BigInteger val2 = val.toBigInteger();
			int res = balance.compareTo(val2);
			if (res == 1) {
				return true;
		}
		}
		return false;

	}


	@Override
	public List<TokenDTO> recentListTransactions() {
		
		List<TokenDTO> transactionList = new ArrayList<TokenDTO>();

		List<TransactionInfo> transferInfo = (List<TransactionInfo>)transactionInfoRepository.findAll();
		
		for (int i = transferInfo.size(); i >0; i--) {
			TokenDTO tokenDTO = new TokenDTO();
					
			TransactionInfo transactionInfo1 = transactionInfoRepository.findById(i);
			
			tokenDTO.setFromAddress(transactionInfo1.getFromAddress());
			tokenDTO.setToAddress(transactionInfo1.getToAddress());
			tokenDTO.setAmount(transactionInfo1.getAmount());
			tokenDTO.setCreatedDate(transactionInfo1.getCreatedDate());
			tokenDTO.setTransferStatus(transactionInfo1.isTransferStatus());
			tokenDTO.setTransferMode(transactionInfo1.getTransferMode());
			
			transactionList.add(tokenDTO);
		}
		return transactionList;
	}
	
	
	


	
}
