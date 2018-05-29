package com.cypherx.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.cypherx.dto.StatusResponseDTO;
import com.cypherx.dto.TokenDTO;
import com.cypherx.service.TokenService;
import com.cypherx.session.SessionCollector;
import com.cypherx.userutils.BitcoinUtils;
import com.cypherx.userutils.CurrentValueUtils;
import com.cypherx.userutils.UserUtils;
import com.google.gson.Gson;
import com.wordnik.swagger.annotations.ApiOperation;

import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping(value = "/cypherx/api/token")

public class TokenController {
	
	private static final Logger LOG = LoggerFactory.getLogger(TokenController.class);
	
	
	@Autowired
	private Environment env;
	
	@Autowired
	SessionCollector sessionCollector;
	
	@Autowired
	TokenService tokenService;
	
	@Autowired
	UserUtils userUtils;
	
	@Autowired
	CurrentValueUtils currentValueUtils;
	
	@Autowired
	BitcoinUtils bitcoinUtils;
	
	
	
	@CrossOrigin
	@RequestMapping(value = "/balance", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "Token Balance", notes = "Need to get token balance")
	public synchronized ResponseEntity<String> tokenBalance(
	@ApiParam(value = "Token Balance", required = true) @RequestBody TokenDTO tokenDTO,HttpServletRequest request) {
			StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
			try {
				HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
				if (session == null) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("session.expired"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}

				Double tokenBalance = tokenService.tokenBalance(tokenDTO);
				if (tokenBalance == null) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("token.balance.failed"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}

				else {
					statusResponseDTO.setStatus(env.getProperty("success"));
					statusResponseDTO.setMessage(env.getProperty("token.balance.success"));
					statusResponseDTO.setTokenBalanceInfo(tokenDTO);
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
				}
			} 
			catch (Exception e) {
				e.printStackTrace();
				LOG.error("Problem in Token Balance: ", e);
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("server.problem"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			}
		}
	
	
	
	@CrossOrigin
	@RequestMapping(value = "/transfer", method = RequestMethod.POST, produces = { "application/json" })
	@ApiOperation(value = "Token Transfer", notes = "Need to transfer token")
	public synchronized ResponseEntity<String> tokenTransfer(
	@ApiParam(value = "Token Transfer", required = true) @RequestBody TokenDTO tokenDTO,HttpServletRequest request) {
			StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
			try {
				HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
				if (session == null) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("session.expired"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}

				boolean isValidateTokenName = userUtils.validateToAddress(tokenDTO);
				if (!isValidateTokenName) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("valid.tokenAddress"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}

				boolean isSameAddress = userUtils.isSameaddress(tokenDTO);
				if (isSameAddress) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("same.address"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
				
				boolean isSamePassword = userUtils.isSamePassword(tokenDTO);
				if (!isSamePassword) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("same.password"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}

				boolean tokenAmountValidation = userUtils.tokenAmountValidation(tokenDTO);
				if (!tokenAmountValidation) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("token.amount.validation"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
				boolean istransferToken = tokenService.tokenTransfer(tokenDTO);
				if (!istransferToken) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("token.transfer.failed"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				} 
				else {
					statusResponseDTO.setStatus(env.getProperty("success"));
					statusResponseDTO.setMessage(env.getProperty("token.transfer.success"));
					
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
				}

			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("Problem in Token Transfer: ", e);
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("server.problem"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			}
	}
	
	@CrossOrigin
	@RequestMapping(value = "/recentTransactionHistory", method = RequestMethod.POST, produces = { "application/json" })
	public synchronized ResponseEntity<String> recentTransactionHistory(
	@ApiParam(value = "Recent Transaction History", required = true) @RequestBody TokenDTO tokenDTO,HttpServletRequest request) {
			StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
			try {
				HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
				if (session == null) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("session.expired"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}
				
				List<TokenDTO> list = tokenService.recentListTransactions();
				
				if (list == null) {
					statusResponseDTO.setStatus(env.getProperty("failure"));
					statusResponseDTO.setMessage(env.getProperty("history.failed"));
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
				}

				else {
					statusResponseDTO.setStatus(env.getProperty("success"));
					statusResponseDTO.setMessage(env.getProperty("history.success"));
					statusResponseDTO.setListToken(list);
					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
				}

			} catch (Exception e) {
				e.printStackTrace();
				LOG.error("Problem in Recent TransactionHistory : ", e);
				statusResponseDTO.setStatus(env.getProperty("failure"));
				statusResponseDTO.setMessage(env.getProperty("server.problem"));
				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
			}

		}


//	@CrossOrigin
//	@RequestMapping(value = "/contributeInCrowdsale", method = RequestMethod.POST, produces = { "application/json" })
//	@ApiOperation(value = "Contribute in Crowdsale", notes = "Need to Contribute in crowdsale")
//	public synchronized ResponseEntity<String> contribution(
//			@ApiParam(value = "Contribute in Crowdsale", required = true) @RequestBody TokenDTO tokenDTO,
//			HttpServletRequest request) {
//		StatusResponseDTO statusResponseDTO = new StatusResponseDTO();
//		try {
//			HttpSession session = SessionCollector.find(tokenDTO.getSessionId());
//			if (session == null) {
//				statusResponseDTO.setStatus(env.getProperty("failure"));
//				statusResponseDTO.setMessage(env.getProperty("session.expired"));
//				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//			}
//
//			boolean isValid = userUtils.validateTokenParam(tokenDTO);
//			if (!isValid) {
//				statusResponseDTO.setStatus(env.getProperty("failure"));
//				statusResponseDTO.setMessage(env.getProperty("validate.token.failed"));
//				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//			}
//
//			/* This Is Ethereum */
//			if (tokenDTO.getSelectTransactionType() == 1) {
//				boolean valid = userUtils.validatePasswordPrams(tokenDTO);
//				if (!valid) {
//					statusResponseDTO.setStatus(env.getProperty("failure"));
//					statusResponseDTO.setMessage(env.getProperty("walletpassword.incorrect"));
//					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//				}
//
//				boolean isToken = tokenService.validAmount(tokenDTO);
//				if (!isToken) {
//					statusResponseDTO.setStatus(env.getProperty("failure"));
//					statusResponseDTO.setMessage(env.getProperty("balance.insufficient"));
//					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//				}
//			}
//			/* This Is Bitcoin */
//			if (tokenDTO.getSelectTransactionType() == 0) {
//				boolean isBalance = bitcoinUtils.bitcoinBalanceCheck(tokenDTO);
//				if (!isBalance) {
//					statusResponseDTO.setStatus(env.getProperty("failure"));
//					statusResponseDTO.setBitcoinResponse(env.getProperty("insufficent.fund"));
//					return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//				}
//			}
//
//			boolean isContribute = bitcoinUtils.contributeToken(tokenDTO);
//			if (!isContribute) {
//				statusResponseDTO.setStatus(env.getProperty("failure"));
//				statusResponseDTO.setMessage(env.getProperty("contribute.failed"));
//				return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.PARTIAL_CONTENT);
//			}
//			statusResponseDTO.setStatus(env.getProperty("success"));
//			statusResponseDTO.setMessage(env.getProperty("contribute.success"));
//			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.OK);
//
//		} catch (Exception e) {
//			e.printStackTrace();
//			LOG.error("Problem in Token Transfer in crowdsale: ", e);
//			statusResponseDTO.setStatus(env.getProperty("failure"));
//			statusResponseDTO.setMessage(env.getProperty("server.problem"));
//			return new ResponseEntity<String>(new Gson().toJson(statusResponseDTO), HttpStatus.CONFLICT);
//		}
//	}

	
	
	
	
}

