# 키친포스

## 요구 사항
### 메뉴 그룹
* 메뉴 그룹을 등록할 수 있다.
* 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴
* 메뉴를 등록할 수 있다.
  * 메뉴의 가격은 0원 이상이어야 한다.
  * 메뉴그룹에 속해있어야 한다.
  * 메뉴 상품은 모두 등록된 상품이어야 한다.
  * 메뉴 가격은 모든 메뉴 상품의 (가격 * 수량)의 합보다 작거나 같아야한다
* 메뉴를 조최회할 수 있다.

### 주문
* 주문을 등록할 수 있다.
  * 주문 항목이 비어있는 경우 주문을 등록할 수 없다.
  * 주문 항목의 수와 등록된 메뉴의 수는 같아야 한다.
  * 주문 테이블에 등록되어 있어야 한다.
* 주문 목록을 조회할 수 있다.
* 주문의 상태를 수정할 수 있다.
  * 주문이 존재해야 한다.
  * 계산 완료된 주문을 수정할 수 없다.

### 상품
* 상품을 등록할 수 있다.
  * 상품의 가격은 0원 이상이어야 한다.
* 상품 목록을 조회할 수 있다.

### 단체 지정(테이블 그룹)
* 단체 지정을 등록할 수 있다.
  * 단체 지정된 테이블은 등록된 테이블이어야 한다.
  * 주문 테이블들이 2개 이상이어야 단체 지정이 가능하다.
  * 빈 주문 테이블만 단체 지정을 할 수 있다.
  * 주문 테이블이 다른 단체에 지정되지 않아야 한다.
* 단체 지정을 해제할 수 있다.
  * 조리중이거나 식사중인 주문이 있으면 단체를 해제할 수 없다.

### 주문 테이블
* 주문 테이블 목록을 조회할 수 있다.
* 주문 테이블을 등록할 수 있다.
* 주문 테이블 이용 여부를 변경할 수 있다.
  * 등록된 주문 테이블이어야 한다.
  * 테이블 그룹에 속해있지 않아야 한다.
  * 주문 상태가 조리중 또는 식사중이 아니어야 한다.
* 주문 테이블의 방문한 손님 수를 변경할 수 있다.
  * 방문한 손님 수는 0명 이상이어야 한다.
  * 등록된 주문 테이블이어야 한다.
  * 주문 테이블이 빈 테이블이 아니어야 한다.

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |

---

## 1단계 - 테스트를 통한 코드 보호
### 요구 사항
* [x] kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다.
* [x] 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다.
  * 모든 Business Object에 대한 테스트 코드를 작성한다.
  * @SpringBootTest를 이용한 통합 테스트 코드 또는 @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성한다.
  * [x] 상품 관련 비즈니스 테스트
  * [x] 메뉴 그룹 관련 비즈니스 테스트
  * [x] 주문 테이블 관련 비즈니스 테스트
  * [x] 테이블 그룹 관련 비즈니스 테스트
  * [x] 메뉴 관련 비즈니스 테스트
  * [x] 주문 관련 비즈니스 테스트
* Lombok을 사용하지 않는다

## 2단계 - 서비스 리팩터링
### 요구 사항
* `spring.jpa.hibernate.ddl-auto=validate` 옵션 사용
* DB 형상 관리

### 프로그래밍 요구사항
* Lombok을 쓰지 않는다
* 무분별한 setter 사용하지 않기
* 자바 코드 컨벤션 지키기
  * 단, 들여쓰기는 `4 space`
* indent의 deps는 1까지만 허용한다.
* 3항 연산자를 쓰지 않는다.
* else, switch/case 예약어를 쓰지 않는다.
* 모든 기능을 TDD로 구현해 단위 테스트가 존재해야 한다
  * 핵심 로직을 구현하는 코드와 UI를 담당하는 로직을 구분한다.
  * UI 로직을 InputView, ResultView와 같은 클래스를 추가해 분리한다.
* 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
  * 함수(또는 메소드)가 한 가지 일만 하도록 최대한 작게 만들어라.
* 배열 대신 컬렉션을 사용한다.
* 모든 원시 값과 문자열을 포장한다
* 줄여 쓰지 않는다(축약 금지).
* 일급 컬렉션을 쓴다.
* 모든 엔티티를 작게 유지한다.
* 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.

### 진행 사항
* [x] 요구사항 정의
* [x] 패키기 구조 변경
* [x] jpa 종속성 추가
* [x] 도메인을 Entity화
* [x] Service에서 DAO대신 Repository로 사용으로 변경
* [x] 종속성 관계 추가
* [x] OrderStatus enum 추가
* [x] Request, Response TDO 적용
  * [x] Menu
  * [x] MenuGroup
  * [x] Product
  * [x] Order
  * [x] TableGroup
  * [x] orderLineItem
* [ ] 일급 객체, 일급 컬렉션 추출
* [x] Menu - Price를 일급 객체로 추출, 도메인 테스트
* [x] MenuProduct를 일급 컬렉션으로 추출






* [ ] 코드 리팩토링
  * [ ] 변수명
    * test code의 request, result값
    * 현실성 있는 데이터로 변경
  * [ ] 인덴트
