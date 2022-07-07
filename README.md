# 키친포스

## 1단계 요구 사항
### 상품(Product)
- 상품을 등록할 수 있다.
- 상품의 가격이 올바르지 않으면 등록할 수 없다.
  -상품의 가격은 0원 이상이어야 한다.
- 상품 목록을 조회할 수 있다.

### 메뉴 그룹(Menu Group)
- 메뉴그룹을 등록할 수 있다.
- 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴(Menu)
- 메뉴를 등록할 수 있다.
- 메뉴는 조건이 맞지 않으면 등록할 수 없다.
  - 메뉴의 가격은 0원 이상이어야 한다.
  - 메뉴는 메뉴그룹에 존재하는 메뉴여야 한다.
  - 메뉴의 상품들이 모두 등록되어 있어야 한다.
  - 메의 가격은 메뉴 상품들의 가격의 합보다 클 수 없다.
- 메뉴를 조회할 수 있다.

### 주문 테이블(Order Table)
- 주문 테이블을 등록할 수 있다.
- 주문 테이블을 조회할 수 있다.
- 주문 테이블을 빈테이블로 변경할 수 있다.
- 조건에 맞지않는 주문 테이블을 빈테이블로 변경할 수 없다.
  - 존재하지 않는 주문 테이블을 변경할 수 없다.
  - 테이블 그룹에 속해있는 주문 테이블은 변경할 수 없다.
  - 주문 상태가 요리중이거나 식사중인 주문 테이블은 변경할 수 없다.
- 주문 테이블의 손님수를 변경할 수 있다.
- 조건에 맞지않는 주문 테이블의 손님수는 변경할 수 없다.
  - 손님의 수를 0명 미만으로 변경할 수 없다.
  - 존재하지 않는 주문테이블을 변경할 수 없다.
  - 비어있는 주문 테이블은 변경할 수 없다.

### 단체 지정(Table Group) 
- 테이블 그룹을 등록할 수 있다.
- 조건에 맞지 않는 테이블 그룹은 등록할 수 없다.
  - 주문 테이블이 비어있지 않거나 2개 이상이어야 한다.
  - 주문 테이블이 모두 등록되어 있어야 한다.
  - 주문 테이블이 비어있지않거나 테이블그룹이 등록되어있으면 안된다.
- 테이블 그룹을 삭제할 수 있다.
- 테이블 그룹의 주문 상태가 요리중이거나 식사중이면 삭제할 수 없다.

### 주문(Order)
- 주문을 등록할 수 있다.
- 조건에 맞지 않으면 주문을 등록할 수 없다.
  - 주문 항목이 비어있으면 등록할 수 없다.
  - 주문 항목의 메뉴가 등록되어 있는 메뉴여야 한다.
  - 주문 테이블이 모두 존재하여야 한다.
  - 주문 테이블이 비어있으면 안된다.
- 주문을 조회할 수 있다.
- 주문 상태를 변경할 수 있다.
- 조건에 맞지 않으면 주문의 상태를 변경할 수 없다.
  - 주문이 존재하지 않으면 변경할 수 없다.
  - 주문상태가 완료됨이면 변경할 수 없다.
  
## 2단계 요구사항
- JDBC -> Spring Data JPA
- 도메인 엔티티 변환
- 롬복사용 금지

## 3단계
- 단방향 관계로 변경
- 간접참조로 변경

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
