**Introduction**
Nimesa API provides functionality to discover and interact with AWS services such as EC2 and S3. The API allows you to discover services, check job status, get discovery results, and perform operations on S3 buckets.

**API Endpoints**

1. Discover Services
Endpoint: /awsscan-service/discover
Method: POST
Description: Discover EC2 instances, S3 buckets, or both based on the provided services list.
Request Example:
Copy code
{
  "services": ["EC2", "S3"]
}
Response: Returns a job ID for asynchronous processing.

3. Job Status
Endpoint: /awsscan-service/jobStatus
Method: GET
Description: Get the status of a job based on the provided job ID.
Request Example:
Query Parameter: jobId=Job-1707652896270
Response: Returns the job status.

4. Discovery Result
Endpoint: /awsscan-service/discoveryResult
Method: GET
Description: Get the result of a discovery for a specific service (EC2 or S3).
Request Example:
Query Parameter: service=S3
Response: Returns the result of the discovery.
5. Get S3 Bucket Objects
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

6. Get S3 Bucket Object Count
Endpoint: /awsscan-service/getS3BucketObjectCount
Method: GET
Description: Get the count of S3 bucket objects based on the provided bucket name.
Request Example:
Query Parameter: bucketName=nimesaassignmentbucket1
Response: Returns the count of S3 bucket objects.

7. Get S3 Bucket Objects Like
Endpoint: /awsscan-service/getS3BucketObjectsLike
Method: GET
Description: Get S3 bucket objects that match a specified pattern based on the provided bucket name.
Request Example:
Query Parameters: bucketName=nimesaassignmentbucket1, pattern=Test-File
Response: Returns a list of S3 bucket objects.


**DB Configurations:**
Secure MySQL Database Configuration
This project utilizes a MySQL database named AWSService, with a user named nimesa and the password set to password123 for demonstration purposes. To enhance security, it is recommended to modify these details. Follow the steps below to ensure a more secure configuration:

Database Configuration
Database Name:

Change the database name (AWSService) to a more secure and specific name.
Database User:

Change the database user (nimesa) to a more secure and specific username.
Grant Permissions:

Grant necessary permissions to the user for the new database.
Update application.properties
Update the application.properties file with the new database details:

# DataSource settings
spring.datasource.url=jdbc:mysql://localhost:3306/your_secure_db_name?useSSL=false
spring.datasource.username=your_secure_user
spring.datasource.password=your_secure_password

# Hibernate settings
spring.jpa.show-sql=true
spring.jpa.hibernate.ddl-auto=update
Security Considerations
Use strong and unique passwords.
Limit database user permissions to the minimum required.
Consider using environment variables for sensitive information.
Restart Application
After making these changes, restart your application to apply the new database configuration.




