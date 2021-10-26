package com.naga.gateway.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.naga.gateway.dto.DeployDTO;

@Component
public class ImageResolver {

	private Map<String, String> appFunc2imageName = new HashMap<>();
    private List<DeployDTO> funcs = new ArrayList<>();
    
	public String getImageName(String app, String func) {
		return appFunc2imageName.get(app.toLowerCase() + "_" + func.toLowerCase());
	}

	public void addImage(DeployDTO deploy) {
		appFunc2imageName.put(deploy.getApp().toLowerCase() + "_" + deploy.getFunc().toLowerCase(), deploy.getImageName());
		funcs.add(deploy);
	}

	public List<DeployDTO> getAllFuncs() {
		return funcs;
	}

}
