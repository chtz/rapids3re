package ch.furthermore.s3.rapids3re;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

@Service("deleteBucket")
@Scope(value=ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class DeleteBucketCommand implements Command {
	@Value(value="${bucket}")
	private String bucket;
	
	@Autowired
	private S3 s3;

	@Override
	public void execute() throws IOException {
		s3.deleteBucket(bucket);
	}
}