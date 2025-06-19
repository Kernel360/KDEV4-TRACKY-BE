package kernel360trackybe.trackyhub.hub.application.service;

import java.util.List;
import java.util.UUID;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kernel360.trackycore.core.domain.entity.CarEntity;
import kernel360.trackycore.core.domain.provider.CarProvider;
import kernel360trackybe.trackyhub.car.domain.provider.CarDomainProvider;
import kernel360trackybe.trackyhub.common.config.RabbitMQProperties;
import kernel360trackybe.trackyhub.hub.application.dto.request.CarOnOffRequest;
import kernel360trackybe.trackyhub.hub.application.dto.request.CycleInfoRequest;
import kernel360trackybe.trackyhub.hub.application.dto.request.GpsHistoryMessage;
import kernel360trackybe.trackyhub.hub.application.dto.response.MdnBizResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class CarInfoProducerService {

	private final RabbitTemplate rabbitTemplate;
	private final RabbitMQProperties rabbitMQProperties;
	private final CarProvider carProvider;
	private final CarDomainProvider carHubDomainProvider;

	public void sendCarStart(CarOnOffRequest carOnOffRequest) {
		rabbitTemplate.convertAndSend(
			rabbitMQProperties.getExchange().getCarInfo(),
			rabbitMQProperties.getRouting().getOnKey(),
			carOnOffRequest
		);
		log.info("시동 ON 전송:{}", carOnOffRequest.toString());
	}

	public void sendCarStop(CarOnOffRequest carOnOffRequest) {
		rabbitTemplate.convertAndSend(
			rabbitMQProperties.getExchange().getCarInfo(),
			rabbitMQProperties.getRouting().getOffKey(),
			carOnOffRequest
		);
		log.info("시동 OFF 전송:{}", carOnOffRequest.toString());
	}

	public void sendCycleInfo(CycleInfoRequest carInfo) {
		GpsHistoryMessage gpsHistoryMessage = GpsHistoryMessage.from(carInfo.mdn(),
			carInfo.oTime(),
			carInfo.cCnt(), carInfo.cList());
		
		rabbitTemplate.convertAndSend(
			rabbitMQProperties.getExchange().getCycleInfo(),
			"",
			gpsHistoryMessage
		);
		// log.info("주기 정보 전송");
	}

	public String getToken() {
		return UUID.randomUUID().toString();
	}

	@Transactional(readOnly = true)
	public List<MdnBizResponse> getMdnAndBizId() {
		List<CarEntity> cars = carHubDomainProvider.findAllByAvailableRent();
		return cars.stream()
			.map(car -> MdnBizResponse.of(car.getMdn(), carProvider.findByMdn(car.getMdn()).getBiz().getId()))
			.toList();
	}
}
