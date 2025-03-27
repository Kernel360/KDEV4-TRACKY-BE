package kernel360trackybe.trackyhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@EntityScan(basePackages = {
    "kernel360trackybe.trackycore.core.infrastructure"
})
@EnableJpaRepositories(basePackages = {
    "kernel360.trackycore.core.infrastructure.repository"
})
public class TrackyHubApplication {

	public static void main(String[] args) {
		SpringApplication.run(TrackyHubApplication.class, args);
	}
	@RestController
	public static class HelloController {

		@GetMapping("/")
		public String hello() {
			return "안녕-hubserver-jeenee-테스트중!!!!!";
		}
	}

}
