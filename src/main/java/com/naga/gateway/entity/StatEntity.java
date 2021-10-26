package com.naga.gateway.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import lombok.Data;

@Entity
@Data
public class StatEntity {

	@Id
	@GeneratedValue
	private Long id;

	private int cpu;
	private long freeMemory, lastStatTime;
	private String hostURL;

}
