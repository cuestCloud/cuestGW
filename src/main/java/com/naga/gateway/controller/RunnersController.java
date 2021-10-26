package com.naga.gateway.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naga.gateway.dto.RunnerDTO;
import com.naga.gateway.entity.RunnerEntity;
import com.naga.gateway.repository.RunnerRepository;

/**
 * 
 * This class handles REST request for cases, mainly querying for cases.
 * 
 * @author Dan Erez
 * 
 */
@RequestMapping("/servers")
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true")
public class RunnersController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	RunnerRepository runnerRepository;

	@GetMapping("/all")
	public List<RunnerDTO> getAllRunners() throws Exception {
		List<RunnerEntity> runners = runnerRepository.findAll();
		List<RunnerDTO> res = new ArrayList<>();
		if (runners != null) {
			runners.forEach(r -> {
				res.add(new RunnerDTO(r.getIp()));
			});
		}
		return res;
	}

	@PostMapping("/add")
	public Long createRunner(@RequestBody String ip) throws Exception {
		RunnerEntity runner = new RunnerEntity();
		runner.setIp(ip);
		runner = runnerRepository.save(runner);
		return runner.getId();
	}

}