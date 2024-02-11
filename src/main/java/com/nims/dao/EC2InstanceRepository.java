package com.nims.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nims.bean.EC2Instance;


@Repository
public interface EC2InstanceRepository extends JpaRepository<EC2Instance, Long> {
	List<EC2Instance> findAllByJobId(String jobId);
}
