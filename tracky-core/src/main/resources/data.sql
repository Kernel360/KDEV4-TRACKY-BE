INSERT INTO biz(biz_name, biz_reg_num, biz_admin, biz_phone_num, created_at)
VALUES('jiwon_company', '012-34-56789', '구지원', '010-1234-5678', '2025-04-13 09:00:00');

INSERT INTO member(biz_id, member_id, pwd, email, role, created_at)
VALUES(1, 'jiwon0321', '$2a$10$0EOkcArMlrYPeHSBrrLVAetH2OdLfdEYlD1a5WA1.80RBI1Z8x7Ii', 'jiwon0321@gmail.com', 'ADMIN', '2025-04-14 09:00:00');

INSERT INTO device(id, tid, mid, did, pv, created_at)
VALUES(1, 'A001', '6', '1', '5', '2025-04-14 09:00:00');

INSERT INTO car(mdn, biz_id, device_id, car_type, car_plate, car_year, status, purpose, sum, created_at)
VALUES
 ('0000000000', '1',  '1', '소나타',   '서울 01가 1001', '2021', 'running',  '렌트',     15000, '2025-04-14 09:00:00'),
  ('1111111111', '1',  '1', '그랜저',   '부산 02나 2002', '2020', 'waiting',  '카쉐어링', 18000, '2025-04-14 09:00:00'),
  ('2222222222', '1',  '1', 'K5',      '인천 03다 3003', '2018', 'fixing',   '렌트',     17000, '2025-04-14 09:00:00'),
  ('3333333333', '1',  '1', '스파크',   '광주 04라 4004', '2022', 'running',  '카쉐어링', 16000, '2025-04-14 09:00:00'),
  ('4444444444', '1',  '1', 'QM6',     '대구 05마 5005', '2019', 'waiting',  '렌트',     14000, '2025-04-14 09:00:00'),
  ('5555555555', '1',  '1', '카니발',   '울산 06바 6006', '2020', 'fixing',   '카쉐어링', 22000, '2025-04-14 09:00:00'),
  ('6666666666', '1',  '1', '모닝',     '경기 07사 7007', '2021', 'running',  '렌트',     13000, '2025-04-14 09:00:00'),
  ('7777777777', '1',  '1', 'SM6',     '강원 08아 8008', '2022', 'waiting',  '카쉐어링', 21000, '2025-04-14 09:00:00'),
  ('8888888888', '1', '1', '투싼',     '충남 09자 9009', '2018', 'fixing',   '렌트',     19000, '2025-04-14 09:00:00'),
  ('9999999999', '1', '1', '싼타페',   '전북 10차 1010', '2019', 'running',  '카쉐어링', 23000, '2025-04-14 09:00:00');

INSERT INTO rent(rent_uuid, mdn, rent_stime, rent_etime, renter_name, renter_phone,
  purpose, rent_status, rent_loc, rent_lat, rent_lon, return_loc, return_lat, return_lon, created_at)
  VALUES(
  '550e8400-e29b-41d4-a716-446655440000',  -- 대여 UUID
  '0000000000',                          -- 차량 MDN (CarEntity 참조)
  '2025-04-14 09:30:00',                  -- 대여 시작 시간
  '2025-04-14 12:45:00',                  -- 대여 종료 시간
  '오승택',                                -- 대여자 이름
  '010-2364-3047',                          -- 대여자 전화번호
  '렌트',                                  -- 사용 목적 (렌트 or 카쉐어링)
  'reserved',                              -- 대여 상태 (running, waiting, fixing)
  '서울 강남구 역삼동',                     -- 대여 위치
  37123456,                               -- 대여 경도 (int)
  12765432,                               -- 대여 위도 (int)
  '서울 강남구 논현동',                     -- 반납 위치
  37123500,                               -- 반납 경도 (int)
  12765500,                                -- 반납 위도 (int)
  '2025-04-14 09:00:00'
);