# 키친포스

## 요구 사항

### 상품

* 상품을 등록할 수 있다.
* 상품의 가격이 올바르지 않으면 등록할 수 없다.
  * 상품의 가격은 0 원 이상이어야 한다.
  * 상품의 가격이 지정되어 있어야 한다.
* 상품의 목록을 조회할 수 있다.

### 메뉴 그룹

* 메뉴 그룹을 등록할 수 있다.
* 메뉴 그룹 목록을 조회할 수 있다.

### 메뉴

* 메뉴를 등록할 수 있다.
  * 메뉴의 가격은 0 원 이상이어야 한다.
  * 등록된 메뉴그룹이 선택되어 있어야 한다.
  * 상품목록의 각 상품이 이미 등록되어 있어야 한다.
  * 메뉴의 가격이 상품목록의 총합 가격보다 더 크면 안된다.
* 메뉴 목록을 조회할 수 있다.

### 주문 테이블

* 주문 테이블을 등록할 수 있다.
* 주문 테이블 목록을 조회할 수 있다.
* 주문 테이블을 비울수 있다.
  * 존재하지 않는 주문 테이블을 비울 수 없다.
  * 주문 테이블이 없거나, 이미 조리중, 식사중인 상태이면 주문 테이블을 비울 수 없다.
* 주문 테이블의 방문한 손님수를 변경할 수 있다.
  * 손님수를 0 이하로 변경 할 수 없다.
  * 존재하지 않는 주문 테이블의 손님수를 변경할 수 없다.

### 단체 지정

* 복수의 주문 테이블을 단체 지정할 수 있다.
  * 단체 지정할 주문 테이블은 2개 이상이어야 한다.
* 이미 단체 지정된 것을 풀 수 있다.
  * 지정한 주문 테이블들이 모두 완료상태여야 그룹 해제가 가능하다.

### 주문

* 주문을 신청한다.
  * 주문 항목에 아무것도 없으면 주문할 수 없다.
  * 주문 항목에 등록하지 않은 메뉴가 있다면 주문할 수 없다.
  * 주문 테이블이 지정되어 있지 않으면 주문할 수 없다.
* 주문 목록을 조회할 수 있다.
* 주문 상태를 변경한다.
  * 신청하지 않은 주문은 상태를 변경할 수 없다.
  * 이미 왼료상태인 주문은 상태를 변경할 수 없다.

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

## 1단계 - 테스트를 통한 코드 보호 To Do List

- [x] 키친포스의 요구 사항 작성
- [x] 상품 테스트 작성
  - [x] Service Test 작성
  - [x] Controller Test 작성
- [x] 메뉴 테스트 작성
  - [x] Service Test 작성
  - [x] Controller Test 작성
- [x] 메뉴 그룹 테스트 작성
  - [x] Service Test 작성
  - [x] Controller Test 작성
- [ ] 주문 테이블 테스트 작성
  - [x] Service Test 작성
  - [ ] Controller Test 작성
- [ ] 단체 지정 테스트 작성
  - [x] Service Test 작성
  - [ ] Controller Test 작성
- [ ] 주문 테스트 작성
  - [x] Service Test 작성
  - [ ] Controller Test 작성
