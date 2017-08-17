package ch.furthermore.s3.rapids3re;

import java.io.IOException;

public class CreateBucketCommand {
	public void execute(S3 s3, String bucket, String region, int expirationInDays) throws IOException {
		s3.createBucket(bucket, region, expirationInDays);
	}
}
