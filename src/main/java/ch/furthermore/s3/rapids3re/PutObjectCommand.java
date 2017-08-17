package ch.furthermore.s3.rapids3re;

import java.io.File;
import java.io.IOException;

public class PutObjectCommand {
	public void execute(S3 s3, String file, String bucket, int expirationDurationMinutes) throws IOException{
		File f = new File(file);
		System.out.println(s3.putObject(bucket, f.getName(), f, expirationDurationMinutes).toExternalForm());
	}
}
