package ch.furthermore.s3.rapids3re;

import java.io.IOException;

public class DeleteBucketCommand {
	public void execute(S3 s3, String bucket) throws IOException {
		s3.deleteBucket(bucket);
	}
}
