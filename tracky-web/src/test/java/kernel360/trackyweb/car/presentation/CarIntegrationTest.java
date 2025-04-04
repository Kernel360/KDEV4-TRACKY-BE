package kernel360.trackyweb.car.presentation;

import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.*;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.*;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
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
@AutoConfigureRestDocs
@Transactional
class CarIntegrationTest {

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
			.andDo(
				document("get-all",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					relaxedResponseFields(
						fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
						fieldWithPath("data").type(JsonFieldType.ARRAY).description("차량 목록")
					)
				)
			)
			.andDo(
				document("car-fields",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					relaxedResponseFields(
						fieldWithPath("data[].mdn").type(JsonFieldType.STRING).description("차량 식별 번호"),
						fieldWithPath("data[].bizId").type(JsonFieldType.NUMBER).description("업체 ID"),
						fieldWithPath("data[].carType").type(JsonFieldType.STRING).description("차량 종류 (예: 아반떼, 소나타 등)"),
						fieldWithPath("data[].carPlate").type(JsonFieldType.STRING).description("차량 번호판"),
						fieldWithPath("data[].carYear").type(JsonFieldType.STRING).description("차량 연식"),
						fieldWithPath("data[].purpose").type(JsonFieldType.STRING).description("차량 용도 (예: 법인, 렌트카 등)"),
						fieldWithPath("data[].status").type(JsonFieldType.STRING)
							.description("차량 운행 상태 (예: 운행중, 정비중, 폐차 등)"),
						fieldWithPath("data[].sum").type(JsonFieldType.NUMBER).description("누적 주행 거리"),
						fieldWithPath("data[].createdAt").type(JsonFieldType.STRING).description("생성 일시 (ISO 8601 형식)")
					)
				)
			)
			.andReturn();
	}

	@Test
	void searchByMdn() throws Exception {
		carRepository.save(car);
		ResultActions resultActions = mockMvc.perform(
			MockMvcRequestBuilders.get("/api/cars/search")
				.param("mdn", carCreateRequest.mdn())
				.contentType(MediaType.APPLICATION_JSON)
		);
		MvcResult result = resultActions
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").isArray())
			.andDo(
				document("search-by-mdn",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					queryParameters(
						parameterWithName("mdn").description("검색할 차량의 mdn (모바일 디바이스 번호)")
					),
					relaxedResponseFields(
						fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
						fieldWithPath("data").type(JsonFieldType.ARRAY).description("차량 목록")
					)
				)
			)
			.andReturn();
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
			.andDo(
				document("search-one-by-mdn",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					pathParameters(
						parameterWithName("mdn").description("차량 식별 번호")
					),
					relaxedResponseFields(
						fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
						fieldWithPath("data").type(JsonFieldType.OBJECT).description("차량 정보")
					)
				)
			)
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
			.andDo(
				document("create",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					relaxedRequestFields(
						fieldWithPath("mdn").type(JsonFieldType.STRING).description("차량 식별 번호"),
						fieldWithPath("bizId").type(JsonFieldType.NUMBER).description("업체 ID"),
						fieldWithPath("device").type(JsonFieldType.OBJECT).description("디바이스 정보"),
						fieldWithPath("carType").type(JsonFieldType.STRING).description("차량 종류 (예: 아반떼, 소나타 등)"),
						fieldWithPath("carPlate").type(JsonFieldType.STRING).description("차량 번호판"),
						fieldWithPath("carYear").type(JsonFieldType.STRING).description("차량 연식"),
						fieldWithPath("purpose").type(JsonFieldType.STRING).description("차량 용도 (예: 법인, 렌트카 등)"),
						fieldWithPath("status").type(JsonFieldType.STRING)
							.description("차량 운행 상태 (예: 운행중, 정비중, 폐차 등)"),
						fieldWithPath("sum").type(JsonFieldType.NUMBER).description("누적 주행 거리")
					),
					relaxedResponseFields(
						fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
						fieldWithPath("data").type(JsonFieldType.OBJECT).description("차량 정보")
					)
				)
			)
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
			.andDo(
				document("update",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					relaxedRequestFields(
						fieldWithPath("bizId").type(JsonFieldType.NUMBER).description("업체 ID"),
						fieldWithPath("device").type(JsonFieldType.OBJECT).description("디바이스 정보"),
						fieldWithPath("carType").type(JsonFieldType.STRING).description("차량 종류 (예: 아반떼, 소나타 등)"),
						fieldWithPath("carPlate").type(JsonFieldType.STRING).description("차량 번호판"),
						fieldWithPath("carYear").type(JsonFieldType.STRING).description("차량 연식"),
						fieldWithPath("purpose").type(JsonFieldType.STRING).description("차량 용도 (예: 법인, 렌트카 등)"),
						fieldWithPath("status").type(JsonFieldType.STRING)
							.description("차량 운행 상태 (예: 운행중, 정비중, 폐차 등)"),
						fieldWithPath("sum").type(JsonFieldType.NUMBER).description("누적 주행 거리")
					),
					relaxedResponseFields(
						fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
						fieldWithPath("data").type(JsonFieldType.OBJECT).description("차량 정보")
					)
				)
			)
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
			.andDo(
				document("update",
					preprocessRequest(prettyPrint()),
					preprocessResponse(prettyPrint()),
					pathParameters(
						parameterWithName("mdn").description("차량 식별 번호")
					),
					relaxedResponseFields(
						fieldWithPath("status").type(JsonFieldType.NUMBER).description("응답 코드"),
						fieldWithPath("message").type(JsonFieldType.STRING).description("응답 메시지"),
						fieldWithPath("data").type(JsonFieldType.STRING).description("삭제 성공 메시지")
					)
				)
			)
			.andReturn();
	}
}