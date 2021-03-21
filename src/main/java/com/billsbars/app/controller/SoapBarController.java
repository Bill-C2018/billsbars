package com.billsbars.app.controller;

import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;
import javax.validation.ValidationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.billsbars.app.AccessDeniedException;
import com.billsbars.app.model.BarOfSoap;
import com.billsbars.app.model.BarTypes;
import com.billsbars.app.model.BaseTypes;
import com.billsbars.app.model.MoldStyle;
import com.billsbars.app.model.ResponseModel;
import com.billsbars.app.service.BarOfSoapService;
import com.billsbars.app.service.UserAuthenticationService;

@RestController
public class SoapBarController {
	
	Logger logger = LoggerFactory.getLogger(SoapBarController.class);
	
	@Autowired
	private BarOfSoapService barOfSoapService;
	
	@Autowired
	private UserAuthenticationService userAuthenticationService;
	
	@PostMapping(value = "/soaps")
	ResponseEntity<ResponseModel> createASoap (
			@RequestHeader(value = "access-token", required = true) String r,
			@Valid @RequestBody BarOfSoap soap) {
		
		logger.info("Calling create soap");

		ResponseModel resp = new ResponseModel();

		if (!userAuthenticationService.isUserAdmin(r)) {
			throw new AccessDeniedException("access denied");
		}
		
		
		String type = soap.getBarType().toString();
		String butter = soap.getBaseType().toString();
		String mold = soap.getMoldStyle().toString();
		
		if ((type == null || type.trim().isEmpty()) ||
			(butter == null || butter.trim().isEmpty()) ||
			(mold == null || mold.trim().isEmpty())) {
			throw new ValidationException("invalid type or mold");
		}
					
		
		soap.setBarType(BarTypes.valueOf(type));
		soap.setBaseType(BaseTypes.valueOf(butter));
		soap.setMoldStyle(MoldStyle.valueOf(mold));
		
		
		if(barOfSoapService.createSoap(soap)) {
			resp.setMessage("Soap added");
			return ResponseEntity.status(HttpStatus.OK).body(resp);			
		}

		
		resp.setMessage("Error adding soap");
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(resp);
	}
	
	@PutMapping(value = "/soaps")
	ResponseEntity<ResponseModel> editASoap(
			@RequestHeader(value = "access-token", required = true) String r,
			@RequestBody BarOfSoap soap) {
		
		ResponseModel resp = new ResponseModel();

		if (!userAuthenticationService.isUserAdmin(r)) {
			throw new AccessDeniedException("access denied");
		}

		resp.setMessage("Not Implemented");
		
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	
	}
	
	@DeleteMapping(value = "/soaps")
	ResponseEntity<ResponseModel> deleteASoap(
			@RequestHeader(value = "access-token", required = true) String r,
			@Valid @RequestBody BarOfSoap soap) {
		
		ResponseModel resp = new ResponseModel();
		
		if (!userAuthenticationService.isUserAdmin(r)) {
			throw new AccessDeniedException("access denied");
		}

		if(barOfSoapService.deleteSoap(soap)) {
			resp.setMessage("Soap deleted");
			return ResponseEntity.status(HttpStatus.OK).body(resp);			
		}


		resp.setMessage("Not Implemented");
		
		return ResponseEntity.status(HttpStatus.I_AM_A_TEAPOT).body(resp);
	
	}
	
	@GetMapping(value = "/soaps")
	ResponseEntity<ResponseModel> getAllSoap() {
		
		ResponseModel resp = new ResponseModel();

		List<BarOfSoap> soapList = barOfSoapService.getAllSoaps();

		if (soapList != null) {
			resp.setMessage("list of soaps");
			ArrayList<BarOfSoap> sl = new ArrayList<BarOfSoap>();
			sl.addAll(soapList);
			resp.setListOfSoaps(sl);
		} else {
			resp.setMessage("None found");
		}
	
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	
	}

	@GetMapping(value = "/soaps/{soapId}")
	ResponseEntity<ResponseModel> getOneSoap(
			@RequestHeader(value = "access-token", required = true) String r) {
		
		ResponseModel resp = new ResponseModel();

		if (!userAuthenticationService.isUserAdmin(r)) {
			throw new AccessDeniedException("access denied");
		}

		resp.setMessage("Not Implemented");
		
		return ResponseEntity.status(HttpStatus.OK).body(resp);
	
	}
	
}
