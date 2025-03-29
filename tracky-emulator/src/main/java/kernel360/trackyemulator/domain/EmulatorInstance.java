package kernel360.trackyemulator.domain;

import lombok.Getter;

@Getter
public class EmulatorInstance {

	private final String mdn;    // 차량 식별 Key
	private final String tid;    // 터미널 아이디 - 'A001'로 고정
	private final String mid;    // 제조사 아이디 - CNSLink는 '6' 값 사용
	private final String pv;     // 패킷 버전 - '5' 고정
	private final String did;    // 디바이스 아이디 - GPS로만 운영함으로 '1'로 고정
	private final String gcd;    // GPS 상태 - 'A' 고정

	private String token;  // 단말 인증 토큰

	private int sum;             // 시동 ON 후 총 누적 거리

	private long cycleLastLat;    // 60초 주기 데이터 중 마지막 60번째 GPS 위도
	private long cycleLastLon;    // 60초 주기 데이터 중 마지막 60번째 GPS 경도
	private int cycleLastSpeed;    //60초 주기 데이터 중 마지막 속도
	private int cycleLastAng;        //60초 주기 데이터 중 마지막 방향

	private EmulatorInstance(String mdn, String tid, String mid, String pv, String did, String gcd) {
		this.mdn = mdn;
		this.tid = tid;
		this.mid = mid;
		this.pv = pv;
		this.did = did;
		this.gcd = gcd;
		this.sum = 0;
	}

	public static EmulatorInstance create(String mdn, String tid, String mid, String pv, String did, String gcd) {
		return new EmulatorInstance(mdn, tid, mid, pv, did, gcd);
	}

	public void setToken(String token) {
		this.token = token;
	}

	public void addSum(int nowSum) {
		this.sum += nowSum;    //시동 ON 후 누적 거리
	}

	public void setCycleLastInfo(long cycleLastLat, long cycleLastLon, int cycleLastSpeed, int cycleLastAng) {
		this.cycleLastLat = cycleLastLat;
		this.cycleLastLon = cycleLastLon;
		this.cycleLastSpeed = cycleLastSpeed;
		this.cycleLastAng = cycleLastAng;
	}

}