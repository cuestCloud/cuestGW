package com.naga.gateway.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naga.gateway.dto.DeployDTO;
import com.naga.gateway.utils.ImageResolver;

/**
 * 
 * This class handles REST request for cases, mainly querying for cases.
 * 
 * @author Dan Erez
 * 
 */
@RequestMapping("/funcs")
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true")
public class FuncsController {

	Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Autowired
	ImageResolver imgResolver;

	
	@GetMapping("/all")
	public List<DeployDTO> getAllFuncs() throws Exception {
		return imgResolver.getAllFuncs();
	}

	

}