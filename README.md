# 멀티 데이터소스 환경에서의 스프링 배치 5 활용

## 1. 스프링 배치 테스트하기

> 메인 DB에는 비즈니스 엔티티를, 서브 DB에는 배치 메타데이터 테이블을 두어 배치작업 처리 메타 데이터를 저장

1. config.batch 패키지의 모든 객체의 주석 해제
2. config.readwrite 패키지의 모든 객체를 주석 처리
3. 실행!
4. Launcher.java의 `@Scheduled` 어노테이션 왼쪽의 스케줄러 실행을 누르면 잡이 실행됨

## 2. `@Transactional` 어노테이션의 readOnly 속성으로 DB 구분하기

> 메인 DB는 쓰기전용, 서브 DB는 읽기전용으로 구분

1. config.batch 패키지의 모든 객체를 주석 처리
2. config.readwrite 패키지의 모든 객체 주석 해제
3. 실행!
4. http.product-api.http의 상품 생성 실행
5. 현재 도커컴포즈에는 서브 DB가 메인 DB를 복제하지 않으므로, 메인 DB에 저장된 상품을 서브 DB에 저장하기
6. http.product-api.http의 상품 조회 실행
7. 로그에 뜨는 커넥션의 요청 url 포트로 DB가 구분된 것을 확인! 

