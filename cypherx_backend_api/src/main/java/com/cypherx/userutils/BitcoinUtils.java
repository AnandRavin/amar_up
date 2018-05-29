package com.cypherx.userutils;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.Date;

import javax.servlet.http.HttpSession;

import org.bitcoinj.core.Address;
import org.bitcoinj.core.Coin;
import org.bitcoinj.core.InsufficientMoneyException;
import org.bitcoinj.core.NetworkParameters;
import org.bitcoinj.core.Transaction;
import org.bitcoinj.kits.WalletAppKit;
import org.bitcoinj.params.MainNetParams;
import org.bitcoinj.params.TestNet3Params;
import org.bitcoinj.wallet.Wallet;
import org.bitcoinj.wallet.listeners.WalletCoinsReceivedEventListener;
import org.bitcoinj.wallet.listeners.WalletCoinsSentEventListener;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.Transfer;

import com.cypherx.dto.RegisterDTO;
import com.cypherx.dto.StatusResponseDTO;
import com.cypherx.dto.TokenDTO;
import com.cypherx.model.BitcoinConfigInfo;
import com.cypherx.model.ConfigInfo;
import com.cypherx.model.ReferralReceivedInfo;
import com.cypherx.model.RegisterInfo;
import com.cypherx.model.RequestInfo;
import com.cypherx.model.StartEndDateInfo;
import com.cypherx.model.TransactionInfo;
import com.cypherx.repository.BitcoinConfigInfoRepository;
import com.cypherx.repository.ConfigInfoRepository;
import com.cypherx.repository.ReferralReceivedInfoRepository;
import com.cypherx.repository.RequestInfoRepository;
import com.cypherx.repository.StartEndDateInfoRepository;
import com.cypherx.repository.TransactionInfoRepository;
import com.cypherx.repository.UserRegisterInfoRepository;
import com.cypherx.serv.impl.EmailNotificationServiceImpl;
import com.cypherx.session.SessionCollector;


@Service
public class BitcoinUtils {

	@Autowired
	private Environment env;
	
	@Autowired
	UserRegisterInfoRepository userRegisterInfoRepository;
	
	@Autowired
	private BitcoinConfigInfoRepository bitcoinConfigInfoRepository;
	
	@Autowired
	CurrentValueUtils currentValueUtils;
	
	@Autowired 
	StartEndDateInfoRepository startEndDateInfoRepository;
	
	@Autowired
	UserUtils userUtils;
	
	@Autowired
	ConfigInfoRepository configInfoRepository;
	
	@Autowired
	RequestInfoRepository requestInfoRepository;
	
	@Autowired
	TransactionInfoRepository transactionInfoRepository;
	
	@Autowired
	private ReferralReceivedInfoRepository referralReceivedInfoRepository;
	
	@Autowired
	private EmailNotificationServiceImpl emailNotificationServiceImpl;
	
	public static final MathContext DEFAULT_CONTEXT = new MathContext(0, RoundingMode.UNNECESSARY);

	public static final int DEFAULT_SCALE = Coin.SMALLEST_UNIT_EXPONENT;

	public static final BigDecimal satoshipercoinDecimal = new BigDecimal(Coin.COIN.value, DEFAULT_CONTEXT);

	public static WalletAppKit bitcoin;

	public static NetworkParameters params = TestNet3Params.get();

	private Wallet.SendResult sendResult;

	static final Logger LOG = LoggerFactory.getLogger(BitcoinUtils.class);

	private final Web3j web3j = Web3j.build(new HttpService("https://rinkeby.infura.io"));

