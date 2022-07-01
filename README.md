# 키친포스

## 요구 사항

## 용어 사전

| 한글명 | 영문명 | 설명 |
| --- | --- | --- |
| 상품 | product | 메뉴를 관리하는 기준이 되는 데이터 |
| 메뉴 그룹 | menu group | 메뉴 묶음, 분류 |
| 메뉴 | menu | 메뉴 그룹에 속하는 실제 주문 가능 단위 |
| 메뉴 상품 | menu product | 메뉴에 속하는 수량이 있는 상품 |
| 금액 | amount | 가격 * 수량 |
| 주문 테이블 | order table | 매장에서 주문이 발생하는 영역 |
| 빈 테이블 | empty table | 주문을 등록할 수 없는 주문 테이블 |
| 주문 | order | 매장에서 발생하는 주문 |
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

## 요구사항
- 상품
  - 상품을 생성할 수 있다.
    - 상품은 0원 이상의 가격을 가져야 한다.
  - 상품 목록을 조회할 수 있다.
- 메뉴 그룹
  - 메뉴 그룹을 생성할 수 있다.
  - 메뉴 그룹 목록을 조회할 수 있다.
- 메뉴
  - 메뉴를 생성할 수 있다.
    - 메뉴는 0원 이상의 가격을 가져야 한다.
    - 메뉴는 메뉴그룹에 속해있어야만 한다.
    - 존재하는 상품이어야 한다.
    - 메뉴의 가격은 포함된 상품들의 금액에 합보다 작거나 같아야 한다.
  - 메뉴 목록을 조회할 수 있다.
- 주문 테이블
  - 주문 테이블을 생성할 수 있다.
  - 주문 테이블 목록을 조회할 수 있다.
  - 빈 테이블로 변경할 수 있다.
    - 그룹으로 지정되지 않은 테이블이어야 한다.
    - 주문 상태가 '조리' 또는 '식사' 상태가 아니어야 한다.
  - 방문 손님 수를 변경할 수 있다.
    - 방문 손님의 수는 0 보다 작지 않아야 한다.
    - 주문 테이블이 빈 테이블이 아니어야 한다.
- 단체 지정
  - 단체 지정을 할 수 있다.
    - 주문 테이블이 빈 테이블이어야 한다.
    - 주문 테이블의 수가 2개 이상이어야 한다.
    - 그룹으로 지정되지 않은 테이블이어야 한다.
  - 단체 지정을 해제할 수 있다.
    - 해당 주문 테이블들의 주문 상태가 '조리' 또는 '식사' 상태가 아니어야 한다.
- 주문
  - 주문을 생성할 수 있다.
    - 주문 항목 1개 이상 있어야 한다.
    - 존재하는 메뉴이어야 한다.
    - 중복된 메뉴가 있어서는 안된다.
    - 주문 테이블이 빈 테이블이 아니어야 한다.
  - 주문 목록을 조회할 수 있다.
  - 주문 상태를 변경할 수 있다.
    - 주문 상태가 '계산 완료' 상태가 아니어야 한다.
    
---
## 🚀 1단계 - 테스트를 통한 코드 보호
### 요구 사항 1
- kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다. 
- 미션을 진행함에 있어 아래 문서를 적극 활용한다.
- <a href="https://dooray.com/htmls/guides/markdown_ko_KR.html">마크다운(Markdown) - Dooray!</a>

