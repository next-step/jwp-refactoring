# 키친포스

## 요구 사항

### 상품

- 상품을 등록할 수 있다
- 상품의 가격이 올바르지 않으면 등록할 수 없다
    - 상품의 가격은 0 원 이상이어야 한다
- 상품의 목록을 조회할 수 있다

### 메뉴

- 메뉴를 등록할 수 있다
- 메뉴의 가격이 올바르지 않으면 등록할 수 없다.
    - 메뉴의 가격은 존재해야 한다
    - 메뉴의 가격은 0원 이상이어야한다.
- 메뉴의 메뉴 그룹이 존재하지 않으면 등록할 수 없다.
- 메뉴 상품의 상품이 존재하지 않으면 등록할 수 없다
- 메뉴 상품의 가격은 상품의 가격과 메뉴 상품의 가격의 곱이다
- 메뉴의 가격이 올바르지 않으면 등록 할 수 없다
    - 메뉴의 가격은 메뉴 상품의 가격의 합보다 작아야 한다.
- 메뉴 목록을 조회할 수 있다

### 메뉴 그룹

- 메뉴 그룹을 등록할 수 있다
- 메뉴 그룹 목록을 조회할 수 있다

### 주문

- 주문을 등록할 수 있다
- 주문 항목 목록이 올바르지 않으면 주문을 등록할 수 없다
    - 주문 항목 목록은 비어 있을 수 없다
- 주문 항목 개수과 존재하는 메뉴 개수가 일치하지 않으면 등록할 수 없다
- 주문 테이블이 존재하지 않으면 등록할 수 없다
- 주문 테이블이 올바르지 않으면 주문을 등록할 수 없다
    - 주문 테이블은 빈 주문 테이블이 아니어야 한다
- 주문 목록을 조회할 수 있다
- 주문 상태를 변경할 수 있다
- 주문 상태 올바르지 않으면 주문 상태를 변경할 수 없다
    - 주문 상태가 완료가 아니어야 한다
- 주문이 존재하지 않으면 주문 상태를 변경할 수 없다

### 주문 테이블

- 주문 테이블을 등록할 수 있다
- 주문 테이블 목록을 조회할 수 있다
- 주문 테이블을 빈 테이블로 변경할 수 있다
- 주문 테이블이 존재하지 않으면 빈 테이블로 변경할 수 없다
- 주문 테이블의 테이블 그룹 아이디가 올바르지 않으면 빈 테이블로 변경할 수 없다
    - 테이블 그룹 아이디가 존재해야 한다
- 주문 테이블 아이디와 주문 혹은 식사 주문 상태인 주문이 존재하면 빈 테이블 여부를 변경할 수 없다
- 주문 테이블의 방문한 손님 수를 변경할 수 있다
- 주문 테이블의 방문한 손님 수가 올바르지 않으면 방문한 손님 수를 변경할 수 없다
    - 방문한 손님 수는 0명 이상이어야 한다
- 주문 테이블이 빈 테이블 여부가 올바르지 않으면 방문한 손님 수를 변경할 수 없다
    - 빈 테이블이 아니여야 한다

### 단체 지정

- 단체 지정을 저장할 수 있다
- 단체 지정의 주문 테이블이 올바르지 않으면 단체 지정을 저장할 수 없다
    - 주문 테이블 목록이 비어 있을 수 없다
    - 주문 테이블 목록의 크기는 2 이상이어야 한다
- 단체 지정의 주문 테이블이 올바르지 않으면 단체 지정을 저장할 수 없다
    - 주문 테이블 목록과 저장된 저장된 주문 테이블 목록의 크기가 같아야 한다
- 단체 지정의 주문 테이블이 올바르지 않으면 단체 지정을 저장할 수 없다
    - 주문 테이블 목록이 비어 있을 수 없다
    - 주문 테이블의 테이블 그룹 아이디는 존재해야 한다
- 단체 지정을 삭제할 수 있다
- 단체 지정의 주문 테이블 아이디와 주문 혹은 식사 주문 상태인 주문이 존재하면 단체 지정을 삭제할 수 없다

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
