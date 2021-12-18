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

## 미션

## 🚀 1단계 - 테스트를 통한 코드 보호

### 상품

* `상품`을 등록할 수 있다.
* `상품`의 `가격`이 올바르지 않으면 등록할 수 없다.
    * 가격은 무조건 있어야한다.
    * 가격은 0 원 이상이어야 한다.
* `상품`의 목록을 조회할 수 있다.

### 메뉴그룹

* `메뉴그룹`을 등록할 수 있다.
* `메뉴그룹`의 목록을 조회할 수 있다.

### 메뉴

* `메뉴`를 등록할 수 있다.
* `메뉴`는 여러개의 상품을 가진다.
    * `메뉴`에 상품이 없을 수 있다.
    * `메뉴`에 상품은 여러개가 있을 수 있다.
* `메뉴`의 정보가 올바르지 않으면 등록할 수 없다.
    * 가격은 무조건 있어야한다.
    * 가격은 0 원 이상이어야 한다.
    * `메뉴`의 가격은 상품목록의 가격(상품가격 * 갯수)의 총합보다 작아야한다.
* `메뉴`가 속할 `메뉴그룹`이 필수로 있어야 한다.
* `메뉴`의 목록을 조회할 수 있다.
