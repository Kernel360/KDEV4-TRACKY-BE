package kernel360.trackyweb;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@SpringBootApplication
@EntityScan(basePackages = {
	"kernel360trackybe.trackycore.core.infrastructure",
})
public class TrackyWebApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackyWebApplication.class, args);
	}
}
