package kernel360trackybe.trackyhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
	"kernel360.trackycore.core.common",
})
public class TrackyHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackyHubApplication.class, args);
	}

}
