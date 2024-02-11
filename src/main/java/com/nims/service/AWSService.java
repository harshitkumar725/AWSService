package com.nims.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

//AWSService.java
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import com.nims.bean.DiscoverRequest;
import com.nims.bean.EC2Instance;
import com.nims.bean.S3Bucket;
import com.nims.bean.S3BucketObject;
import com.nims.dao.EC2InstanceRepository;
import com.nims.dao.S3BucketObjectRepository;
import com.nims.dao.S3BucketRepository;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.ec2.Ec2Client;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesRequest;
import software.amazon.awssdk.services.ec2.model.DescribeInstancesResponse;
import software.amazon.awssdk.services.neptunedata.model.S3Exception;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListBucketsResponse;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Response;

@Service
public class AWSService {

private final Map<String, JobStatus> jobStatusMap = new HashMap<>();

@Value("${aws.accessKeyId}")
private String accessKey;

@Value("${aws.secretKey}")
private String secretKey;

@Value("${aws.region}")
private String awsRegion;


 @Autowired
 private EC2InstanceRepository ec2InstanceRepository;

 @Autowired
 private S3BucketRepository s3BucketRepository;

 @Autowired
 private ThreadPoolTaskExecutor taskExecutor;
 
 @Autowired
 private S3BucketObjectRepository s3ObjRepository;
 
 public String discoverServices(DiscoverRequest discoverRequest) {
     String jobId = generateJobId();
     System.out.println("jobId: "+jobId);
     try {
     jobStatusMap.put(jobId, JobStatus.RUNNING);
     if(discoverRequest.getServices().contains("S3"))
    	 CompletableFuture.runAsync(() -> discoverEC2Instances(jobId), taskExecutor);
     if(discoverRequest.getServices().contains("EC2"))
    	 CompletableFuture.runAsync(() -> discoverS3Buckets(jobId), taskExecutor);
     jobStatusMap.put(jobId, JobStatus.COMPLETED);
     }catch(Exception e) {jobStatusMap.put(jobId, JobStatus.FAILED);}
     return jobId;
 }

 @Async
 public void discoverEC2Instances(String jobId) {
	 System.out.println("discoverEC2Instances is called.");
	 try (Ec2Client ec2 = Ec2Client.builder().region(Region.of(awsRegion))
             .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey)).build()) {
         DescribeInstancesResponse response = ec2.describeInstances(DescribeInstancesRequest.builder().build());
         System.out.println("response: "+response);
         response.reservations().forEach(reservation ->
                 reservation.instances().forEach(instance -> {
                     EC2Instance ec2Instance = new EC2Instance();
                     ec2Instance.setJobId(jobId);
                     ec2Instance.setInstanceId(instance.instanceId());
                     System.out.println(ec2Instance.toString());
                     ec2InstanceRepository.save(ec2Instance);
                 }));
     }catch (Exception e) {
    	 System.err.println("Error discovering EC2 instances: " + e.getMessage());
    	 jobStatusMap.put(jobId, JobStatus.FAILED);}
 }
 
 public List<String> getDiscoveryResult(String service) {
	 if(service.equalsIgnoreCase("S3"))
		 return s3BucketRepository.findAll().stream().map(obj->obj.getBucketName()).collect(Collectors.toList());
	 else
		 return ec2InstanceRepository.findAll().stream().map(obj->obj.getInstanceId()).collect(Collectors.toList());
 }

 @Async
 public void discoverS3Buckets(String jobId) {
	 System.out.println("discoverS3Buckets is called.");
	 try (S3Client s3 = S3Client.builder().region(Region.AP_SOUTH_1)
			 .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey)).build()) {
         ListBucketsResponse response = s3.listBuckets();
         System.out.println("response: "+response);
         response.buckets().forEach(bucket -> {
             S3Bucket s3Bucket = new S3Bucket();
             s3Bucket.setJobId(jobId);
             s3Bucket.setBucketName(bucket.name());
             System.out.println(s3Bucket.toString());
             s3BucketRepository.save(s3Bucket);
         });
     } catch (S3Exception e) {
         System.err.println("Error discovering S3 buckets: " + e.getMessage());
         jobStatusMap.put(jobId, JobStatus.FAILED);
     }
 }

 public String discoverS3BucketObjects(String bucketName) {
	 
	 String jobId = generateJobId();
	 discoverS3BucketObjects(bucketName,jobId);
	 return jobId;
 }
 
 @Async
 public String discoverS3BucketObjects(String bucketName,String jobId) {
	
     System.out.println("jobId: "+jobId);
     try (S3Client s3Client = S3Client.builder().region(Region.of(awsRegion))
             .credentialsProvider(() -> AwsBasicCredentials.create(accessKey, secretKey)).build()) {

         jobStatusMap.put(jobId, JobStatus.RUNNING);
         ListObjectsV2Request listObjectsV2Request = ListObjectsV2Request.builder()
                 .bucket(bucketName)
                 .build();

         ListObjectsV2Response listObjectsV2Response = s3Client.listObjectsV2(listObjectsV2Request);

         // Save the discovered S3 bucket objects in the database
         listObjectsV2Response.contents().forEach(s3Object -> {
             S3BucketObject s3BucketObject = new S3BucketObject();
             s3BucketObject.setJobId(jobId);
             s3BucketObject.setBucketName(bucketName);
             s3BucketObject.setObjectName(s3Object.key());
             System.out.println(bucketName+": "+s3Object.key());
             s3ObjRepository.save(s3BucketObject);
         });

         jobStatusMap.put(jobId, JobStatus.COMPLETED);
     }catch(Exception e) {jobStatusMap.put(jobId, JobStatus.FAILED);}
     return jobId;
 }

 public int getS3BucketObjectCount(String bucketName) {
     // Get the result from the DB
     return s3ObjRepository.countByBucketName(bucketName);
 }

 public List<String> getS3BucketObjectsLike(String bucketName, String pattern) {
     // Get the list of file names matching the pattern from the DB
     return s3ObjRepository.findObjectNamesByBucketNameAndObjectNameContaining(bucketName, pattern)
    		 .stream().map(obj->obj.getObjectName()).collect(Collectors.toList());
 }
 
 private String generateJobId() {
     // Implement logic to generate a unique job ID
     // You can use UUID.randomUUID() or any other method based on your requirements
     return "Job-" + System.currentTimeMillis();
 }

 public JobStatus getJobStatus(String jobId) {
     return jobStatusMap.getOrDefault(jobId, JobStatus.UNKNOWN);
 }

 public enum JobStatus {
     RUNNING, COMPLETED, UNKNOWN, FAILED
 }
 
}


