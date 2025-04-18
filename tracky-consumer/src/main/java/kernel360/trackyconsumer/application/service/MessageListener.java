package kernel360.trackyconsumer.application.service;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;

import kernel360.trackyconsumer.application.dto.CarOnOffRequest;
import kernel360.trackyconsumer.application.dto.GpsHistoryMessage;
import kernel360.trackyconsumer.config.RabbitMQConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class MessageListener {
	private final ConsumerService consumerService;

	@RabbitListener(queues = RabbitMQConfig.ONOFF_QUEUE_NAME)
	public void receiveCarOnOffMessage(@Payload CarOnOffRequest message,
		@Header("amqp_receivedRoutingKey") String routingKey) {

		log.info("메시지 수신: {}", message.toString());
		switch (routingKey) {
			case "on":
				consumerService.processOnMessage(message);
				break;
			case "off":
				consumerService.processOffMessage(message);
				break;
		}
	}

	// GPS 정보 처리 큐
	@RabbitListener(queues = RabbitMQConfig.GPS_QUEUE_NAME)
	public void receiveCarMessage(GpsHistoryMessage message) {
		consumerService.receiveCycleInfo(message);
	}
}
