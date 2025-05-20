# TRACKY - 실시간 차량 관제 서비스

<img src="https://github.com/Kernel360/KDEV4-TRACKY-BE/blob/resource/img-tracky/image.png?raw=true" width="400">

서비스 체험하기 - [https://www.tracky.kr](https://www.tracky.kr)<br/>
PDF - [https://www.canva.com/design/DAGnVKhYFmw/2JCuVRr8JMHjKqibKvbuIQ/edit](https://www.canva.com/design/DAGnVKhYFmw/2JCuVRr8JMHjKqibKvbuIQ/edit)<br/>
ERD - [https://www.erdcloud.com/d/mE2uimBHiXsvoxAWJ](https://www.erdcloud.com/d/mE2uimBHiXsvoxAWJ)



## TRACKY 소개
차량 운영에 어려움을 겪는 기업을 위한 **통합 관제 솔루션**입니다.

준 실시간 위치 추적, 운행 기록 조회, 다양한 통계 데이터 등을 제공함으로써

누가, 언제, 어디서, 얼마나 차량을 사용했는지 확인할 수 있습니다.

## 기술 스택
### BE
- Java17, Spring Boot, Spring Security, JWT
- Spring Data JPA, QueryDSL, SSE, RabbitMQ
- Amazon ECS, Amazon RDS, Amazon ALB


### FE
- React, Typescript, Vercel 


## 주요 기능

**대시보드**

![대시보드]()

**차량관리**

![차량관리]()

**준 실시간 경로 추적**

![경로조회]()


**운행기록 조회**

![운행기록]()

**사용자 및 관리자 통계**

![통계]()



## 시스템 아키텍처
![아키텍처.png]()


## 이슈 및 트러블 슈팅
- [부하테스트 - 15,000 TPS 수용하기]
  - hub, consumer 구조 채택 이유
  - 테스트 시나리오, 환경, 결과 도출 과정
  - 테스트 도중 발생한 트러블 슈팅

- [복합 인덱스를 통한 쿼리 최적화 과정](https://www.notion.so/1f2a3f519ab880c78f86f8d1bf688ba3)


## 팀원소개
<table>
  <tbody>
    <tr>
      <td align="center">
        <a href="">
          <img src="" width="100px;" alt="팀장 프로필"/><br />
          <span><b>천승준</b></span>
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="" width="100px;" alt="팀원 프로필"/><br />
          <span><b>구지원</b></span>
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="" width="100px;" alt="팀원 프로필"/><br />
          <span><b>조형준</b></span>
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="" width="100px;" alt="팀원 프로필"/><br />
          <span><b>차의진</b></span>
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="" width="100px;" alt="팀원 프로필"/><br />
          <span><b>김한빈</b></span>
        </a>
      </td>
      <td align="center">
        <a href="">
          <img src="" width="100px;" alt="팀원 프로필"/><br />
          <span><b>오승택</b></span>
        </a>
      </td>
    </tr>
  </tbody>
</table>



