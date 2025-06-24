import http from 'k6/http';
import {check} from 'k6';
// import {requestBodyList} from './cycleRequest.js'; // 이 줄은 더 이상 필요 없습니다.

export const options = {
    // constant-vus: 지정한 VU(가상 사용자)를 동시에 유지
    scenarios: {
        load_test: {
            executor: 'per-vu-iterations',
            vus: 100,           // 100명의 가상 사용자
            iterations: 150,    // 각 VU당 150번 실행 - 총 1.5만번 요청
            maxDuration: '600s',     // 최대 실행 시간
        },
    },
    // 시스템 메모리 제한 설정 (필요에 따라 조정)
    rps: 0, // 요청 속도 제한 해제
    thresholds: {
        // 실패율이 1% 초과 시 에러로 간주
        'http_req_failed': ['rate<0.01'],
        // 95% 요청 응답 시간이 500ms 이하
        'http_req_duration': ['p(95)<500'],
    },
};

// 기본 cList 데이터 생성 함수 - 필요하다면 이 함수를 default 함수 내부로 옮길 수도 있습니다.
// 현재는 모든 요청이 동일한 cList를 사용해도 괜찮다는 가정 하에 유지합니다.
function generateCList() {
    const cList = [];
    for (let i = 0; i < 60; i++) {
        const oTime = "202505081400" + String(i).padStart(2, '0'); // oTime 생성 (00~59)
        cList.push({
            "oTime": oTime,
            "gcd": "A",
            "lat": 37566500,
            "lon": 126978000,
            "ang": "90",
            "spd": "70",
            "sum": 70.0
        });
    }
    return cList;
}

const commonCList = generateCList(); // cList는 공통으로 사용

export default function () {
    const url = 'http://host.docker.internal:8082/hub/car/cycle';
    const url2 = 'http://34.64.202.93:8082/hub/car/cycle';
    const tomcat = 'https://tracky-tomcat-922210846945.asia-northeast3.run.app/hub/car/cycle';
    const undertow = 'https://hub-undertow-922210846945.asia-northeast3.run.app/hub/car/cycle'
    const start = 'https://consumer-922210846945.asia-northeast3.run.app/consume/start'
    const stop = 'https://consumer-922210846945.asia-northeast3.run.app/consume/stop'
    const headers = {
        'Content-Type': 'application/json',
    };

    // 각 요청마다 고유한 값 생성
    // __VU는 1부터 시작, __ITER는 0부터 시작
    const iterationsPerVU = 150; // 직접 값 지정
    const uniqueIndex = (__VU - 1) * iterationsPerVU + __ITER;
    const mdn = '0' + (1000000001 + uniqueIndex);
    const tid = `TID${123456 + uniqueIndex}`;
    const mid = `MID${789 + uniqueIndex}`;
    const did = `DEVICE${String(1 + uniqueIndex).padStart(6, '0')}`;
    // oTime은 필요에 따라 동적으로 생성하거나 고정 값을 사용할 수 있습니다.
    const oTime = "202505081400";


    const payload = JSON.stringify({
        "mdn": mdn,
        "tid": tid,
        "mid": mid,
        "pv": "1.0",
        "did": did,
        "oTime": oTime,
        "cCnt": 60,
        "cList": commonCList // 필요하다면 cList도 동적으로 생성
    });

    const res = http.post(url2, payload, {headers});
    check(res, {'status is 200': (r) => r.status === 200});

    // const startstop = http.post(start, payload, {headers});
    // check(startstop, {'status is 200': (r) => r.status === 200});
}