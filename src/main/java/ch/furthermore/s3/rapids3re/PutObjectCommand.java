package ch.furthermore.s3.rapids3re;

import java.io.File;
import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("putObject")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class PutObjectCommand implements Command {
	@Value(value="${file}")
	private String file;
	
	@Value(value="${bucket}")
	private String bucket;
	
	@Value(value="${expirationDurationMinutes}")
	private int expirationDurationMinutes;
	
	@Autowired
	private S3 s3;
	
	@Override
	public void execute() throws IOException{
		File f = new File(file);
		System.out.println(s3.putObject(bucket, f.getName(), f, expirationDurationMinutes));
	}
}
