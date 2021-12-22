# 키친포스

## 요구 사항

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


### 메뉴그룹
* 메뉴그룹을 등록할 수 있다.
* 메뉴그룹 목록을 조회할 수 있다.
* 메뉴그룹 종류
  * 두마리메뉴
  * 한마리메뉴
  * 순살파닭두마리메뉴
  * 신메뉴
  * 추천메뉴

### 메뉴
* 메뉴를 등록할 수 있다.
  * 가격이 음수이거나 없는 메뉴는 등록할 수 없다.
  * 메뉴그룹 Id가 없는 메뉴는 등록할 수 없다.
  * 메뉴제품 합보다 가격이 비싼 메뉴는 등록할 수 없다.
* 메뉴 목록을 조회할 수 있다.

### 메뉴상품
* 메뉴상품 등록할 수 있다.
* 메뉴상품 목록을 조회할 수 있다.
* 메뉴아이디를 가지고 있다
* 상품아이디를 가지고 있다.
* 수량을 가지고 있다.

### 주문
* 주문을 등록할 수 있다.
* 주문 목록을 조회 할 수 있다.
* 주문 상태를 변경 할 수 있다.
  * 조리
  * 식사
  * 완성
* 주문테이블 아이디를 가지고 있다.
* 주문 상태를 가지고 있다.
* 주문한 시간을 가지고 있다.
* 주문 순서 정보를 가지고 있다.

### 주문테이블
* 주문테이블을 등록할 수 있다.
* 주문테이블을 목록을 조회할수 있다.
* 주문테이블을 빈테이블로 변경할 수 있다.
  * 테이블그룹에 속해있는 테이블은 변경할 수 없다.
  * 주문 상태가 조리중이거나 식사중이면 테이블을 변경할 수 없다.
* 주문테이블에 손님 수를 변경할 수 있다.
  * 손님 수는 0보다 작을 수 없다. 
  * 빈테이블이면 손님 수를 변경할 수 없다.

### 테이블그룹
* 테이블그룹을 등록할 수 있다.
* 테이블그룹에 주문테이블을 제거할수 있다.

### 상품
* 상품을 등록할 수 있다.
* 상품의 가격이 올바르지 않으면 등록할 수 없다.
  * 상품의 가격은 0 원 이상이어야 한다.
* 상품의 목록을 조회할 수 있다.

