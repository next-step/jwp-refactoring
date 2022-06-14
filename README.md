# 키친포스

## 요구 사항

### 상품

- 상품을 등록한다.
    - 가격은 0 이상이어야 한다.
    - 가격과 이름은 필수이다.
- 상품 목록을 조회한다.

### 메뉴 그룹

- 메뉴 그룹을 등록한다.
    - 이름은 필수이다.
- 메뉴 그룹 목록을 조회한다.

### 메뉴

- 메뉴(메뉴 상품 포함)를 등록한다.
    - 가격은 0 이상이어야 한다.
    - 등록할 메뉴 그룹이 있어야 한다.
    - 존재하는 상품이어야 한다.
    - 메뉴 가격이 상품들의 금액의 합 보다 클 수 없다.
    - 초기 상태는 조리 상태이다.
- 메뉴(메뉴 상품 포함) 목록을 조회한다.

### 주문 테이블

- 주문 테이블을 등록한다.
    - 처음에는 테이블 그룹이 지정되어 있지 않는다.
    - 빈 테이블 여부, 손님 수는 필수이다.
- 테이블 목록을 조회한다.
- 주문 테이블을 빈 테이블로 변경/해지한다.
    - 존재하는 주문 테이블이어야 한다.
    - 단체 지정된 주문 테이블은 변경할 수 없다.
    - 주문 테이블의 조리, 식사 중인 주문이 있으면 변경할 수 없다.
- 주문 테이블에 방문한 손님 수를 변경한다.
    - 방문한 손님 수가 0명 미만 일 수 없다.
    - 존재하는 주문 테이블이어야 한다.
    - 빈 테이블은 변경할 수 없다.

### 단체 지정

- 단체 지정을 등록한다.
    - 지정한 주문 테이블이 2개 미만일 수 없다
    - 존재하는 주문 테이블이어야 한다.
    - 주문 테이블이 빈 테이블이거나 다른 단체 지정에 포함되어 있으면 안된다.
- 단체 지정을 해지한다.
    -  주문 테이블들의 조리, 식사 중인 주문이 있으면 변경할 수 없다.

### 주문

- 주문을 등록한다.
    - 주문 항목이 비어있을 수 없다.
    - 주문 항목이 메뉴에 존재해야 한다.
    - 주문 테이블이 비어있을 수 없다.
- 주문(주문 항목 포함) 목록을 조회한다.
- 주문의 상태를 변경한다.
    - 변경할 주문이 존재해야 한다.
    - 주문이 계산 완료 상태이면 변경할 수 없다.

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
