package com.naga.gateway.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@Configuration
@EnableJpaRepositories(entityManagerFactoryRef = "entityManagerFactory", basePackages = {
		"com.naga.gateway.repository" })

@EntityScan(basePackages = { "com.naga.gateway.entity" })
@EnableTransactionManagement
public class PersistenceConfig {

}