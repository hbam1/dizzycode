# 프로젝트 명

- Dizzycode (Discord 클론)

# 구조도

![](../../../Downloads/Group 8.png)

# 기술 스택

- Spring
- Openvidu
- Redis
- MongoDB
- MySQL
- RabbitMQ
- Docker
- Swagger
- Nginx

# 주요 서비스 및 기능

- JWT 기반 인증
- WebSocket과 STOMP를 통한 실시간 채팅 (파일 업로드 가능)
- Openvidu를 사용한 실시간 음성 및 화상 통화
- Redis와 Websocket을 활용한 실시간 유저 상태 체크
- 서버 scale out에 따른 로드밸런서(Nginx)와 Message Queue(RabbitMQ) 도입
- 주요 도메인 관련 데이터는 MySQL에 저장하고, 채팅 관련 데이터는 쉽게 scale out 할 수 있는 MongoDB에 저장
- API는 Swagger를 통해 문서화

# 프로젝트 실행

```bash
git clone https://github.com/hbam1/dizzycode.git
docker-compose up -d
```

- docker container를 실행시킨 후, nginx 설정에 따라 프로젝트를 8081, 8082 포트에서 각각 실행
- API를 확인하고 싶다면 [http://localhost:8081/swagger-ui.html](http://localhost:8080/swagger-ui.html)에서 확인할 수 있음 (또는 8082 포트)

### 프론트엔드

- 만약 프론트엔드 부분도 실행시키고자 한다면 다음을 참고

```bash
git clone https://github.com/DizzyCode2024/client.git
npm i
npm run dev
```

- 위와 같이 프로젝트를 실행시킨 후 웹 브라우저를 통해 http://localhost:5173 에 접속하면 접근 가능

# 리팩토링

### Architecture 개선

- MVC에 충실한 기존 Layered Architecture에서 보다 원활한 TDD를 위한 구조로 개선
- Clean Architecture와 Hexagonal Architecture를 참고하여 도메인 중심으로 패키지 구조 개선 후 DB에 대한 의존성을 낮춤
- unit test가 보다 수월해졌고, 보다 도메인 중심 서비스에 충실해짐

# 추가 사항

### 프론트 엔드

- React로 이루어진 프로젝트
- repository: https://github.com/DizzyCode2024/client
