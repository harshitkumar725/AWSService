Nimesa API Documentation
Introduction
Nimesa API provides functionality to discover and interact with AWS services such as EC2 and S3. The API allows you to discover services, check job status, get discovery results, and perform operations on S3 buckets.

API Endpoints
1. Discover Services
Endpoint: /discover
Method: POST
Description: Discover EC2 instances, S3 buckets, or both based on the provided services list.
Request Example:
json
Copy code
{
  "services": ["EC2", "S3"]
}
Response: Returns a job ID for asynchronous processing.
2. Job Status
Endpoint: /jobStatus
Method: GET
Description: Get the status of a job based on the provided job ID.
Request Example:
Query Parameter: jobId=Job-1707652896270
Response: Returns the job status.
3. Discovery Result
Endpoint: /discoveryResult
Method: GET
Description: Get the result of a discovery for a specific service (EC2 or S3).
Request Example:
Query Parameter: service=S3
Response: Returns the result of the discovery.
4. Get S3 Bucket Objects
Endpoint: /getS3BucketObjects
Method: POST
Description: Get S3 bucket objects based on the provided bucket name.
Request Example:
json
Copy code
{
  "services": ["EC2", "S3"]
}
Response: Returns a job ID for asynchronous processing.
5. Get S3 Bucket Object Count
Endpoint: /getS3BucketObjectCount
Method: GET
Description: Get the count of S3 bucket objects based on the provided bucket name.
Request Example:
Query Parameter: bucketName=nimesaassignmentbucket1
Response: Returns the count of S3 bucket objects.
6. Get S3 Bucket Objects Like
Endpoint: /getS3BucketObjectsLike
Method: GET
Description: Get S3 bucket objects that match a specified pattern based on the provided bucket name.
Request Example:
Query Parameters: bucketName=nimesaassignmentbucket1, pattern=Test-File
Response: Returns a list of S3 bucket objects.

