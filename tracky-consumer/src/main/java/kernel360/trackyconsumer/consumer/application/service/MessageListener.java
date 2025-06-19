package kernel360.trackyconsumer.consumer.application.service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import kernel360.trackyconsumer.consumer.application.dto.request.CarOnOffRequest;
import kernel360.trackyconsumer.consumer.application.dto.request.GpsHistoryMessage;
import kernel360.trackycore.core.domain.entity.GpsHistoryEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
//@RequiredArgsConstructor
@Slf4j
public class MessageListener {
	private final ConsumerService consumerService;
    private final Timer gpsProcessingTimer;

    // 생성자에서 MeterRegistry를 주입받아 Timer를 생성
    public MessageListener(ConsumerService consumerService, MeterRegistry meterRegistry) {
        this.consumerService = consumerService;
        // "gps.processing" 이라는 이름으로 타이머를 등록
        this.gpsProcessingTimer = Timer.builder("gps.processing")
                .description("GPS 메시지 처리 시간 및 개수")
                .tag("queue", "gps-queue") // 어떤 큐에 대한 메트릭인지 태그로 구분
                .register(meterRegistry);
    }
	
	@RabbitListener(queues = "on-off-Queue")
	public void receiveCarOnOffMessage(@Payload CarOnOffRequest message,
		@Header("amqp_receivedRoutingKey") String routingKey) {
		try {
			switch (routingKey) {
				case "onKey":
					consumerService.processOnMessage(message);
					break;
				case "offKey":
					consumerService.processOffMessage(message);
					break;
			}
		} catch (Exception e) {
			log.error("Error processing message: {}", e.getMessage());
		}
	}

	// GPS 정보 처리 큐
    @RabbitListener(queues = "gps-queue", containerFactory = "batchRabbitListenerContainerFactory")
    public void receiveCarMessages_bulk(List<GpsHistoryMessage> messages) {
        // Timer.record() 메소드는 실행 시간을 자동으로 측정해줍니다.
        gpsProcessingTimer.record(() -> {
			long start = System.currentTimeMillis();
            List<GpsHistoryEntity> gpses = new ArrayList<>();
            for (GpsHistoryMessage message : messages) {
                try {
                    gpses.addAll(consumerService.receiveCycleInfo_bulk(message));
                } catch (Exception e) {
                    log.error("GPS 메시지 처리 중 오류 발생: {}", e.getMessage());
                }
            }
            if (!gpses.isEmpty()) {
                consumerService.saveAllGps(gpses);
            }
			long end = System.currentTimeMillis(); // 종료 시간 측정
        	long duration = end - start; // 처리 시간 계산
            log.info("GPS 배치 처리 완료 | 메시지 개수: {} | 소요 시간: {}ms", gpses.size(), duration);
        });
    }

	// @RabbitListener(queues = "gps-queue", containerFactory = "batchRabbitListenerContainerFactory")
	// public void receiveCarMessages(GpsHistoryMessage message) {
	// 	log.info("GPS 메시지 수신");
	//
	// 	long start = System.currentTimeMillis();
	// 	try {
	// 		consumerService.receiveCycleInfo_bulk(message);
	// 	} catch (Exception e) {
	// 		log.error("GPS 메시지 처리 중 오류 발생: {}", e.getMessage());
	// 	}
	// 	long end = System.currentTimeMillis();
	//
	// 	log.info("GPS 메시지 처리 완료 | {}ms 소요", end - start);
	// }
}
