### 테스트를 통한 코드 보호

* [x] `kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 `README.md`에 작성
* [x] 정리한 키친포스의 요구 사항을 토대로 테스트 코드 작성
  * [x] 모든 Business Object 에 대한 테스트 코드 작성
  * [x] `@SpringBootTest`를 이용한 통합 테스트 코드 또는<br> 
    `@ExtendWith(MockitoExtension.class`를 이용한 단위 테스트 코드 작성
* [x] Lombok 없이 미션진행
<hr>
* http 디렉터리의 .http 파일(HTTP client)을 보고 어떤 요청을 받는지 참고
* src/main/resources/db/migration 디렉터리의 .sql 파일을 보고 어떤 관계로 이루어져 있는지 참고

![img.png](img.png)
<hr>

### 서비스 리팩터링

* [x] 단위 테스트하기 어려운 코드와 가능한 코드 분리
  * [x] 단위 테스트 구현
* [x] Spring Data JPA 사용 시 spring.jpa.hibernate.ddl-auto=validate 옵션 필수

### 서비스 리팩터링 리뷰

* [x] @Transactional 누락 방지를 위해 클래스 레벨에 추가
* [x] N+1 을 방지하기
  * fetch join 혹은 hibernate batch size 를 활용
* [x] 생성시 필요한 값은 생성자에서 모두 받을 수 있도록 변경
* [x] 반드시 필요한 검증로직이 있다면 비즈니스 로직과 함께 수행되도록 변경
* [x] Void 제거
* [x] 기본 생성자 protected 사용
* [x] 예외 메세지 추가
* [x] API 스펙이 변경되지 않도록 유지하기

### 의존성 리팩터링
- 시스템을 모듈단위로 분리하기 위해 의존성을 관리해야 한다
* [x] 패키지 분리
  * 상품 - product
  * 메뉴그룹 - menuGroup
  * 메뉴 - menu, menuProduct
  * 주문 - order, orderLineItem
  * 주문테이블 - orderTable
  * 단체지정 - tableGroup
* [ ] 같은 패키지는 객체참조, 서로다른 패키지는 Id + Repository 활용
  * [ ] 검증 로직의 분리
  * [ ] 간접참조의 변경사항 발생 시 이벤트 발행 (혹은 DIP 활용)
* [ ] 조회 단위는 객체참조가 가능한 영역까지만 한다
