package com.naga.gateway.controller;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.naga.gateway.dto.RunnerDTO;
import com.naga.gateway.dto.StatDTO;
import com.naga.gateway.entity.RunnerEntity;
import com.naga.gateway.entity.StatEntity;
import com.naga.gateway.repository.DeployRepository;
import com.naga.gateway.repository.StatRepository;

/**
 * 
 * This class handles REST request for cases, mainly querying for cases.
 * 
 * @author Dan Erez
 * 
 */
@RequestMapping("/stat")
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true")
public class StatController {

	private static final long MAX_NO_STAT_TIME = 1000*60;
	Logger logger = LoggerFactory.getLogger(this.getClass());
	Map<String, StatDTO> ip2stat = new ConcurrentHashMap<>();

	@Autowired
	StatRepository statRepository;
	
	@PostMapping("")
	public void stat(@RequestBody StatDTO stat, HttpServletRequest request) throws Exception {
		stat.setLastStatTime(System.currentTimeMillis());
		StatDTO lastStat = ip2stat.get(stat.getHostURL());
		if (lastStat != null) {
			stat.setLastInvokeTime(lastStat.getLastInvokeTime());
		} 
		ip2stat.put(stat.getHostURL(), stat);
		System.out.println("Got stat from " + stat.getHostURL());
		//TODO: send to Q for saving (go a-sync)
		statRepository.save(createEntity(stat));
		//TODO: run a scheduler to keep info 
		// for calculating busy hours in days of the week,
		// average busyness, reponse times, etc.
	}

	

	@GetMapping("/all")
	public Collection<StatDTO> getAllServers() throws Exception {
		return ip2stat.values();
	}
	
	public String getRunnerURL(String app, String func) {
		// TODO keep track of who ran this app/func lately,
		// who's the 'hottest' runner, etc.
		//CURRENTLY: return the least used one
		Entry<String, StatDTO> leastUsed = null;
		long now = System.currentTimeMillis();
		long lastUsedDiff = 0;
		for (Entry<String, StatDTO> es : ip2stat.entrySet()) {
			if (es.getValue().getLastInvokeTime() < 0) {
				// Never used - let's use it
				leastUsed = es;
				break;
			}
			long diff = now - es.getValue().getLastInvokeTime();
			if (diff > lastUsedDiff) {
				lastUsedDiff = diff;
				leastUsed = es;
			}
		};
		leastUsed.getValue().setLastInvokeTime(System.currentTimeMillis());
		return leastUsed.getKey();
	}

	@Scheduled(fixedDelay = 60000)
	public void removeDeadRunners() {
		// Go over the runners and remove whoever is too old
		List<String> ip2remove = new ArrayList<>();
		long now = System.currentTimeMillis();
		ip2stat.entrySet().forEach(e -> {
			if (now - e.getValue().getLastStatTime() > MAX_NO_STAT_TIME) {
				ip2remove.add(e.getKey());
			}
		});
		ip2remove.forEach(i -> {
			ip2stat.remove(i);
		});
	}
	
	private StatEntity createEntity(StatDTO stat) {
		StatEntity ent = new StatEntity();
		ent.setCpu(stat.getCpu());
		ent.setFreeMemory(stat.getFreeMemory());
		ent.setHostURL(stat.getHostURL());
		ent.setLastStatTime(stat.getLastStatTime());
		return ent;
	}
}