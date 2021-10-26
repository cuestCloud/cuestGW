package com.naga.gateway.controller;

import java.net.MalformedURLException;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.naga.gateway.dto.invokeDTO;
import com.naga.gateway.entity.InvokeEntity;
import com.naga.gateway.repository.DeployRepository;
import com.naga.gateway.repository.InvokeRepository;
import com.naga.gateway.utils.ImageResolver;

/**
 * 
 * This class handles REST request for cases, mainly querying for cases.
 * 
 * @author Dan Erez
 * 
 */
@RequestMapping("/invoke")
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true")
public class InvokeController {

	private static final String URL_SEP = "/";
	private static final String INVOKE_PREFIX = "/invoke/";
	private static final String BAD_URL_MSG = "Missing app or func name in URL, should be /invoke/app/func";

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ImageResolver imageResolver;
	@Autowired
	StatController runnerResolver;
	@Autowired
	RestTemplate rest;
	@Autowired
	InvokeRepository invokeRepository;
	
	
	@PostMapping("/**")
	public String invoke(@RequestBody String payload, HttpServletRequest request) throws Exception {
		logger.info("{} invoked with {}", request.getRequestURI(), payload);
		String app = getApp(request.getRequestURI()).toLowerCase();
		String func = getFunc(request.getRequestURI()).toLowerCase();
		// Builf the invocation request
		invokeDTO invocation = new invokeDTO(payload, imageResolver.getImageName(app, func));
		// log it
		invokeRepository.save(createEntity(invocation));
		//
		String url = "http://" + runnerResolver.getRunnerURL(app, func) + "/invoke";
		System.out.println("Sending invocation to " + url + " , img: " + invocation.getImageName());
		String res = rest.postForObject(url, invocation, String.class);
		//
		logger.info("Response for {} is {}", request.getRequestURI(), res);		
		return res;
	}

	

	private String getFunc(String requestURI) throws MalformedURLException {
		int idx = requestURI.indexOf(URL_SEP, INVOKE_PREFIX.length() + 1);
		if (idx < 0) {
			throw new MalformedURLException(BAD_URL_MSG);
		}
		return requestURI.substring(idx + 1);
	}

	private String getApp(String requestURI) throws MalformedURLException {
		int idx = requestURI.indexOf(URL_SEP, INVOKE_PREFIX.length() + 1);
		if (idx < 0) {
			throw new MalformedURLException(BAD_URL_MSG);
		}
		return requestURI.substring(INVOKE_PREFIX.length(), idx);
	}

	private InvokeEntity createEntity(invokeDTO invocation) {
		InvokeEntity ent = new InvokeEntity();
		ent.setImageName(invocation.getImageName());
		ent.setPayload(invocation.getPayload());
		return ent;
	}
}