## 🎯 Spring Batch 를 이용한 정산 시스템 프로젝트

----
```
배달 서비스(배달의 민족, 요기요)를 참고해 관리자(Admin)와 가게 업주(Owner)가 주문 정보와 정산 정보를 조회, 관리하는
프로그램입니다.
```


### ⚒️ 기술 스택

----

**Backend** : `Spring Boot`, `JPA`, `QueryDSL`  
**Database** : `MySQL`  
**Frontend** : `Flutter`  
**Devops** : `Docker`, `Jenkis`, `Aws Ec2`

### 클라이언트 화면

<details>
  <summary><b>📍 전체 보기</b></summary>
  
|로그인|
|:--:|
|<img src="https://github.com/user-attachments/assets/96d21658-cb33-4b19-a92f-4a8606724a60" width="1400" height="500" />

|메인 화면|
|:--:|
|<img src="https://github.com/user-attachments/assets/a6f86f8f-6302-4d9e-a294-b1f88b7fc489" width="1400" height="500" />

|정산 달력|
|:--:|
|<img src="https://github.com/user-attachments/assets/c65e6fe7-16e2-4a35-bc79-ee7f0807ea8f" width="1400" height="500" />

|주문 내역|
|:--:|
|<img src="https://github.com/user-attachments/assets/692b9450-8c29-4bd7-a582-2e479f072c22" width="1400" height="500" />

|주문 상세 내역|
|:--:|
|<img src="https://github.com/user-attachments/assets/2261b1fd-2b39-441e-89f5-cffc6a875cd8" width="1400" height="500" />

</details>

### 📖 개발 기록
<details>
<summary><b>📖 개발 기록</b></summary>

<div style="display: flex; flex-wrap: wrap; gap: 20px;">

  <div style="border: 1px solid #ddd; border-radius: 8px; padding: 15px; width: 250px; box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);">
    <h4><a href="https://comumu.tistory.com/149" target="_blank" style="color: #007bff; text-decoration: none;">Docker EC2 CI/CD 구축</a></h4>
  </div>

</details>

### 기능 명세서

----

<details>
  <summary><b>📍 전체 보기</b></summary>

### 업주 도메인
1. 주문 내역 조회
   - [x] 업주(Owner)는 가게의 주문 내역을 조회할 수 있다.
     - [x] 수정은 불 가능하다.
2. 정산 내역 조회
   - [x] 업주는 가게의 정산 내역을 조회할 수 있다.
   - [x] 달력으로 나타내고, 하루 매출을 노출시킨다.
   - [x] 한달, 하루단위를 기본으로 나타낸다.

### 배치 시스템

1. 데이터 수집 및 전처리
   - [x] 거래 데이터 수집: 가게의 거래 데이터를 수집합니다. 데이터는 하루 동안 발생한 모든 거래 내역(판매, 환불 등)을 포함해야 합니다.
   - [x] 데이터 유효성 검증: 수집된 거래 데이터의 유효성을 검증하는 단계를 추가합니다. 예를 들어, 누락된 필드가 없는지, 금액이 음수가 아닌지 등의 검증 로직을 포함합니다.
   - [x] 데이터 정규화: 여러 가게에서 수집된 데이터를 하나의 표준 포맷으로 정규화합니다. 서로 다른 포맷의 데이터를 동일한 포맷으로 변환하는 로직을 구현합니다.
     
2. 정산 계산 로직
   - [x] 매출 집계: 가게별 총 매출, 환불 금액, 순매출을 계산합니다.
   - [x] 수수료 계산: 각 가게에 적용되는 수수료(예: 결제 수수료, 플랫폼 수수료 등)를 계산하여 최종 정산 금액을 계산합니다.
   - [x] 할인 처리: 특정 프로모션이나 할인 이벤트가 적용된 경우, 이를 반영하여 정산 금액을 계산합니다.
   
4. 정산 결과 저장
   - [x] 정산 내역 저장: 계산된 정산 결과를 DB에 저장합니다. 가게별로 일별, 월별 정산 내역을 관리할 수 있도록 테이블을 설계합니다.
   - [x] 이력 관리: 정산 작업의 이력을 관리합니다. 예를 들어, 정산이 언제 완료되었는지, 성공/실패 여부 등을 기록합니다.

5. 스케줄링 및 알림
   - [x] 정산 작업 스케줄링: AM 3:00에 정산 작업이 자동으로 실행되도록 Spring Batch의 스케줄링 기능을 사용하여 배치 작업을 구성합니다.
   - [ ] 실패 시 재시도 로직: 정산 작업이 실패했을 때 자동으로 재시도하는 로직을 구현합니다. 재시도 횟수와 대기 시간을 설정합니다.
   - [ ] 정산 완료 알림: 정산 작업이 완료되면 가게 주인에게 정산 완료 알림을 전송합니다. 이메일이나 SMS 등을 활용할 수 있습니다
   
6. 정산 결과 파일 생성 및 다운로드
   - [ ] 정산 결과 파일 생성: 정산 결과를 CSV 또는 Excel 파일로 생성하는 기능을 구현합니다. 파일에는 가게별 매출, 수수료, 최종 정산 금액 등이 포함되어야 합니다.
   - [ ] 파일 저장소 관리: 생성된 파일을 AWS S3 또는 로컬 파일 시스템에 저장하는 기능을 구현합니다.
   - [ ] 파일 다운로드 API: 저장된 정산 결과 파일을 다운로드할 수 있는 REST API를 제공합니다. 가게별로 파일을 조회하고 다운로드할 수 있어야 합니다.

</details>


