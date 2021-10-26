package com.naga.gateway.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class DeployEntity {

	@Id
	@GeneratedValue
	private Long id;

	private String app, func, version, imageName, lang, description, mainClass;

}
