package com.cypherx.controller;


import java.awt.Image;
import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;

import com.cypherx.dto.LoginDTO;
import com.cypherx.dto.RegisterDTO;
import com.cypherx.dto.StatusResponseDTO;
import com.cypherx.dto.TokenDTO;
import com.cypherx.model.ConfigInfo;
import com.cypherx.repository.ConfigInfoRepository;
import com.cypherx.serv.impl.EmailNotificationServiceImpl;
import com.cypherx.service.UserRegisterService;
import com.cypherx.session.SessionCollector;
import com.cypherx.userutils.BitcoinUtils;
import com.cypherx.userutils.EncryptDecrypt;
import com.cypherx.userutils.UserUtils;
import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;


@RestController
@RequestMapping(value= "/cypherx/api")
@Api(value="UserRegisterController",description="UserRegisterController Api")
@CrossOrigin

public class UserRegisterController {
	
	static final Logger LOG = LoggerFactory.getLogger(UserRegisterController.class);
	
	@Autowired
	private Environment env;

	@Autowired
	private UserUtils userUtils;
	
	@Autowired
	UserRegisterService userRegisterService;
	
	@Autowired
	ConfigInfoRepository configInfoRepository;
	
	@Autowired
	private EmailNotificationServiceImpl emailNotificationServiceImpl;

	@Autowired
	private BitcoinUtils bitcoinUtils;
	
