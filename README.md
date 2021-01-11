# 키친포스

## 요구 사항

### 상품
* 상품을 등록할 수 있다.
* 상품의 가격이 올바르지 않으면 등록할 수 없다.
    * 상품의 가격은 0 원 이상이어야 한다.
* 상품의 목록을 조회할 수 있다.

### 메뉴
* 메뉴 그룹을 등록할 수 있다.
* 메뉴 그룹에 여러개의 메뉴를 등록할 수 있다.
* 메뉴 그룹의 목록을 조회할 수 있다.
* 메뉴의 가격이 올바르지 않으면 등록할 수 없다.
  * 메뉴의 가격은 0원 이상이어야 한다.
  * 메뉴의 총 가격이 메뉴에 속한 상품 가격 * 수량보다 작아야 한다.
* 매뉴의 목록을 조회할 수 있다.


### 주문
* 주문한 메뉴는 1개 이상이어야 한다.
* 주문한 메뉴의 
* 주문의 상태값을 나타낼 수 있다.
  * 요리중, 완료됨
* 주문한 메뉴와 등록된 메뉴의 갯수가 일치하는지 확인한다.

### 테이블 그룹

* 테이블 그룹을 2개 이상이어야 한다.
* 테이블 그룹을 삭제 할 수 있다.
  * 주문 상태가 요리중, 식사중일때 그룹을 삭제 할 수 없다.
  
### 테이블
* 테이블을 등록할 수 있다.
* 테이블에 인원, 상태값을 변경 가능하다.
* 테이블이 그룹에 속하면 상태를 변경할 수 없다.
* 테이블의 주문 상태를 요리중, 식사중일때 바꿀수 없다.
* 테이블 인원은 0보다 작을수 없다.


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
