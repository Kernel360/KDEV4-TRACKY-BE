package kernel360.trackyweb.common.config;

import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class RabbitMQConfig {

	private final RabbitMQProperties properties;

	@Bean
	public Jackson2JsonMessageConverter messageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	@Bean
	public SimpleRabbitListenerContainerFactory batchRabbitListenerContainerFactory(
		ConnectionFactory connectionFactory) {
		SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
		factory.setConnectionFactory(connectionFactory);
		factory.setBatchSize(properties.getBatch().getSize());
		factory.setReceiveTimeout(properties.getBatch().getTimeout());
		factory.setBatchListener(properties.getBatch().isEnabled());
		factory.setConsumerBatchEnabled(properties.getBatch().isConsumerBatchEnabled());
		factory.setMessageConverter(messageConverter());

		factory.setErrorHandler(throwable -> {
			log.error("배치 처리 중 오류 발생: {}", throwable.getMessage());
		});

		factory.setDefaultRequeueRejected(properties.getBatch().isDefaultRequeueRejected());

		return factory;
	}
}
