# 키친포스

## 요구 사항

### 메뉴
- 메뉴를 생성 할 수 있다.
  - 생성 하려는 메뉴 가격은 비어있거나, 0보다 작을 수 없다.
  - 생성 하려는 메뉴의 메뉴 그룹이 시스템에 존재 하지 않으면 추가 할 수 없다.
  - 생성 하려는 메뉴의 메뉴 상품이 시스템에 등록 되어 있지 않으면 추가 할 수 없다.
  - 생성 하려는 메뉴 가격이 전체 메뉴상품의 전체 금액(가격 * 수량의 총합)보다 클 수 없다.
- 메뉴의 목록을 조회 할 수 있다.

### 메뉴 그룹
- 메뉴 그룹을 생성 할 수 있다.
- 메뉴 그릅의 목록을 조회 할 수 있다.

### 주문
- 주문을 생성 할 수 있다.
  - 생성하려는 주문에서 주문 항목이 비어있으면 주문을 생성 할 수 없다.
  - 생성하려는 주문에서 주문 항목의 메뉴가 시스템에 등록 되어 있지 않으면 주문을 생성 할 수 없다.
  - 생성하려는 주문에서 주문 테이블이 시스템에 등록 되어 있지 않으면 주문을 생성 할 수 없다.
  - 생성하려는 주문의 초기 상태는 조리(COOKING) 상태이다.
- 주문 목록을 조회 할 수 있다.
- 주문의 상태를 변경 할 수 있다.
  - 주문이 시스템에 등록 되어 있지 않으면 변경 할 수 없다.
  - 주문이 완료 상태이면 변경 할 수 없다.

### 상품
- 상품을 생성 할 수 있다.
  - 생성하려는 상품에서 상품 가격 항목이 비어있거나 0 보다 작을 수 없다.
- 상품 목록을 조회 한다.

### 테이블
- 테이블을 생성 할 수 있다.
- 테이블 목록을 조회 할 수 있다.
- 테이블을 빈테이블로 변경 할 수 있다.
  - 주문 테이블이 시스템에 등록 되어 있지 않으면 빈테이블로 변경 할 수 없다.
  - 주문 테이블이 단체 지정 되어 있으면 빈테이블로 지정 할 수 없다.
  - 조리 중(COOKING), 식사 중(MEAL) 상태에 있으면 빈테이블로 지정 할 수 없다.
- 테이블에 방문한 손님수를 변경 할 수 있다.
  - 변경하려는 손님수가 0 보다 작을 수 없다.
  - 주문 테이블이 시스템에 등록 되어 있지 않으면 손님수를 변경 할 수 없다.
  - 빈테이블 이면 손님수를 변경 할 수 없다.

### 테이블 그룹(단체 지정)
- 테이블 그룹을 지정 할 수 있다.
  - 주문 테이블이 비거나 2개보다 작으면 테이블 그룹으로 지정 할 수 없다.
  - 주문 테이블들이 시스템에 등록 되어 있지 않으면 테이블 그룹은 지정 할 수 없다.
  - 주문 테이블이 빈테이블이 아니면 테이블 그룹을 지정 할 수 없다.
  - 이미 테이블 그룹에 속해 있으면 테이블 그룹을 지정 할 수 없다.
- 지정된 테이블 그룹을 해제 할 수 있다.
  - 주문 상태가 조리중(COOKING), 식사중(MEAL)인 경우에는 해제 할 수 없다.
  
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
