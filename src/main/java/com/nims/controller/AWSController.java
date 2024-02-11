package com.nims.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.nims.bean.DiscoverRequest;
import com.nims.bean.JobStatusResponse;
import com.nims.service.AWSService;

@RestController
public class AWSController {
	
	@Autowired
	AWSService awsService;	

    @PostMapping("/discover")
    public String discoverServices(@RequestBody DiscoverRequest discoverRequest) {
        return awsService.discoverServices(discoverRequest);
    }

    @GetMapping("/jobStatus")
    public JobStatusResponse getJobStatus(@RequestParam String jobId) {
        AWSService.JobStatus status = awsService.getJobStatus(jobId);

        JobStatusResponse response = new JobStatusResponse();
        response.setJobId(jobId);
        response.setStatus(status);

        return response;
    }

    @GetMapping("/discoveryResult")
    public Object getDiscoveryResult(@RequestParam String service) {
        return awsService.getDiscoveryResult(service);
    }
    
    @PostMapping("/getS3BucketObjects")
    public String getS3BucketObjects(@RequestParam String bucketName) {
        return awsService.discoverS3BucketObjects(bucketName);
    }

    @GetMapping("/getS3BucketObjectCount")
    public int getS3BucketObjectCount(@RequestParam String bucketName) {
        return awsService.getS3BucketObjectCount(bucketName);
    }

    @GetMapping("/getS3BucketObjectsLike")
    public List<String> getS3BucketObjectsLike(@RequestParam String bucketName, @RequestParam String pattern) {
        return awsService.getS3BucketObjectsLike(bucketName, pattern);
    }
}