	@CrossOrigin
	@RequestMapping(value = "/signup", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "signup", notes = "Need to Signup")
	public synchronized ResponseEntity<String> register(
			@ApiParam(value = "Register Users", required = true) @RequestBody RegisterDTO registerDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {

			boolean isValid = userUtils.validateRegistration(registerDTO);
			if (!isValid) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("register.valid"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isValidUsername = userUtils.validateUserName(registerDTO);
			if (!isValidUsername) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("username.valid"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean isValidEmailId = userUtils.validateEmail(registerDTO);
			if (!isValidEmailId) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("mailId.valid"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean isvalidpassword = userUtils.validatePassword(registerDTO);
			if (!isvalidpassword) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("Password.valid"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean isvalidConfirmPassword = userUtils.validateConfirmPassword(registerDTO);
			if (!isvalidConfirmPassword) {

				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("confirmpassword.valid"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean isEmailExist = userRegisterService
					.isAccountExistCheckByEmailId(registerDTO.getEmailId().toLowerCase());
			if (isEmailExist) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("emailId.exist"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			
			boolean isEtherWallet = userRegisterService.isEtherWalletCreated(registerDTO);
			if (!isEtherWallet) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ether.wallet.creation.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			String encryptedPassword = EncryptDecrypt.encrypt(registerDTO.getPassword());
			
			boolean isRegister = userRegisterService.saveRegisterUser(registerDTO, encryptedPassword);

			ConfigInfo configInfo = configInfoRepository.findConfigInfoByConfigKey("WalletFile");
			String decryptedEtherWalletAddress = EncryptDecrypt.decrypt(registerDTO.getEtherWalletAddress());
			System.out.println("decryptedEtherWalletAddress::::::::::::::::::" + decryptedEtherWalletAddress);
			String etherWalletAddress = userUtils.getEtherWalletAddress(configInfo.getConfigValue(),
					decryptedEtherWalletAddress);
			String decryptedEtherWalletPassword = EncryptDecrypt.decrypt(registerDTO.getEtherWalletPassword());
			String encryptEmailId = EncryptDecrypt.encrypt(registerDTO.getEmailId());

			System.out.println("Encrypted Email Id:::::::::::::::" + encryptEmailId);
			
			String verificationLink = "Hi," + "<br><br>" + env.getProperty("email.content") + "<br><br>" + "UserName = "
					+ StringUtils.trim(registerDTO.getUserName()) + "<br>" + "Password = "
					+ StringUtils.trim(registerDTO.getPassword()) + "<br>" + "Ether Wallet Address = "
					+ StringUtils.trim(etherWalletAddress) + "<br>" + "Ether Wallet Password = "
					+ StringUtils.trim(decryptedEtherWalletPassword) + "<br><br>" + "<a href='"
					+ env.getProperty("activation.url") + encryptEmailId + "'>"
					+ env.getProperty("verification.user.portal.url") + "</a><br><br>" + "<br>" + ""
					+ "With Regards,<br>" + env.getProperty("cypherx.team");

			// Send verification link to user email Id - With subject & content
			
			
			boolean isEmailSent = emailNotificationServiceImpl.sendEmail(registerDTO.getEmailId(),
					"CYPHERX App Registration", verificationLink);
			if (!isEmailSent) {
				// Email sending failed
				statusResponseDTO.setStatus(env.getProperty("failure"));				statusResponseDTO.setMessage(env.getProperty("register.emailSendFailed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			if (isRegister) {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("register.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			
			}
				
			else {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("register.failed"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}

			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("Problem in User Registration: ", e);
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("server.problem"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			}
	}
	
	
	@CrossOrigin
	@RequestMapping(value = "/emailverification", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "validate user", notes = "Verify emailverification and send response to activate user Account")
	public ResponseEntity<String> emailVerification(
			@ApiParam(value = "Required user details", required = true) @RequestBody RegisterDTO registerDTO) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValidInputParams = userUtils.validateverificationParams(registerDTO);
			if (!isValidInputParams) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrectDetails"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isStatusActive = userUtils.isStatusActive(registerDTO);

			if (isStatusActive) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("email.verification.exist"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			boolean validateEmailLink = userUtils.validateEmailLink(registerDTO);

			if (!validateEmailLink) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("email.verification.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("email.verification.successfully"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);

			}

		} catch (Exception e) {
			LOG.error("Problem in Email Verification User Activities Controller  : ", e);
			e.printStackTrace();
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

		}

	}
	
	@CrossOrigin
	@RequestMapping(value = "/login", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "login admin/user", notes = "Need to get admin/user details and login admin/user")
	public synchronized ResponseEntity<String> loginUser(
			@ApiParam(value = "Login admin/user", required = true) @RequestBody RegisterDTO registerDTO,
			HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			boolean isValidLogin = userUtils.validateLoginParam(registerDTO);
			if (!isValidLogin) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("incorrect.details"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean validEmailId = userUtils.validateEmail(registerDTO);
			if (!validEmailId) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("valid.emailId"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			LoginDTO responseDTO = userRegisterService.isEmailAndPasswordExit(registerDTO, request);
			if (responseDTO.getStatus().equalsIgnoreCase("failed")) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("login.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);

			}

			boolean isValidate = userUtils.validateActivation(registerDTO);
			if (!isValidate) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("login.valid.activation"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("login.success"));
			statusResponseDTO.setLoginInfo(responseDTO);
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		} 
		
		catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in registration User Activities Controller  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/ether/balance", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "Ether Balance", notes = "Need to get Ether balance")
	public synchronized ResponseEntity<String> etherBalance(
			@ApiParam(value = "Ether Balance", required = true) @RequestBody RegisterDTO registerDTO,
			HttpServletRequest request) throws InterruptedException {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {
			HttpSession session = SessionCollector.find(registerDTO.getSessionId());
			if (session == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("session.expired"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			BigDecimal etherBalance = userRegisterService.getEtherBalance(registerDTO);
			if (etherBalance == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ether.balance.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("ether.balance.success"));
				statusResponseDTO.setEtherBalanceInfo(registerDTO);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in registration : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}
	}
	@CrossOrigin
	@RequestMapping(value = "/ether/transfer", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "Ether Transfer", notes = "Need to transfer Ether")
	public synchronized ResponseEntity<String> transferEther(
			@ApiParam(value = "Ether Transfer", required = true) @RequestBody TokenDTO tokenDTO,
			HttpServletRequest request) throws InterruptedException {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {
			HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
			if (session == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("session.expired"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isSameAddress = userUtils.isSameaddress(tokenDTO);
			if (isSameAddress) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("same.address"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isValidateToAddress = userUtils.validateEtherWalletAddress(tokenDTO);
			if (!isValidateToAddress) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("valid.address"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isBalance = userUtils.etherBalanceCheck(tokenDTO);
			if (!isBalance) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ether.balance"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			
			boolean isEtherPassword = userUtils.isEtherPassword(tokenDTO);
			if (!isEtherPassword) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("same.password"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isTransfer = userRegisterService.etherTransfer(tokenDTO);
			if (!isTransfer) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("ether.transaction.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("ether.transaction.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in Ether Transfered  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/bitcoin/balance", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "Bitcoin Balance", notes = "Need to get bitcoin balance")
	public synchronized ResponseEntity<String> bitcoinBalance(
			@ApiParam(value = "Bitcoin Balance", required = true) @RequestBody RegisterDTO registerDTO,
			HttpServletRequest request) throws InterruptedException {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();

		try {
			HttpSession session = SessionCollector.find(registerDTO.getSessionId());
			if (session == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("session.expired"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			BigDecimal bitcoinBalance = bitcoinUtils.bitcoinBalance(registerDTO);
			if (bitcoinBalance == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("bitcoin.balance.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("bitcoin.balance.success"));
				statusResponseDTO.setBitcoinBalanceInfo(registerDTO);
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in registration : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/bitcoin/transfer", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "Bitcoin Transfer", notes = "Need to transfer bitcoin")
	public synchronized ResponseEntity<String> transferBitcoin(
			@ApiParam(value = "Bitcoin Transfer", required = true) @RequestBody RegisterDTO registerDTO,
			HttpServletRequest request) throws InterruptedException {

		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {
			HttpSession session = SessionCollector.find(registerDTO.getSessionId());
			if (session == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("session.expired"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isBalance = bitcoinUtils.bitcoinBalanceCheck(registerDTO);
			if (!isBalance) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("bitcoin.balance"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			
			boolean isBitcoinPassword = userUtils.isBitcoinPassword(registerDTO);
			if (!isBitcoinPassword) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("bitcoin.password"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean isTransfer = bitcoinUtils.transferAmount(registerDTO);
			if (!isTransfer) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("bitcoin.transaction.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			} else {
				statusResponseDTO.setStatus(env.getProperty("success"));
				statusResponseDTO.setMessage(env.getProperty("bitcoin.transaction.success"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in registration  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}
	}

	@CrossOrigin
	@RequestMapping(value = "/logout", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "User Logout", notes = "Need to Logout")
	public synchronized ResponseEntity<String> logoutUser(
			@ApiParam(value = "User Logout", required = true) @RequestBody RegisterDTO registerDTO,
			HttpServletRequest request) {
		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
		try {

			HttpSession session = SessionCollector.find(registerDTO.getSessionId());
			if (session == null) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("session.expired"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}

			boolean islogout = userUtils.logoutParam(registerDTO);
			if (!islogout) {
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("logout.failed"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
			}
			statusResponseDTO.setStatus(env.getProperty("success"));
			statusResponseDTO.setMessage(env.getProperty("logout"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
		} 
		catch (Exception e) {
			e.printStackTrace();
			LOG.error("Problem in Logout  : ", e);
			statusResponseDTO.setStatus(env.getProperty("failure"));
			statusResponseDTO.setMessage(env.getProperty("server.problem"));
			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
		}
	}
	
	
	@ResponseBody
	@RequestMapping(value = "/Image/{id:.+}", method = RequestMethod.GET, consumes = MediaType.ALL_VALUE, produces = MediaType.IMAGE_JPEG_VALUE)
	public ResponseEntity<Image> getImage(@PathVariable("id")String id) {
	    Image image = userRegisterService.getImage(id);  //this just gets the data from a database
	    return ResponseEntity.ok(image);
	}
	
	
}
