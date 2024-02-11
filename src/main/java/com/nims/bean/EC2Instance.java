package com.nims.bean;

import javax.persistence.Column;
//EC2Instance.java
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class EC2Instance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
    private String jobId;
    @Column(unique=true)
	private String instanceId;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getJobId() {
		return jobId;
	}
	public void setJobId(String jobId) {
		this.jobId = jobId;
	}
	public String getInstanceId() {
		return instanceId;
	}
	public void setInstanceId(String instanceId) {
		this.instanceId = instanceId;
	}
	@Override
	public String toString() {
		return "EC2Instance [id=" + id + ", jobId=" + jobId + ", instanceId=" + instanceId + "]";
	}
}
