package kernel360trackybe.trackyhub;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
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
