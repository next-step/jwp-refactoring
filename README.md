# 키친포스

## 요구 사항

### 상품
- 상품은 이름, 가격으로 구성된다.
- 상품을 등록하기 위해서는 가격을 반드시 가져야 한다.(가격은 0원 이상이어야 한다.)
- 상품의 전체 목록을 조회할 수 있는 기능이 있다.

### 메뉴
- 메뉴는 이름, 가격, 메뉴그룹, 여러 개의 메뉴상품으로 구성된다.
- 메뉴를 등록하기 위해서는 가격이 반드시 있어야 한다.(가격이 0원 이상이어야 한다.)
- 메뉴를 등록하기 위해서는 메뉴 그룹에 반드시 속해야 한다.
- 메뉴 상품들은 모두 상품에 등록이 되어있어야 한다.
- 등록하려는 메뉴의 가격이 메뉴 그룹의 금액보다 크면 등록할 수 없다.
- 메뉴 목록을 조회할 수 있는 기능이 있다.

### 주문
- 주문을 등록하기 위해서는 주문항목이 반드시 존재해야 한다.
- 주문을 등록하기 위해서는 주문항목이 메뉴에 있어야 한다.
- 주문을 등록하기 위해서는 주문테이블이 반드시 존재해야 한다.
- 주문 후에는 주문상태는 "조리중"으로 변경된다.
- 주문 목록을 조회할 수 있는 기능이 있다.
- 주문 상태를 변경할 수 있으며, 주문이 완료되었을 경우 상태를 변경할 수 없다.

### 단체지정
- 단체지정은 등록할 때 주문테이블이 2개 이상이어야 한다.
- 단체지정을 등록할 때 주문테이블은 모두 존재하는 주문테이블이어야 한다.
- 단체지정을 등록할 때 요청된 주문테이블이 빈 테이블이 아니면 그룹으로 지정할 수 없다.
- 단체지정을 삭제할수 있다. 단, 테이블이 조리중이거나 식사중에는 삭제할 수 없다.

### 주문테이블
- 주문 테이블 등록 시, 단체 지정은 빈 값으로 초기화된다.
- 주문 테이블을 빈 테이블로 만들 수 있다. (주문의 상태가 조리이거나, 식사의 경우에는 빈 테이블로 만들 수 없다.)
- 주문 테이블에는 방문한 손님 수를 변경할 수 있다.
- 만약 처음 방문한 손님의 수가 없을 경우, 변경할 수 없다.
- 테이블이 비었을 경우, 손님의 수를 변경할 수 없다.
- 주문 테이블의 전체 목록을 조회할 수 있다.

### 메뉴그룹
- 메뉴 그룹을 등록할 수 있다.
- 메뉴 그룹의 목록을 조회할 수 있다.

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
