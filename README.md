# 키친포스
---
### 단계별 미션 내용
**1단계 : 테스트를 통한 코드 보호**
- [X] 키친포스의 요구사항을 README.md 파일에 작성한다.
- [X] 정의된 요구사항을 토대로 테스트 코드를 작성한다.
 

---
## 요구 사항
### 메뉴 그룹 (Menu Group)
- [X] 메뉴 그룹을 등록할 수 있다.
  - [X] 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴 (Menus)
- [X] 메뉴를 등록할 수 있다.
    - [X] 메뉴 가격은 0원 이상이어야 한다.
    - [X] 메뉴는 반드시 메뉴 그룹에 포함되어야 한다.
    - [X] 메뉴를 구성하는 상품이 없는 경우 메뉴로 등록할 수 없다.
    - [X] 메뉴의 가격은 메뉴를 구성하는 상품 가격 * 수량의 총 합보다 클 수 없다.
- [X] 메뉴 목록을 조회할 수 있다.

### 상품 (Products)
- [X] 상품을 등록할 수 있다.
  - [X] 상품의 가격은 0원 이상이어야 한다.
- [X] 상품 목록을 조회할 수 있다.

### 주문 (Order)
- [X] 주문을 할 수 있다.
  : 주문 생성 시 주문 테이블(orderTableId)이 함께 할당되며 주문 상태(OrderStatus)는 COOKING 이며, 주문 시간(OrderedTime)은 현재 시간으로 설정된다.  
  - [X] 주문항목(OrderLineItem)은 1건 이상이어야 한다.
  - [X] 주문항목에 메뉴가 존재하지 않으면 주문을 등록할 수 없다.
  - [X] 주문 테이블이 없이는 주문을 등록할 수 없다.
- [X] 주문 목록을 조회할 수 있다.
- [X] 주문의 상태를 변경할 수 있다.
  - [X] 주문 상태가 "COMPLETION"인 경우 주문 상태를 변경할 수 없다. 

### 테이블 (Table)
- [X] 테이블을 등록할 수 있다.
- [X] 테이블 목록을 조회할 수 있다.
- [X] {순번} 테이블의 상태를 빈 테이블 상태로 변경할 수 있다.
  - [X] 테이블이 존재하지 않으면 빈 테이블 상태로 변경할 수 없다.
  - [X] 테이블 그룹이 존재하면 빈 테이블 상태로 변경할 수 없다.
  - [X] 테이블의 주문 상태가 COOKING 상태이면 빈 테이블 상태로 변경할 수 없다.
- [X] 테이블의 손님 수를 변경할 수 있다.
  - [X] 테이블의 변경하려는 손님 수는 1명 이상이어야 한다.
  - [X] 테이블이 없으면 손님 수를 변경할 수 없다. 
  - [X] 테이블이 비어있으면 테이블 손님 수를 변경할 수 없다.

### 단체 지정 (Table Group)
- [X] 단체를 지정할 수 있다.
  - [X] 주문 테이블이 비어있거나 1개만 존재하면 테이블을 단체로 지정할 수 없다.
  - [X] 단체에 속하는 주문 테이블이 존재하지 않으면 단체로 지정할 수 없다.
  - [X] 단체에 속하는 주문 테이블이 빈 테이블이 아니면 단체로 지정할 수 없다.
  - [X] 단체에 속하는 주문 테이블이 이미 테이블 그룹에 속해있으면 단체로 지정할 수 없다.
- [X] 단체를 해제할 수 있다.
  - [X] 주문 테이블의 주문 상태가 COOKING 이거나 MEAL 인 경우 단체를 해제할 수 없다.

---
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
