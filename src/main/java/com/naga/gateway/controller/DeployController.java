package com.naga.gateway.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.naga.gateway.dto.DeployDTO;
import com.naga.gateway.entity.DeployEntity;
import com.naga.gateway.repository.DeployRepository;
import com.naga.gateway.utils.ImageResolver;
import com.naga.gateway.utils.StreamGobbler;

/**
 * 
 * This class handles REST request for cases, mainly querying for cases.
 * 
 * @author Dan Erez
 * 
 */
@RequestMapping("/deploy")
@RestController
@CrossOrigin(origins = "*",allowCredentials = "true")
public class DeployController {

	Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	ImageResolver imgResolver;
	@Autowired
	DeployRepository deployRepository;

	@PostMapping("/add")
	// Called by CLI
	public void add(@RequestBody DeployDTO deploy) throws Exception {
		deploy.setLang("Java");
		System.out.println("Deployed "+deploy);
		imgResolver.addImage(deploy);
		//
		deployRepository.save(createEntity(deploy));
	}
	

	@PostMapping("/addFunc")
	// Called by UI
	public void add( @RequestParam("file") MultipartFile file, @RequestParam("deployStr") String deployStr) throws Exception {
		DeployDTO deploy = new ObjectMapper().readValue(deployStr, DeployDTO.class);
		deploy.setLang("Java");
		// Currently we only support java, so expect a jar
		// Save it locally
		Path tempDirWithPrefix = Files.createTempDirectory("naga");
		File convFile = File.createTempFile(file.getName(), ".jar", tempDirWithPrefix.toFile()); 
	    FileOutputStream fos = new FileOutputStream(convFile); 
	    fos.write(file.getBytes());
	    fos.close(); 
	    // Create the docker file
	    File docker = new	 File(tempDirWithPrefix.toString()+"/Dockerfile");
	    PrintWriter pw = new PrintWriter(docker); 
	    pw.println(getDockerfileContent(convFile.getName(), deploy.getMainClass()));
	    pw.close(); 
	    // Use the naga cli to deploy
	    Process process = Runtime.getRuntime()
	    	      .exec(String.format("naga upload -d %s -f %s -a %s", 
	    	    		  tempDirWithPrefix.toString(),deploy.getFunc(),deploy.getApp()));
	    StreamGobbler streamGobbler = 
	    	 	  new StreamGobbler(process.getInputStream(), System.out::println);
	    		Executors.newSingleThreadExecutor().submit(streamGobbler);
	    int exitCode = process.waitFor();
	    System.out.println("Uploading status "+exitCode);
	    //TODO: Delete the local image
	    // delete the temp files
	    convFile.delete();
	    docker.delete();
	    tempDirWithPrefix.toFile().delete();
	}

	private String getDockerfileContent(String jarName, String mainClass) {
		return "FROM openjdk:15-jdk-alpine\r\n"+
		"ARG JAR_FILE="+jarName+"\r\n"+
		"COPY ${JAR_FILE} app.jar\r\n"+
		"ENTRYPOINT [\"java\",\"-cp\",\"/app.jar\",\""+mainClass+"\"]";
	}


	private DeployEntity createEntity(DeployDTO deploy) {
		DeployEntity ent = new DeployEntity();
		ent.setApp(deploy.getApp());
		ent.setDescription(deploy.getDescription());
		ent.setFunc(deploy.getFunc());
		ent.setImageName(deploy.getImageName());
		ent.setLang(deploy.getLang());
		ent.setMainClass(deploy.getMainClass());
		ent.setVersion(deploy.getVersion());
		return ent;
	}
}