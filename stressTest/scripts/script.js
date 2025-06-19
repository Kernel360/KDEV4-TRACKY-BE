import http from 'k6/http';
import {check} from 'k6';
import {requestBodyList} from './cycleRequest.js';

export const options = {
    // constant-vus: 지정한 VU(가상 사용자)를 동시에 유지
    scenarios: {
        load_test: {
            executor: 'per-vu-iterations',
            vus: 200,           // 100명의 가상 사용자
            iterations: 75,    // 각 VU당 150번 실행 - 총 1.5만번 요청
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

export default function () {
    const url = 'http://host.docker.internal:8082/hub/car/cycle';
    const tomcat = 'https://tracky-tomcat-922210846945.asia-northeast3.run.app/hub/car/cycle';
    const undertow = 'https://hub-undertow-922210846945.asia-northeast3.run.app/hub/car/cycle'
    const start = 'https://consumer-922210846945.asia-northeast3.run.app/consume/start'
    const stop = 'https://consumer-922210846945.asia-northeast3.run.app/consume/stop'
    const headers = {
        'Content-Type': 'application/json',
    };

    // 각 VU는 자신의 번호에 해당하는 요청만 처리
    // __VU는 1부터 시작하므로 -1을 해줌
    const index = (__VU - 1) % requestBodyList.length;
    const payload = JSON.stringify(requestBodyList[index]);
    const res = http.post(url, payload, {headers});
    check(res, {'status is 200': (r) => r.status === 200});

    // const startstop = http.post(start, payload, {headers});
    // check(startstop, {'status is 200': (r) => r.status === 200});
}
