package com.naga.gateway.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.naga.gateway.entity.DeployEntity;
import com.naga.gateway.entity.RunnerEntity;

@Repository
public interface DeployRepository extends JpaRepository<DeployEntity, Long> {

}
