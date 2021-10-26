package com.naga.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naga.gateway.entity.RunnerEntity;
import com.naga.gateway.entity.StatEntity;

@Repository
public interface StatRepository extends JpaRepository<StatEntity, Long> {
	
}
