# 키친포스

## step2 요구 사항

- [ ] 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.

### 작업 계획

- 도메인을 기준으로 하나 씩 리팩토링을 진행한다.
- 엔티티 -> 서비스 -> 컨트롤러 순서로 리팩토링을 진행
- 엔티티는 spring data jpa를 사용 할 수 있도록 리팩토링 한다.

- [x] 상품
- [x] 메뉴 그룹
- [ ] 메뉴
- [ ] 테이블
- [ ] 주문
- [ ] 단체 지정

## step1 요구 사항

- [x] kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다.
- [x] 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다. 모든 Business Object에 대한 테스트 코드를 작성한다. @SpringBootTest를 이용한 통합 테스트 코드 또는
  @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성한다. 인수 테스트 코드 작성은 권장하지만 필수는 아니다.

### 키친포스 요구사항

#### 상품

- [x] 상품을 등록할 수 있다.
    - [x] 상품 가격은 필수이고 0원 보다 작을 수 없다.
- [x] 상품 목록을 조회할 수 있다.

#### 메뉴 그룹

- [x] 메뉴 그룹을 등록할 수 있다.
- [x] 메뉴 그룹 목록을 조회할 수 있다.

#### 메뉴

- [x] 메뉴를 등록할 수 있다.
    - [x] 메뉴 가격은 필수이고 0원 보다 작을 수 없다.
    - [x] 메뉴를 추가할 메뉴 그룹이 존재해야 한다.
    - [x] 메뉴를 만드는데 사용하는 메뉴 상품 목록의 상품이 존재해야 한다.
    - [x] 메뉴 상품 가격의 총 합은 메뉴 가격보다 작아야 한다.
- [x] 메뉴 목록을 조회할 수 있다.

#### 테이블

- [x] 테이블을 등록할 수 있다.
    - [x] 테이블 생성 시 단체지정은 되어있지 않다.
- [x] 테이블 목록을 조회할 수 있다.
- [x] 테이블을 빈 테이블로 변경할 수 있다.
    - [x] 빈 테이블로 변경할 테이블이 존재해야 한다.
    - [x] 단체 지정인 테이블일 경우 빈 테이블로 변경할 수 없다.
    - [x] 테이블의 주문 상태가 조리 혹은 식사이면 빈 테이블로 변경할 수 없다.
- [x] 빈 테이블을 테이블로 변경할 수 있다.
    - [x] 주문 테이블로 변경할 테이블이 존재해야 한다.
    - [x] 단체 지정인 테이블일 경우 주문 테이블로 변경할 수 없다.
    - [x] 테이블의 주문 상태가 조리 혹은 식사이면 주문 테이블로 변경할 수 없다.
- [x] 테이블의 방문한 손님 수를 변경할 수 있다.
    - [x] 방문한 손님 수는 0이상 이어야 한다.
    - [x] 방문한 손님 수를 변경할 테이블이 존재해야 한다.
    - [x] 빈 테이블이 아니어야 한다.

#### 주문

- [x] 주문을 등록할 수 있다.
    - [x] 주문 항목 목록이 존재해야 한다.
    - [x] 메뉴에 존재하지 않는 주문 항목은 주문 할 수 없다.
    - [x] 주문 테이블이 존재해야 한다.
    - [x] 주문 테이블이 빈 테이블이 아니어야 한다.
    - [x] 주문 등록 시 상태는 조리이다.
    - [x] 주문 등록 시 주문 시각은 현재 시각으로 설정한다.
- [x] 주문 목록을 조회할 수 있다.
- [x] 주문 상태를 변경할 수 있다.
    - [x] 주문이 존재해야 한다.
    - [x] 계산 완료 상태의 주문은 상태를 변경할 수 없다.

#### 단체 지정

- [x] 테이블 목록을 묶어 단체 지정을 할 수 있다.
    - [x] 테이블 목록의 개수는 2개 이상이어야 한다.
    - [x] 모든 테이블은 존재하는 테이블이어야 한다.
    - [x] 모든 테이블은 빈 테이블이 아니어야 한다.
    - [x] 모든 테이블은 단체 지정이 되지 않은 테이블이어야 한다.
- [x] 단체 지정을 삭제할 수 있다.
    - [x] 단체 내 모든 테이블의 주문 상태가 주문 또는 식사 상태이면 단체 지정을 삭제할 수 없다.

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
