package ch.furthermore.s3.rapids3re;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;

@SpringBootApplication
public class ShareApplication implements CommandLineRunner {
	public static void main(String[] args) {
		SpringApplication.run(ShareApplication.class, args);
	}

	@Value(value="${command}")
	private String command;
	
	@Autowired
	private ApplicationContext ctx;
	
	public void run(String... args) throws Exception {
		((Command) ctx.getBean(command)).execute();
	}
}
