package com.naga.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StatDTO {
	private int cpu;
	private long freeMemory, lastStatTime, lastInvokeTime = Long.MIN_VALUE;
	private String hostURL;
}
