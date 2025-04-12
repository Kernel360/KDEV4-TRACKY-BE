package kernel360.trackyconsumer.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

	public static final String EXCHANGE_NAME = "car-info-exchange";
	// public static final String QUEUE_NAME = "car-info-queue";
	public static final String GPS_QUEUE_NAME = "gps-queue";
	public static final String ONOFF_QUEUE_NAME = "on-off-Queue";
	
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
	public Binding onBinding(Queue onOffQueue, TopicExchange exchange) {
		return BindingBuilder.bind(onOffQueue).to(exchange).with("on");
	}
	
	@Bean
	public Binding offBinding(Queue onOffQueue, TopicExchange exchange) {
		return BindingBuilder.bind(onOffQueue).to(exchange).with("off");
	}

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}