package com.naga.gateway.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class RunnerEntity {

	@Id
	@GeneratedValue
	private Long id;

	private String ip;

}