	public String createBitcoinWallet(RegisterInfo registerInfo) {
		if (bitcoin == null) {
			BitcoinConfigInfo bitcoinConfigInfo = bitcoinConfigInfoRepository
					.findBitcoinConfigByConfigKey("walletfile");
			if (bitcoinConfigInfo != null) {
				String bitcoinwalletFileLocation = bitcoinConfigInfo.getConfigValue();
				System.out.println("bitcoinwalletFileLocation " + bitcoinwalletFileLocation);

				bitcoin = new WalletAppKit(params, new File(bitcoinwalletFileLocation + "."),
						registerInfo.getId().toString()){
				
		

					@Override
					public void onSetupCompleted() {
						bitcoin.wallet().allowSpendingUnconfirmedTransactions();
						// initializeWallet();
					}
				};
			}
			bitcoin.startAsync();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			String receiveAddress = null;
			if (!bitcoin.wallet().currentReceiveAddress().toString().isEmpty()) {
				receiveAddress = bitcoin.wallet().currentReceiveAddress().toString();
				System.out.println("Current Receive Address:::::::::::::::" + receiveAddress);
			}
			return receiveAddress;
		} else {
			bitcoin.stopAsync();
			bitcoin.awaitTerminated();

			BitcoinConfigInfo bitcoinConfigInfo = bitcoinConfigInfoRepository
					.findBitcoinConfigByConfigKey("walletfile");
			if (bitcoinConfigInfo != null) {
				String bitcoinwalletFileLocation = bitcoinConfigInfo.getConfigValue();
				System.out.println("bitcoinwalletFileLocation " + bitcoinwalletFileLocation);

				bitcoin = new WalletAppKit(params, new File(bitcoinwalletFileLocation + "."),
						registerInfo.getId().toString()) {

					@Override
					public void onSetupCompleted() {
						bitcoin.wallet().allowSpendingUnconfirmedTransactions();
					}
				};
			}
			bitcoin.startAsync();
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			String receiveAddress = null;
			if (!bitcoin.wallet().currentReceiveAddress().toString().isEmpty()) {
				receiveAddress = bitcoin.wallet().currentReceiveAddress().toString();
				System.out.println("Current Receive Address:::::::::::::::" + receiveAddress);
			}
			return receiveAddress;
		}
	}

	public void initializeWallet() {
		Wallet wallet = bitcoin.wallet();
		LOG.info("Current Receive Address  :" + wallet.currentReceiveAddress().toString());
	}

	public void getRefreshWallet() {
		System.out.println("Inside Refresh Wallet");
		bitcoin.wallet().addCoinsReceivedEventListener(new WalletCoinsReceivedEventListener() {

			@Override
			public void onCoinsReceived(Wallet arg0, Transaction arg1, Coin arg2, Coin arg3) {
				System.out.println("coins received");

			}
		});

		bitcoin.wallet().addCoinsSentEventListener(new WalletCoinsSentEventListener() {

			@Override
			public void onCoinsSent(Wallet arg0, Transaction arg1, Coin arg2, Coin arg3) {
				System.out.println("coins sent");

			}
		});
	}
	
	public boolean transferAmount(RegisterDTO registerDTO)
			throws InterruptedException, InsufficientMoneyException {
		Thread.sleep(2000);
		
		String fromAddress=bitcoin.wallet().currentReceiveAddress().toString();
		System.out.println("bitcoin wallet address::::::::"+fromAddress);

		String amount = registerDTO.getBitcoinTransferAmount().toString();
		Coin transferAmount = Coin.parseCoin(amount);

		System.out.println("transferAmount:::::::::::" + transferAmount);
		String transferAccount = registerDTO.getBitcoinToAddress();

		System.out.println("transferAccount::::::::::::::::" + transferAccount);
		Address destination = Address.fromBase58(params, transferAccount);

		System.out.println("destination::::::::::::::::::::" + destination);

		Coin balance = bitcoin.wallet().getBalance();
		System.out.println("Wallet Balance :" + balance);

				if (balance.isGreaterThan(Coin.ZERO)) {
			Wallet.SendResult sendResult;

			sendResult = bitcoin.wallet().sendCoins(bitcoin.peerGroup(), destination, transferAmount);
			System.out.println("SendResult::::::::::" + sendResult);
			Double transferbtcAmount=Double.parseDouble(amount);
			
			
			
			getRefreshWallet();
			
			if(sendResult!=null)
			{
				
			
				
				TransactionInfo transactionInfo=new TransactionInfo();
				transactionInfo.setFromAddress(fromAddress);
				transactionInfo.setToAddress(transferAccount);
				transactionInfo.setCreatedDate(new Date());
				transactionInfo.setAmount(transferbtcAmount);
				transactionInfo.setTransferStatus(true);
				transactionInfo.setTransferMode("BTC");
				transactionInfoRepository.save(transactionInfo);
				
				
				
				
			}
			
			return true;
		} else {
			return false;
		}
		
		
	}
	
