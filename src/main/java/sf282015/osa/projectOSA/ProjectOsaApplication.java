package sf282015.osa.projectOSA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ProjectOsaApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProjectOsaApplication.class, args);
	}
}
