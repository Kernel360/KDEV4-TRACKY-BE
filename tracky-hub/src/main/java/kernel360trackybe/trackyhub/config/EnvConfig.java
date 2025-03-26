package kernel360trackybe.trackyhub.config;


import org.springframework.context.annotation.Configuration;


import io.github.cdimascio.dotenv.Dotenv;
import jakarta.annotation.PostConstruct;

@Configuration
public class EnvConfig {
	@PostConstruct
	public void loadEnv() {
		Dotenv dotenv = Dotenv.configure()
			.directory("./") // 루트에 있는 경우
			.filename(".env")
			.load();
		System.setProperty("DB_HOST", dotenv.get("DB_HOST"));
		System.setProperty("DB_NAME", dotenv.get("DB_NAME"));
		System.setProperty("DB_USER", dotenv.get("DB_USER"));
		System.setProperty("DB_PASSWORD", dotenv.get("DB_PASSWORD"));

		// RabbitMQ
        System.setProperty("RABBITMQ_HOST", dotenv.get("RABBITMQ_HOST"));
        System.setProperty("RABBITMQ_PORT", dotenv.get("RABBITMQ_PORT"));
        System.setProperty("RABBITMQ_USER", dotenv.get("RABBITMQ_USER"));
        System.setProperty("RABBITMQ_PASSWORD", dotenv.get("RABBITMQ_PASSWORD"));
	}
}
