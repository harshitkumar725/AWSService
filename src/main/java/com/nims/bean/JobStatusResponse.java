package com.nims.bean;

import com.nims.service.AWSService;

//JobStatusResponse.java
public class JobStatusResponse {

    private String jobId;
    private AWSService.JobStatus status;
 
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public AWSService.JobStatus getStatus() {
		return status;
	}
	public void setStatus(AWSService.JobStatus status) {
		this.status = status;
	}

 // Getters and setters
}