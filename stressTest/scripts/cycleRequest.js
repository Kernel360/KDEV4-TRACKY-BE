// 기본 cList 데이터 생성 함수
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

// 공통으로 사용할 cList 데이터 생성
const commonCList = generateCList();

// 15000개의 requestBody 리스트 생성
export const requestBodyList = Array.from({ length: 15000 }, (_, index) => {
  // mdn은 01000000001부터 01000015000까지 증가
  const mdn = '0' + (1000000001 + index);
  
  return {
    "mdn": mdn,
    "tid": `TID${123456 + index}`,
    "mid": `MID${789 + index}`,
    "pv": "1.0",
    "did": `DEVICE${String(1 + index).padStart(6, '0')}`,
    "oTime": "202505081400",
    "cCnt": 60,
    "cList": commonCList
  };
});