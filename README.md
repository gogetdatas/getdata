# 프로젝트
> - 각 업체에서 나오는 데이터를 각 업체에 소속된 사용자가 자신들의 데이터를 모니터링하고 집계할 수 있는 서비스
<br/>

## Summary
> - 애플리케이션 이름: getdata
> - 개발 환경: IntelliJ IDEA
> - API 테스트 도구: Postman
> - 빌드 도구: Gradle
> - 협업 도구: GitHub
> - 자바 버전: 17


## 🛠️ Backend Tech Stack
<img src="https://img.shields.io/badge/Java-007396?style=for-the-badge&logo=Java&logoColor=white"><img src="https://img.shields.io/badge/Spring Boot-6DB33F?style=for-the-badge&logo=Spring Boot&logoColor=white"><img src="https://img.shields.io/badge/mysql-4479A1?style=for-the-badge&logo=mysql&logoColor=white"><img src="https://img.shields.io/badge/mongodb-47A248?style=for-the-badge&logo=mongodb&logoColor=white"><img src="https://img.shields.io/badge/Redis-DC382D?style=for-the-badge&logo=redis&logoColor=white">
<br/>

## **주요기능**
## **company-service**
  **유저 업체 등록** 
  유저는 업체를 등록할 수 있으며 등록된 업체는 고유의 토큰을 부여받아 이후 데이터 전송 시 검증에 사용됨
  
  **업체 유저 등록**
  유저는 특정 업체에 등록 요청을 보낼 수 있고 업체의 관리자가 승인하면 소속됨
  
  **업체 팀 생성**
  업체의 유저는 팀을 생성 할 수 있음
  
  **업체 팀 유저 등록**
  업체에 소속된 유저는 특정 팀에 소속 요청을 하고 팀 관리자가 승인하면 소속됨 

## **channel-service**
  **업체 채널 생성**
  > - 생성요청 api에 들어온 데이터로 채널 생성 및 설정 생성 
  >   - 타입만 설정한 데이터 
  >   - 타입과 서브타입을 설정한 데이터 
  >   - 타입과 서브타입 그리고 집계설정한 데이터

  **업체 채널 생성 json 예시**
  
 **타입만 선택한 예시**:
  ```json
  {
      "channelName": "userActivity_Channel",
      "type": "click",
      "isAggregates": false
  }
  ```
  **타입 + 서브타입 선택한 예시**:
  ```json
  {
      "channelName": "userActivity_Click_Channel",
      "type": "userActivity",
      "isAggregates": false,
      "subtype": "click",
      "selectKeyHash": "50655e18bd8c236ea7fa56a448122b47d9820fd2b1936ffe7cd22ec52357d88e",
      "selectKeys": ["ModelName"]
  }
  ```
  **타입 + 서브타입 + 집계 설정한 예시**:
  ```json
  {
      "channelName": "modelNameClickCnt",
      "type": "userActivity",
      "isAggregates": true,
      "subtype": "click",
      "selectKeyHash": "50655e18bd8c236ea7fa56a448122b47d9820fd2b1936ffe7cd22ec52357d88e",
      "selectKeys": ["ModelName"],
      "aggregates": {
          "groupBy": ["ModelName"],
          "filters": [
              {
                  "field": "ModelName",
                  "type": "COUNT"
              }
          ],
          "orderBy": "modelName DESC"
      }
  }
  ```
**업체 채널 조회**
  > 1. 해당 채널 조회 
  > 2. 채널의 설정 조회 </br>
  >    - 집계 설정이 True일 경우: 설정된 집계 방식에 따라 데이터를 반환</br>
  >    - 집계 설정이 False일 경우: 원본 데이터를 그대로 반환
## **datamanagemant-service**
  **데이터 조회**
![get data](https://github.com/user-attachments/assets/b1cf3c4e-c191-4a30-b308-bd00ef9fb8f4)
  > 1. 채널 설정 조회
  > 2. 설정에 있는 조건대로 데이터 조회
  > 3. 데이터 반환
## DBMS
## **ERD**
![erd](https://github.com/user-attachments/assets/1b41f850-68e7-4538-b838-e42ec64561a5)
### - Mysql
> - 업체 데이터의 메타데이터, 유저, 업체, 팀, 채널들의 관계형 데이터를 관리
### - Redis
> - 유저 정보 캐싱 및 빠른 조회를 위한 캐시 저장소로 사용
### - MongoDB
> - 업체의 다양한 구조의 데이터를 관리하기 위해 비정형 데이터를 저장
> - 업체데이터와 채널설정의 데이터를 관리
<br/>
