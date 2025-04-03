package kernel360.trackyweb.car.presentation;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import kernel360.trackycore.core.common.entity.CarEntity;
import kernel360.trackycore.core.common.entity.DeviceEntity;
import kernel360.trackyweb.car.application.mapper.CarMapper;
import kernel360.trackyweb.car.infrastructure.repository.CarRepository;
import kernel360.trackyweb.car.infrastructure.repository.DeviceRepository;
import kernel360.trackyweb.car.presentation.dto.CarCreateRequest;
import kernel360.trackyweb.car.presentation.dto.CarUpdateRequest;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class CarControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CarRepository carRepository;

	@Autowired
	private DeviceRepository deviceRepository;

	private CarCreateRequest carCreateRequest;
	private CarUpdateRequest carUpdateRequest;
	private DeviceEntity device;
	private CarEntity car;

	@BeforeEach
	void setUp() {
		device = deviceRepository.findById(1L).orElseThrow();
		carCreateRequest = new CarCreateRequest(
			"0123456789",
			1L,
			device,
			"SUV",
			"대전 12가 3456",
			"2020",
			"렌트카",
			"운행중",
			10000.0
		);
		carUpdateRequest = new CarUpdateRequest(
			1L,
			device,
			"세단",
			"대전 12가 3456",
			"2020",
			"렌트카",
			"운행중",
			10000.0
		);
		car = CarMapper.createCar(carCreateRequest, device);
	}

	@Test
	void getAll() throws Exception {
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/cars/all")
				.contentType(MediaType.APPLICATION_JSON)
		);
		MvcResult result = resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andReturn();
	}

	@Test
	void searchByMdn() {
	}

	@Test
	void searchOneByMdn() throws Exception {
		carRepository.save(car);

		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/cars/search/{mdn}", carCreateRequest.mdn())
				.param("mdn", carCreateRequest.mdn())
				.contentType(MediaType.APPLICATION_JSON)
		);
		MvcResult result = resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.mdn").value(carCreateRequest.mdn()))
			.andReturn();
	}

	@Test
	void searchOneDetailByMdn() throws Exception {
		deviceRepository.save(device);
		carRepository.save(car);

		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/cars/search/{mdn}/detail", carCreateRequest.mdn())
				.param("mdn", carCreateRequest.mdn())
				.contentType(MediaType.APPLICATION_JSON)
		);
		MvcResult result = resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.mdn").value(carCreateRequest.mdn()))
			.andReturn();
	}

	@Test
	void create() throws Exception {
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.post("/api/cars/create")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(carCreateRequest))
		);
		MvcResult result = resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.mdn").value(carCreateRequest.mdn()))
			.andReturn();
	}

	@Test
	void update() throws Exception {
		carRepository.save(car);
		
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.patch("/api/cars/update/{mdn}", carCreateRequest.mdn())
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(carUpdateRequest))
		);
		MvcResult result = resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.carType").value(carUpdateRequest.carType()))
			.andReturn();
	}

	@Test
	void delete() throws Exception {
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.delete("/api/cars/delete/{mdn}", carCreateRequest.mdn())
				.contentType(MediaType.APPLICATION_JSON)
		);
		MvcResult result = resultActions
			.andExpect(status().isOk())
			.andReturn();
	}
}