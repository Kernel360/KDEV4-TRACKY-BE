package kernel360trackybe.trackyhub.presentation.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import kernel360.trackycore.core.common.entity.vo.EmulatorInfo;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CycleInfoRequest {
	private String mdn;
	private EmulatorInfo emulatorInfo;

	@JsonProperty("cCnt") // JSON 필드명과 객체 필드명이 cCnt로 일치하는데 매핑이 안돼서 설정함(왜지?)
	private int cCnt;   // 주기 정보 개수

	@JsonFormat(pattern = "yyyyMMddHHmm")
	private LocalDateTime oTime;  // 발생시간

	// Jackson이 CycleData 객체로 JSON 형식의 데이터를 변환하는 과정에서 이름 매핑을 위해 필요함
	@JsonProperty("cList") //
	private List<CycleGpsRequest> cList;  // 주기정보 리스트
}
