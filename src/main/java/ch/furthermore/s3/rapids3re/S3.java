package ch.furthermore.s3.rapids3re;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.HttpMethod;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.BucketLifecycleConfiguration;
import com.amazonaws.services.s3.model.CreateBucketRequest;
import com.amazonaws.services.s3.model.DeleteBucketRequest;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ListObjectsRequest;
import com.amazonaws.services.s3.model.ObjectListing;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3ObjectSummary;
import com.amazonaws.services.s3.model.SetBucketLifecycleConfigurationRequest;

@Service
public class S3 {
	@Value(value = "${region:eu-west-1}")
	private String region;

	@Value(value = "${accessKey}")
	private String accessKey;

	@Value(value = "${secretKey}")
	private String secretKey;

	private AmazonS3 s3;

	@PostConstruct
	public void init() {
		s3 = AmazonS3ClientBuilder.standard()
				.withCredentials(new AWSStaticCredentialsProvider(new BasicAWSCredentials(accessKey, secretKey)))
				.build();
	}

	public URL putObject(String bucketName, String key, File file, int expirationDurationMinutes) throws IOException {
		Calendar expiration = Calendar.getInstance();
		expiration.add(Calendar.MINUTE, expirationDurationMinutes);

		Map<String, String> userMetadata = new HashMap<String, String>();
		userMetadata.put("expiration", Long.toString(expiration.getTimeInMillis()));

		ObjectMetadata metadata = new ObjectMetadata();
		metadata.setContentLength(file.length());
		metadata.setContentType(Files.probeContentType(file.toPath()));

		InputStream in = new BufferedInputStream(new FileInputStream(file));
		try {
			PutObjectRequest request = new PutObjectRequest(bucketName, key, in, metadata);

			s3.putObject(request);

			GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, key);
			generatePresignedUrlRequest.setMethod(HttpMethod.GET);
			generatePresignedUrlRequest.setExpiration(expiration.getTime());

			return s3.generatePresignedUrl(generatePresignedUrlRequest);
		} finally {
			in.close();
		}
	}

	public void createBucket(String bucketName, int expirationInDays) {
		s3.createBucket(new CreateBucketRequest(bucketName, region));
		
		BucketLifecycleConfiguration.Rule globalExpNDaysRule = new BucketLifecycleConfiguration.Rule()
        		.withId("GlobalExp" + expirationInDays + "Days")
        		.withExpirationInDays(expirationInDays)
        		.withStatus(BucketLifecycleConfiguration.ENABLED.toString());
		
		s3.setBucketLifecycleConfiguration(new SetBucketLifecycleConfigurationRequest(bucketName, 
				new BucketLifecycleConfiguration(Arrays.asList(globalExpNDaysRule))));
	}

	public void deleteBucket(String bucketName) {
		ListObjectsRequest request = new ListObjectsRequest().withBucketName(bucketName);
		ObjectListing listing = s3.listObjects(request);
		for (;;) {
			for (S3ObjectSummary summary : listing.getObjectSummaries()) {
				s3.deleteObject(new DeleteObjectRequest(bucketName, summary.getKey()));
			}

			if (listing.isTruncated()) {
				listing = s3.listNextBatchOfObjects(listing);
			} else {
				break;
			}
		}

		s3.deleteBucket(new DeleteBucketRequest(bucketName));
	}
}
