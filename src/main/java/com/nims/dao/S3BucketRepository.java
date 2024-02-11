package com.nims.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.nims.bean.S3Bucket;

@Repository
public interface S3BucketRepository extends JpaRepository<S3Bucket, Long> {
	List<S3Bucket> findAllByJobId(String jobId);
}