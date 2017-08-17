package ch.furthermore.s3.rapids3re;

import java.io.IOException;

import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class ShareApplication {
	public static void main(String[] args) throws ParseException, NumberFormatException, IOException {
		Options options = new Options();
		options.addOption("command", true, "Command to perform (createBucket, deleteBucket or putObject)");
		options.addOption("region", true, "AWS region");
		options.addOption("accessKey", true, "AWS access key");
		options.addOption("secretKey", true, "AWS secret");
		options.addOption("bucket", true, "S3 bucket");
		options.addOption("expirationInDays", true, "S3 bucket expiration (in days)");
		options.addOption("file", true, "File to upload");
		options.addOption("expirationDurationMinutes", true, "Download link expiration (in minutes)");

		CommandLineParser parser = new BasicParser();
		CommandLine line = parser.parse( options, args );
		
		S3 s3 = new S3(line.getOptionValue("region"), line.getOptionValue("accessKey"), line.getOptionValue("secretKey"));
		
		String command = line.getOptionValue("command");
		
		if ("createBucket".equals(command)) {
			new CreateBucketCommand().execute(s3, line.getOptionValue("bucket"), line.getOptionValue("region"), Integer.parseInt(line.getOptionValue("expirationInDays")));
		}
		else if ("deleteBucket".equals(command)) {
			new DeleteBucketCommand().execute(s3, line.getOptionValue("bucket"));
		}
		else if ("putObject".equals(command)) {
			new PutObjectCommand().execute(s3, line.getOptionValue("file"), line.getOptionValue("bucket"), Integer.parseInt(line.getOptionValue("expirationDurationMinutes")));
		}
		else throw new IllegalArgumentException("unexpected: " + command); 
	}
}
