package kernel360.trackycore.core.common.entity.vo;

import lombok.Getter;

@Getter
public class EmulatorInfo {

	private final String tid;    // 터미널 아이디 - 'A001'로 고정
	private final String mid;    // 제조사 아이디 - CNSLink는 '6' 값 사용
	private final String pv;     // 패킷 버전 - '5' 고정
	private final String did;    // 디바이스 아이디 - GPS로만 운영함으로 '1'로 고정

	private EmulatorInfo(String tid, String mid, String pv, String did) {
		this.tid = tid;
		this.mid = mid;
		this.pv = pv;
		this.did = did;
	}

	public static EmulatorInfo create() {
		return new EmulatorInfo("A001", "6", "5", "1");
	}

	public static EmulatorInfo create(String tid, String mid, String pv, String did) {
		return new EmulatorInfo(tid, mid, pv, did);
	}
}
