package com.naga.gateway.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeployDTO {
	private String app, func, version, imageName, lang, description, mainClass;
}
