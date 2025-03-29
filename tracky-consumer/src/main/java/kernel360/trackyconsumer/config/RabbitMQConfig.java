package kernel360.trackyconsumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE_NAME = "car-info-exchange";
	// public static final String QUEUE_NAME = "car-info-queue";
	public static final String GPS_QUEUE_NAME = "gps-queue";
	public static final String ONOFF_QUEUE_NAME = "onOffQueue";
	
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(EXCHANGE_NAME);
	}
	
	@Bean
	public Queue gpsQueue() {
		return new Queue(GPS_QUEUE_NAME, true);
	}
	
	@Bean
	public Queue onOffQueue() {
		return new Queue(ONOFF_QUEUE_NAME, true);
	}
	
	@Bean
	public Binding gpsBinding(Queue gpsQueue, TopicExchange exchange) {
		return BindingBuilder.bind(gpsQueue).to(exchange).with("gps");
	}
	
	@Bean
	public Binding onOffBinding(Queue onOffQueue, TopicExchange exchange) {
		return BindingBuilder.bind(onOffQueue).to(exchange).with("on");
	}
	
	@Bean
	public Binding offBinding(Queue onOffQueue, TopicExchange exchange) {
		return BindingBuilder.bind(onOffQueue).to(exchange).with("off");
	}

	// @Bean
	// public Queue queue() {
	// 	return new Queue(QUEUE_NAME, true);
	// }

	// @Bean
	// public Binding binding(Queue queue, TopicExchange exchange) {
	// 	return BindingBuilder.bind(queue).to(exchange).with("#");
	// }

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// ConnectionFactory는 Spring Boot에서 자동으로 생성해주므로 주입받아 사용한다.
	@Bean
	public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
		RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter());
		return rabbitTemplate;
	}
}