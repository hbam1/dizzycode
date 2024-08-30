# 프로젝트 명

- Dizzycode (Discord 클론)

# 구조도

![img.png](img.png)

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
- 채널 검색 기능을 위해 Flask로 AI 서버 도입 (https://github.com/hbam1/Dizzycode_RecommendationServer)
- API는 Swagger를 통해 문서화

# 프로젝트 실행

## 환경설정
```bash
FILE_UPLOAD_DIR=./uploads
OPENVIDU_SECRET=secret
OPENVIDU_URL=http://localhost:4443
SERVER_PORT=8081
SPRING_DATA_MONGODB_URI=mongodb+srv://admin:password1234@dizzycode.pg2ff.mongodb.net/dizzycode?retryWrites=true&w=majority&appName=Dizzycode
SPRING_DATA_REDIS_HOST=localhost
SPRING_DATASOURCE_PASSWORD=password
SPRING_DATASOURCE_URL=jdbc:mysql://localhost:3306/dizzycode?useSSL=false&useUnicode=true&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true
SPRING_DATASOURCE_USERNAME=admin
SPRING_JWT_SECRET=vmfhaltmskdlstkfkdgodyroqkfwkdbalroqkfwkdbalaaaaaaaaaaaaaaaabbbbbcccccc
SPRING_RABBITMQ_HOST=localhost
SPRING_RABBITMQ_PASSWORD=guest
SPRING_RABBITMQ_USERNAME=guest
SPRING_RESOURCES_STATIC_LOCATIONS=classpath:/static/,file:uploads/
SPRING_SERVLET_MULTIPART_LOCATION=uploads
```

## 실행

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

#### test code 예시

```bash
@Test
void 이메일이_같은_회원_가입_불갸() {
    // given
    MemberSignup memberGiven = new MemberSignup();
    memberGiven.setEmail("test@test.com");
    memberGiven.setUsername("test1");
    memberGiven.setPassword("password");
    memberService.signUp(memberGiven);

    // when
    MemberSignup memberSignup = new MemberSignup();
    memberSignup.setEmail("test@test.com");
    memberSignup.setUsername("test2");
    memberSignup.setPassword("password");

    // then
    assertThatThrownBy(() -> {
        memberService.signUp(memberGiven);
    }).isInstanceOf(ExistMemberException.class);
}
```

# 성능 테스트

## 1. Websocket, STOMP 연결 시 병목 지점 확인

### Test

![img_1.png](img_1.png)

- 사용자 연결 시 서버 측 부하 지점 확인
- Spring 서버와 RabbitMQ 중 RabbitMQ에 걸리는 부하가 성능 문제 야기
- 연결 후 메시지 발행까지 과정이 하나씩 진행될 때마다 RabbitMQ의 응답속도 저하 확인

### 성능 개선 확인

![img_2.png](img_2.png)

- 메모리 512MB 할당 시
  - Average: 평균 3826ms
  - Std. Dev.(표준편차): 평균 3682ms
- 메모리 1024MB 할당 시
  - Average: 평균 2379ms
  - Std. Dev.(표준편차): 평균 1894ms

#### 결론

- Average의 평균은 약 1.6배, Std. Dev.(표준편차)의 평균은 약 1.9배 가량 빨라짐

## 2. 메시지 read

### Test

![img_3.png](img_3.png)

- 저장된 메시지가 많으면 특정의 방의 메시지를 읽을 때 응답 속도 저하 예상
- 유의미한 속도 저하 시에는 index를 활용할 계획
- 각각 1만 개, 10만 개의 데이터가 있을 때 읽기 속도 차이 확인
- 그 결과, 각각 Average가 10ms, 57ms로, 느려지기는 하지만 큰 성능 저하는 나타나지 않음

#### 결론

- index 설정을 할 때의 trade off도 존재하기 때문에, 속도 저하가 크지 않을 때는 도입 이유가 없다고 판단

# 추가 사항

### 프론트 엔드

- React로 이루어진 프로젝트
- repository: https://github.com/DizzyCode2024/client