### 요구 사항 2
- 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다. 
- 모든 Business Object에 대한 테스트 코드를 작성한다. @SpringBootTest를 이용한 통합 테스트 코드 또는 @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성한다.
- 인수 테스트 코드 작성은 권장하지만 필수는 아니다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.
    - [Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
    - [Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)

---
## 🚀 2단계 - 서비스 리팩터링
### 요구 사항
- 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
- Spring Data JPA 사용 시 spring.jpa.hibernate.ddl-auto=validate 옵션을 필수로 준다.
- 데이터베이스 스키마 변경 및 마이그레이션이 필요하다면 아래 문서를 적극 활용한다.
  - [DB도 형상관리를 해보자!](https://meetup.toast.com/posts/173)

### 리팩토링 리스트
- [x] 패키지 나누기
- [x] JPA 설정
- [x] Request, Response DTO 생성
  - [x] product
  - [x] menu
  - [x] menu group
  - [x] menu product
  - [x] order
  - [x] order line item
  - [x] order table
  - [x] table group
- [x] Service 비즈니스 로직 도메인으로 책임 위임
  - [x] ProductService
  - [x] MenuService
  - [x] MenuGroupService
  - [x] OrderService
  - [x] TableService
  - [x] TableGroupService
- [x] DAO -> Repository 변경
  - [x] product
  - [x] menu
  - [x] menu group
  - [x] menu product
  - [x] order
  - [x] order line item
  - [x] order table
  - [x] table group
- [x] 불필요한 getter, setter 제거
- [x] 모든 원시 값과 문자열 포장

---
## 🚀 3단계 - 의존성 리팩터링
### 요구사항
이전 단계에서 객체 지향 설계를 의식하였다면 아래의 문제가 존재한다. 의존성 관점에서 설계를 검토해 본다.

- 메뉴의 이름과 가격이 변경되면 주문 항목도 함께 변경된다. 메뉴 정보가 변경되더라도 주문 항목이 변경되지 않게 구현한다.
- 클래스 간의 방향도 중요하고 패키지 간의 방향도 중요하다. 클래스 사이, 패키지 사이의 의존 관계는 단방향이 되도록 해야 한다.
- 데이터베이스 스키마 변경 및 마이그레이션이 필요하다면 아래 문서를 적극 활용한다.
  - [DB도 형상관리를 해보자!](https://meetup.toast.com/posts/173)
  
### 의존도 draw
- Before(https://github.com/tyakamyz/jwp-refactoring/blob/step3/docs/refactor-before.drawio.png)
- After(https://github.com/tyakamyz/jwp-refactoring/blob/step3/docs/refactor-after.drawio.png)
- After - DIP 적용 후 (https://github.com/tyakamyz/jwp-refactoring/blob/step3/docs/refactor-after.drawio_DIP적용.png)

### 리팩토링 리스트
- [x] 현재 의존성 현황 정리
- [x] 패키지 재정의
- [x] 클래스 사이의 의존 관계 단방향으로 리팩토링
- [x] 패키지 사이의 의존 관계 단방향으로 리팩토링

---
## 🚀 4단계 - 멀티 모듈 적용
### 요구사항
- Gradle의 멀티 모듈 개념을 적용해 자유롭게 서로 다른 프로젝트로 분리해 본다.
  - 컨텍스트 간의 독립된 모듈로 만들 수 있다.
  - 계층 간의 독립된 모듈로 만들 수 있다.
- 의존성 주입, HTTP 요청/응답, 이벤트 발행/구독 등 다양한 방식으로 모듈 간 데이터를 주고받을 수 있다.

### 리팩토링 리스트
- [x] 멀티 모듈 분리
- [x] 테스트 완료

---
- Lombok은 그 강력한 기능만큼 사용상 주의를 요한다.
  - 무분별한 setter 메서드 사용
  - 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
  - [Lombok 사용상 주의점(Pitfall)](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)
  - 이번 과정에서는 Lombok 없이 미션을 진행해 본다.
- 자바 코드 컨벤션을 지키면서 프로그래밍한다.
  - 기본적으로 Google Java Style Guide을 원칙으로 한다.
  - 단, 들여쓰기는 '2 spaces'가 아닌 '4 spaces'로 한다.
- indent(인덴트, 들여쓰기) depth를 2를 넘지 않도록 구현한다. 1까지만 허용한다.
  - 예를 들어 while문 안에 if문이 있으면 들여쓰기는 2이다.
  - 힌트: indent(인덴트, 들여쓰기) depth를 줄이는 좋은 방법은 함수(또는 메서드)를 분리하면 된다.
- 3항 연산자를 쓰지 않는다.
- else 예약어를 쓰지 않는다.
  - else 예약어를 쓰지 말라고 하니 switch/case로 구현하는 경우가 있는데 switch/case도 허용하지 않는다.
  - 힌트: if문에서 값을 반환하는 방식으로 구현하면 else 예약어를 사용하지 않아도 된다.
- 모든 기능을 TDD로 구현해 단위 테스트가 존재해야 한다. 단, UI(System.out, System.in) 로직은 제외
  - 핵심 로직을 구현하는 코드와 UI를 담당하는 로직을 구분한다.
  - UI 로직을 InputView, ResultView와 같은 클래스를 추가해 분리한다.
- 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
  - 함수(또는 메소드)가 한 가지 일만 하도록 최대한 작게 만들어라.
- 배열 대신 컬렉션을 사용한다.
- 모든 원시 값과 문자열을 포장한다
- 줄여 쓰지 않는다(축약 금지).
- 일급 컬렉션을 쓴다.
- 모든 엔티티를 작게 유지한다.
- 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