	public BigDecimal bitcoinBalance(RegisterDTO registerDTO) {

		BigDecimal satoshi = new BigDecimal(bitcoin.wallet().getBalance().getValue(), BitcoinUtils.DEFAULT_CONTEXT);

		BigDecimal bitcoinBalance = satoshi.divide(satoshipercoinDecimal, DEFAULT_SCALE, RoundingMode.UNNECESSARY);

		registerDTO.setBitcoinBalance(bitcoinBalance);

		if (bitcoinBalance != null) {
			return bitcoinBalance;
		} else
			return null;
	}
	
	public boolean bitcoinBalanceCheck(RegisterDTO registerDTO) throws JSONException, IOException {
		
		BigDecimal satoshi = new BigDecimal(bitcoin.wallet().getBalance().getValue(), BitcoinUtils.DEFAULT_CONTEXT);

		BigDecimal bitcoinBalance = satoshi.divide(satoshipercoinDecimal, DEFAULT_SCALE, RoundingMode.UNNECESSARY);

		registerDTO.setBitcoinBalance(bitcoinBalance);
		
		if(registerDTO.getBitcoinTransferAmount().doubleValue() < bitcoinBalance.doubleValue() && registerDTO.getBitcoinTransferAmount().doubleValue() >= 0.001) {
			return true;
		}
		return false;
	}
	
	
//	public boolean bitcoinBalanceCheck(TokenDTO tokenDTO) throws JSONException, IOException {
//
//		if (tokenDTO.getSessionId() != null) {
//			double requestToken = tokenDTO.getRequestTokens();
//			// List<CoinsInfo> coinsList = coinsInfoRepository.findAll();
//			// ArtcoinUserModelInfo artcoinUserModelInfo = new
//			// ArtcoinUserModelInfo();
//			// CoinsInfoDTO coinsInfoDTO = new CoinsInfoDTO();
//			// ArtcoinUserModelInfo artcoinUserModelInfo =
//			// artcoinUserModelInfoRepository.findUserInfoModelByEmailId(tokenDTO.getEmailId());
//			// for (CoinsInfo coinsInfo : coinsList) {
//			// coinsInfoDTO.setId(coinsInfo.getId());
//			// coinsInfoDTO.setBtc(coinsInfo.getBtc());
//			// coinsInfoDTO.setEth(coinsInfo.getEth());
//			// }
//			RegisterDTO registerDTO = new RegisterDTO();
//			double amount = (requestToken * currentValueUtils.getBitcoinValueForOneDollar());
//			BigDecimal bitcoinBalance = bitcoinBalance(registerDTO);
//			String convertAmount = Double.toString(amount);
//			BigDecimal Cbalance = new BigDecimal(convertAmount);
//			int res = bitcoinBalance.compareTo(Cbalance);
//			if (res == 1) {
//				return true;
//			}
//			return false;
//		}
//		return false;
//	}
//	
//	
//	@SuppressWarnings("unused")
//	public boolean contributeToken(TokenDTO tokenDTO) throws Exception {
//
//		StartEndDateInfo startEndDateInfo = startEndDateInfoRepository.findOne(1);
//
//		System.out.println("startEndDateInfo.getEndDate()::::::::::::::" + startEndDateInfo.getStartDate());
//		System.out.println("startEndDateInfo.getEndDate()::::::::::::::" + startEndDateInfo.getEndDate());
//		System.out.println("startEndDateInfo.getEndDate()::::::::::::::" + startEndDateInfo.getMaximumValue());
//		System.out.println("startEndDateInfo.getEndDate()::::::::::::::" + startEndDateInfo.getMinimumValue());
//
//		Date date = new Date();
//		Date date2 = startEndDateInfo.getEndDate();
//
//		if (date2.after(date)) {
//
//			System.out.println("Inside contribution");
//
//			int total = startEndDateInfo.getTotalToken().intValue();
//
//			if (startEndDateInfo.getMaximumValue().intValue() >= total
//					&& startEndDateInfo.getAvailableTokens().intValue() >= tokenDTO.getRequestTokens()) {
//
//				String etherWalletPassword = tokenDTO.getEtherWalletPassword();
//				double requestToken = tokenDTO.getRequestTokens();
//				System.out.println("request tokens" + requestToken);
//
//				// List<CoinsInfo> coinsList = coinsInfoRepository.findAll();
//				// CoinsInfoDTO coinsInfoDTO = new CoinsInfoDTO();
//				ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("walletFile");
//				HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
//				System.out.println("SessionId::::::::::::::::::" + tokenDTO.getSessionId());
//				String email = (String) session.getAttribute("emailId");
//				// String etherWalletPassword = (String)
//				// session.getAttribute("etherWalletPassword");
//
//				RegisterInfo artcoinUserModelInfo = userRegisterInfoRepository
//						.findRegisterInfoByEmailId(email);
//				// RequestInfo requestInfo =
//				// requestInfoRepository.findRequestInfoByEmailId(email);
//				// for (CoinsInfo coinsInfo : coinsList) {
//				// coinsInfoDTO.setId(coinsInfo.getId());
//				// coinsInfoDTO.setBtc(coinsInfo.getBtc());
//				// coinsInfoDTO.setEth(coinsInfo.getEth());
//				// }
//
//				/* Bitcoin Contribute */
//
//				if (tokenDTO.getSelectTransactionType() == 0) {
//					Address destination = Address.fromBase58(params, env.getProperty("bitcoin.receving.address"));
//					if (destination == null) {
//						StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
//						statusResponseDTO.setBitcoinResponse(env.getProperty("address.not.found"));
//					}
//					Double amount = (requestToken * currentValueUtils.getBitcoinValueForOneDollar());
//					System.out.println("Bitcoin Amount" + amount);
//					DecimalFormat df = new DecimalFormat("#.########");
//					Coin transferAmount = Coin.parseCoin(df.format(amount));
//					System.out.println("transferAmount" + transferAmount);
//					String bitcoinReveingaddress = createBitcoinWallet(artcoinUserModelInfo);
//					tokenDTO.setFromAddress(bitcoinReveingaddress);
//					Wallet.SendResult sendResult;
//					try {
//						sendResult = bitcoin.wallet().sendCoins(bitcoin.peerGroup(), destination, transferAmount);
//					} catch (InsufficientMoneyException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
//
//					String etherWalletAddress = EncryptDecrypt.decrypt(artcoinUserModelInfo.getEtherWalletAddress());
//					String walletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
//							etherWalletAddress);
//					tokenDTO.setToAddress(walletAddress);
//					BigDecimal requestamount = new BigDecimal(requestToken);
//					LOG.info("request token before transaction" + requestamount);
//					tokenDTO.setAmount(requestamount.toBigInteger());
//
//					RequestInfo requestInfo = new RequestInfo();
//					requestInfo.setTransactionType(0);
//					requestInfo.setIsTransferStatus(0);
//					requestInfo.setEmailId(email);
//					requestInfo.setCreatedDate(new Date());
//					requestInfo.setEtherWalletAddress(tokenDTO.getToAddress());
//					requestInfo.setRequestToken(requestamount.toBigInteger());
//					requestInfo.setBitcoinWalletAddress(bitcoinReveingaddress);
//					requestInfo.setTransferAmount(amount);
//					requestInfoRepository.save(requestInfo);
//					
//					TransactionInfo transactionHistory = new TransactionInfo();
//					
//					transactionHistory.setRequestId(requestInfo.getId());
//					transactionHistory.setFromAddress(env.getProperty("bitcoin.receving.address"));
//					transactionHistory.setBitcoinTransferedAmount(requestInfo.getTransferAmount());
//					transactionHistory.setEtherTransferedAmount(0.0);
//					transactionHistory.setAmount(requestInfo.getRequestToken());
//					transactionHistory.setCreatedDate(new Date());
//					transactionHistory.setToAddress(walletAddress);
//					transactionHistory.setTransferStatus(false);
//					transactionInfoRepository.save(transactionHistory);
//					
//					artcoinUserModelInfo.setStatus(true);
//					userRegisterInfoRepository.save(artcoinUserModelInfo);
//
//					BigInteger rtkn = BigDecimal.valueOf(requestToken).toBigInteger();
//
//					BigInteger total2 = startEndDateInfo.getTotalToken();
//
//					BigInteger sum = rtkn.add(total2);
//
//					startEndDateInfo.setTotalToken(sum);
//
//					BigInteger availabletoken = startEndDateInfo.getAvailableTokens().subtract(rtkn);
//
//					startEndDateInfo.setAvailableTokens(availabletoken);
//
//					BigInteger sold = startEndDateInfo.getTotalSoldTokens().add(rtkn);
//
//					startEndDateInfo.setTotalSoldTokens(sold);
//
//					startEndDateInfoRepository.save(startEndDateInfo);
//
//					if (requestToken > 10) {
//						System.out.println("Inside referrel Progrma");
//						referralTransfer(requestToken, email);
//					}
//					/*
//					 * boolean check = solidityHandler.TransferCoin(tokenDTO);
//					 * if (check) { return true; }else { Address userDestination
//					 * = Address.fromBase58(params, bitcoinReveingaddress);
//					 * Wallet.SendResult failedResult =
//					 * bitcoin.wallet().sendCoins(bitcoin.peerGroup(),
//					 * userDestination, transferAmount); }
//					 */
//					getRefreshWallet();
//					
//					String verificationLink = "Hi," + "<br><br>"
//							+ env.getProperty("contribute.content") + "<br><br>" + 
//							"Requested Token = " + tokenDTO.getRequestTokens() + "<br>" + "Amount of Bitcoin = " + requestInfo.getTransferAmount() + "<br><br>" +"Token will be transfered to your account at the of Crowdsale" +
//							"<br><br>" + "With Regards,<br><br>"
//							+ env.getProperty("artcoin.team");
//					
//					System.out.println("Email ::::::::::::: Date :::::::::" + requestInfo.getEmailId()+":::::::::" + tokenDTO.getRequestTokens() + requestInfo.getTransferAmount());
//					
//					boolean isEmailSent = emailNotificationServiceImpl.sendEmail(requestInfo.getEmailId(),
//							"Artcoin App Registration", verificationLink);
//					
//					return true;
//
//					/* Ethereum Contribute */
//				} else {
//					System.out.println("Inside Ether" + artcoinUserModelInfo.getEtherWalletAddress());
//					String etherWalletAddress = EncryptDecrypt.decrypt(artcoinUserModelInfo.getEtherWalletAddress());
//					System.out.println("etherWalletAddress" + etherWalletAddress);
//					double amount = (requestToken * currentValueUtils.getEtherValueForOneDollar());
//					System.out.println("amount:::::::" + amount);
//					DecimalFormat df = new DecimalFormat("#.###############");
//					BigDecimal amt = new BigDecimal(df.format(amount));
//					System.out.println("amount: " + amt);
//					//tokenDTO.setAmount(amt.toBigInteger());
//					//tokenDTO.setEtherAmount(amt);
//					Credentials credentials = WalletUtils.loadCredentials(etherWalletPassword,
//							new File(configInfo.getConfigValue() + "//" + etherWalletAddress));
//					TransactionReceipt transactionReceipt = Transfer.sendFunds(web3j, credentials,
//							env.getProperty("main.address"), amt, Convert.Unit.ETHER)
//							.send();
//					if (transactionReceipt != null) {
//						String walletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
//								etherWalletAddress);
//						tokenDTO.setToAddress(walletAddress);
//						BigDecimal requestamount = new BigDecimal(requestToken);
//						LOG.info("request token before transaction" + requestamount);
//						tokenDTO.setAmount(requestamount.toBigInteger());
//
//						// if(requestInfo == null){
//						RequestInfo requestInfo = new RequestInfo();
//						requestInfo.setTransactionType(1);
//						requestInfo.setIsTransferStatus(0);
//						requestInfo.setEmailId(email);
//						requestInfo.setEtherWalletAddress(walletAddress);
//						requestInfo.setRequestToken(requestamount.toBigInteger());
//						requestInfo.setTransferAmount(amt.doubleValue());
//						requestInfo.setCreatedDate(new Date());
//						requestInfoRepository.save(requestInfo);
//
//						artcoinUserModelInfo.setStatus(true);
//						userRegisterInfoRepository.save(artcoinUserModelInfo);
//						
//						TransactionInfo transactionHistory = new TransactionInfo();
//						
//						transactionHistory.setRequestId(requestInfo.getId());
//						transactionHistory.setFromAddress(env.getProperty("main.address"));
//						transactionHistory.setBitcoinTransferedAmount(0.0);
//						transactionHistory.setEtherTransferedAmount(requestInfo.getTransferAmount());
//						transactionHistory.setAmount(requestInfo.getRequestToken());
//						transactionHistory.setCreatedDate(new Date());
//						transactionHistory.setToAddress(walletAddress);
//						transactionHistory.setTransferStatus(false);
//						transactionInfoRepository.save(transactionHistory);
//						
//						
//						
//						
//						// } else {
//						// BigInteger addRequestTokens1 =
//						// requestInfo.getRequestToken().add(requestamount.toBigInteger());
//						// requestInfo.setRequestToken(addRequestTokens1);
//						// Double amount1 =
//						// requestInfo.getTransferAmount()+amount;
//						// requestInfo.setTransferAmount(amount1);
//						// requestInfoRepository.save(requestInfo);
//						// }
//
//						BigInteger rtkn1 = BigDecimal.valueOf(requestToken).toBigInteger();
//
//						BigInteger total1 = startEndDateInfo.getTotalToken();
//
//						BigInteger sum1 = rtkn1.add(total1);
//
//						startEndDateInfo.setTotalToken(sum1);
//
//						BigInteger availabletoken = startEndDateInfo.getAvailableTokens().subtract(rtkn1);
//
//						startEndDateInfo.setAvailableTokens(availabletoken);
//
//						BigInteger sold = startEndDateInfo.getTotalSoldTokens().add(rtkn1);
//
//						startEndDateInfo.setTotalSoldTokens(sold);
//
//						startEndDateInfoRepository.save(startEndDateInfo);
//
//						String verificationLink = "Hi," + "<br><br>"
//								+ env.getProperty("contribute.content") + "<br><br>" + 
//								"Requested Token = " + tokenDTO.getRequestTokens() + "<br>" + "Amount of Ether = " + requestInfo.getTransferAmount() + "<br><br>" +"Token will be transfered to your account at the Of Crowdsale" +
//								"<br><br>" + "With Regards,<br><br>"
//								+ env.getProperty("artcoin.team");
//						
//						System.out.println("Email ::::::::::::: Date :::::::::" + requestInfo.getEmailId()+":::::::::" + tokenDTO.getRequestTokens() + requestInfo.getTransferAmount());
//						
//						boolean isEmailSent = emailNotificationServiceImpl.sendEmail(requestInfo.getEmailId(),
//								"Artcoin App Registration", verificationLink);
//						
//						if (requestToken > 10) {
//							System.out.println("Inside referrel Progrma");
//							referralTransfer(requestToken, email);
//						}
//						/*
//						 * boolean check =
//						 * solidityHandler.TransferCoin(tokenDTO); if(check) {
//						 * return true; } else { String wallet =
//						 * EncryptDecrypt.decrypt(artcoinUserModelInfo.
//						 * getEtherWalletAddress()); double reamount =
//						 * (requestToken *
//						 * currentValueUtils.getEtherValueForOneDollar());
//						 * BigDecimal reamountDecimal = new
//						 * BigDecimal(reamount);
//						 * tokenDTO.setAmount(reamountDecimal); Credentials
//						 * credentials2 =
//						 * WalletUtils.loadCredentials(configInfo.getConfigValue
//						 * (), etherWalletAddress); TransactionReceipt
//						 * retransactionReceipt = Transfer.sendFunds(web3j,
//						 * credentials2, wallet, tokenDTO.getAmount(),
//						 * Convert.Unit.ETHER).send(); }
//						 */
//					}
//				}
//				return true;
//			}
//		}
//		return false;
//	}
//	
//	
//	public boolean referralTransfer(Double requestTokens, String mail) throws Exception {
//		if (requestTokens != null || mail != null) {
//			RegisterInfo artcoinUserModelInfo = userRegisterInfoRepository.findRegisterInfoByEmailId(mail);
//			if (artcoinUserModelInfo.getReferenceId() != 1 && artcoinUserModelInfo.getReferenceId() != null
//					&& artcoinUserModelInfo.getReferenceId() != 0) {
//				RegisterInfo artcoinUserModelInfo2 = userRegisterInfoRepository
//						.findOne(artcoinUserModelInfo.getReferenceId());
//				// System.out.println("ReferralTransfer
//				// EtherWalletAddress:::"+artcoinUserModelInfo2.getEtherWalletAddress());
//				// String decryptWalletAddress =
//				// EncryptDecrypt.decrypt(artcoinUserModelInfo2.getEtherWalletAddress());
//				// ConfigInfo configInfo =
//				// configInfoRepository.findConfigInfoByConfigKey("walletFile");
//				// String etherWalletAddress =
//				// userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
//				// decryptWalletAddress);
//				Double referalTokens = (double) Math.round((requestTokens * 5) / 100);
//				System.out.println("Referal Tokens:::" + referalTokens);
//				System.out
//						.println("ArtcoinUserModelInfo2.getEmailId():::::::::::" + artcoinUserModelInfo2.getEmailId());
//				// RequestInfo requestInfo = requestInfoRepository
//				// .findRequestInfoByEmailId(artcoinUserModelInfo2.getEmailId());
//				// Double userRequestTokens =
//				// requestInfo.getRequestToken().doubleValue();
//				// Double sum = (referalTokens + userRequestTokens);
//				// BigDecimal totalRequestToken = new BigDecimal(sum);
//				// requestInfo.setRequestToken(totalRequestToken.toBigInteger());
//				// requestInfoRepository.save(requestInfo);
//				StartEndDateInfo startEndDateInfo = startEndDateInfoRepository.findOne(1);
//				BigInteger totalTokens = startEndDateInfo.getTotalToken();
//				BigInteger referal = BigDecimal.valueOf(referalTokens).toBigInteger();
//				BigInteger sum2 = totalTokens.add(referal);
//
//				startEndDateInfo.setTotalToken(sum2);
//				
//				BigInteger availabletoken = startEndDateInfo.getAvailableTokens().subtract(referal);
//
//				startEndDateInfo.setAvailableTokens(availabletoken);
//
//				BigInteger sold = startEndDateInfo.getTotalSoldTokens().add(referal);
//
//				startEndDateInfo.setTotalSoldTokens(sold);
//
//				startEndDateInfoRepository.save(startEndDateInfo);
//				ReferralReceivedInfo referralReceivedInfo = new ReferralReceivedInfo();
//				referralReceivedInfo.setUserId(artcoinUserModelInfo.getReferenceId());
//				ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("walletFile");
//				String decryptWalletAddress = EncryptDecrypt.decrypt(artcoinUserModelInfo2.getEtherWalletAddress());
//				String etherWalletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
//						decryptWalletAddress);
//				referralReceivedInfo.setEtherWalletAddress(etherWalletAddress);
//				referralReceivedInfo.setAmount(referal);
//				referralReceivedInfo.setReferralTransferStatus(0);
//				referralReceivedInfo.setReferentPersonName(artcoinUserModelInfo.getUserName());
//				referralReceivedInfo.setCreatedDate(new Date());
//				referralReceivedInfoRepository.save(referralReceivedInfo);
//				return true;
//			}
//			return false;
//		}
//		return false;
//	}
//	
//	
	
	
	
}

