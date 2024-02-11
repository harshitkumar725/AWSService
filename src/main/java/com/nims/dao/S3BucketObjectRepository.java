package com.nims.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import com.nims.bean.S3BucketObject;

import java.util.List;

public interface S3BucketObjectRepository extends JpaRepository<S3BucketObject, Long> {

    int countByBucketName(String bucketName);

    List<S3BucketObject> findObjectNamesByBucketNameAndObjectNameContaining(String bucketName, String pattern);
}

