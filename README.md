# 레거시 코드 리팩터링 - 키친포스

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

## 1단계 - 테스트를 통한 코드 보호
### 요구사항
- [x] `kitchenpos` 패키지의 코드을 통한 키친포스의 요구 사항을 `README.md`에 작성
- [ ] 작성한 키친포스의 요구 사항을 토대로 테스트 코드를 작성
  - [ ] 모든 Business Object에 대한 테스트 코드를 작성
  - [ ] `@SpringBootTest`를 이용한 통합 테스트 코드 또는 `@ExtendWith(MockitoExtension.class)`를 이용한 단위 테스트 코드를 작성

### 키친포스 요구사항
- 메뉴그룹
  - 메뉴그룹이 저장/조회 된다.
  
- 메뉴
 - 메뉴가격은 메뉴에대한 상품의 수량과 가격을 곱한 가격들을 합한 금액을 초과할 수 없다.
 - 메뉴가 저장/조회 된다.
 
- 주문
  - 어떤 주문 테이블에서 발생됬는지 저장된다.
  - 오더는 초기상태가 조리로 발생시간은 현재시간으로 저장된다.
  - 오더가 조회된다.
  - 오더에대한 메뉴와 그 수량이 저장된다.
  - 오더 상태가 변경된다.
  
- 상품
  - 상품이 저장/조회된다.

- 주문테이블
 - 모든 주문테이블이 조회된다.
 - 주문테이블이 결재가 되면 주문테이블의 상태는 비어있게 된다.
 - 주문테이블의 방문수가 입력된다.

- 단체지정
 - 단체지정 수는 2이상일때 진행된다.
 - 주문테이블은 비어있지 않은 상태로 저정된다.
 - 주문테이블은 오더상태가 결재가되면 단체지정의 정보가 제거된다.

### 레거시코드 분석
#### UML

#### ERD

#### API List
- MenuGroupRestController.java
  - @PostMapping("/api/menu-groups")
  - @GetMapping("/api/menu-groups")

- MenuRestController.java
  - @PostMapping("/api/menus")
  - @GetMapping("/api/menus")

- OrderRestController.java
  - @PostMapping("/api/orders")
  - @GetMapping("/api/orders")
  - @PutMapping("/api/orders/{orderId}/order-status")

- ProductRestController.java
  - @PostMapping("/api/products")
  - @GetMapping("/api/products")

- TableGroupRestController.java
  - @PostMapping("/api/table-groups")
  - @DeleteMapping("/api/table-groups/{tableGroupId}")

- TableRestController.java
  - @PostMapping("/api/tables")
  - @GetMapping("/api/tables")
  - @PutMapping("/api/tables/{orderTableId}/empty")
  - @PutMapping("/api/tables/{orderTableId}/number-of-guests")

#### 기능 기록
- MenuGroupService
  - MenuGroup이 저장된다.
  - MenuGroup이 모두 조회된다.

- MenuService
  - Menu가 저장된다.
    - Menu의 모든 MenuProduct를 조회한후 MenuProduct가 없으면 예외처리 있으면
      MenuProduct의 수량과 가격을 곱한값을 다 더한다.
    - Menu의 가격이 위 과정보다 크면 에외처리된다.
    - Menu가 저장된다.
    - MenuProduct가 저정된다.

  - MenuGroup이 모두 조회된다.
    - 모든 Menu가 조회된다.

- OrderService
  - Order가 생성된다.
    - Order의 OrderLineItem리스트를 조회 후 비어있으면 예외처리
    - OrderLineItem리스트에서 MenuId리스트를 생성
    - OrderLineItem리스트의 개수와 등록된 MenuId의 개수가 다르면 예외처리
    - Order에서 OrderTable를 조회 없으면 예외처리
    - Order에 OrderTable를 설정
    - Order의 상태를 Cooking으로 설정
    - Order시간을 현재시간으로 설정
    - Order 저장
    - orderLineItem을 저장

  - Order가 조회된다.
    - 모든 Order가 조회된다.

  - Order상태가 변경된다.
    - 저장된 Order 가 아니면 예외
    - Order 상태가 COMPLETION이면 예외
    - 입력된 상태로 오더 변경 후 저장

- ProductService
  - Product가 생성된다.
    - Product 가격이 없거나 0보다 같으면 예외처리
  - Product가 조회된다.
    - 모든 Product가 조회횐다.

- TableGroupService
  - TableGroup이 생성된다.
    - 요청된 OrderTable이 없거나 2보다 작으면 예외처리
    - OrderTable리스트와 등록된 OrderTable리스트개수가 다르면 예외
    - 등록된 OrderTable가 없거나 TableGroupId가 Null이 아닐떄 예외처리
    - TableGroup생성시간을 현재시간으로 등록한다.
    - OrderTable을 비어있지 않음으로 하고 저장한다.

  - ungroup
    - 모든 OrderTable를 조회한다.
    - order에 OrderTable를 기준으로 Order상태가 COOKING이거나 MEAL이면 예외처리
    - orderTable의 TableGroup를 해제한 후 저장

- TableService
  - OrderTable를 생성된다.
    - OrderTable의 TableGroup를 Null로 설정 후 저장한다.
  - 모든 OrderTable를 조회한다.
  - changeEmpty
    - 저장된 orderTable이 없으면 예외처리
    - order에 OrderTable를 기준으로 Order상태가 COOKING이거나 MEAL이면 예외처리
    - 저장된 OrderTable의 빈 상태여부를 변경 후 저장
  - changeNumberOfGuests
    - 손님의 숫자가 0보다 작으면 예외처리
    - 저장된 orderTable이 없으면 예외처리
    - orderTable에 손님 숫자를 입력한 후 저장한다.