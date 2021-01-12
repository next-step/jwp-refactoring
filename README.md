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
---
# 요구사항

### 상품
* [x] 상품을 등록할 수 있다.
  * [x] 상품가격은 0원 이상이어야 한다.
* [x] 상품 목록을 조회할 수 있다.

### 메뉴 그룹
* [x] 메뉴그룹을 등록할 수 있다.
* [x] 메뉴그룹 목록을 조회할 수 있다.

### 메뉴, 메뉴 상품
* 메뉴를 등록할 수 있다.
    * [x] 메뉴의 가격은 0원 이상이어야 한다.
    * [x] 등록된 메뉴 그룹을 선택할 수 있다.
    * [x] 메뉴 상품 정보를 등록할 수 있다.
        * [x] 메뉴의 가격은 메뉴 상품 가격의 총 금액과 같아야 한다.
    
* [x] 메뉴 목록을 조회할 수 있다.
    * [x] 메뉴 상품 정보를 조회할 수 있다.

### 주문 테이블
* [x] 주문 테이블을 등록할 수 있다.

* [x] 주문 테이블 목록을 조회할 수 있다.

* [x] 주문 테이블을 `빈 테이블` 또는`주문 테이블`로 변경할 수 있다.
  * [x] 단체 지정이 되지 않은 테이블만 변경이 가능하다.
  * [x] `조리`, `식사` 상태의 주문이 발생한 테이블은 상태를 변경할 수 없다.

* [x] 주문 테이블의 게스트 숫자를 변경할 수 있다.
  * [x] 게스트는 0명 이상이어야 한다.
  * [x] 등록된 주문 테이블만 변경이 가능하다.

### 단체 지정
* 단체 지정 정보를 등록한다.
  * [x] 주문 테이블은 2개 이상이어야 한다.
  * [x] 등록된 주문 테이블들을 단체 지정할 수 있다.
  * [x] 단체 지정이 되지 않은 테이블들을 단체 지정할 수 있다.
  * [x] 단체 지정한 시간을 등록한다.
* [x] 단체 지정을 해제한다.
  * [x] 조리, 식사 상태의 주문만 단체 지정 해제가 가능하다.

### 주문, 주문 항목
* [x] 주문을 등록할 수 있다.
    * [x] 주문 항목을 등록할 수 있다.
        * [x] 등록된 메뉴를 주문할 수 있다.
    * [x] 주문을 등록하면 주문 상태는 '조리중' 상태이다.
    * [x] 등록된 주문 테이블을 선택할 수 있다.
    * [x] 주문시간을 기록한다.
    
* [x] 주문 목록을 조회할 수 있다.
    * [x] 주문 항목을 조회할 수 있다.
    
* [x] 주문 상태를 변경할 수 있다.
    * [x] 결제 완료 상태의 주문은 상태를 변경할 수 없다.
