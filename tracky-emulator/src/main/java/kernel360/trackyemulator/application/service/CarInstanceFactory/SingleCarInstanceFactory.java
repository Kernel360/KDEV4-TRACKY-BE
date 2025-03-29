package kernel360.trackyemulator.application.service.CarInstanceFactory;


import org.springframework.stereotype.Component;

import kernel360.trackyemulator.domain.EmulatorInstance;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SingleCarInstanceFactory {

	public EmulatorInstance createCarInstances() {
		String mdn = "01012345678";

		//EmulatorInstance 생성
		EmulatorInstance car = EmulatorInstance.create(mdn, "A001", "6", "5", "1", "A");

		return car;
	}

}
