# 키친포스

## 요구 사항

### 메뉴 그룹(menu groups)

POST `/api/menu-groups`
메뉴 그룹을 생성한다

GET `/api/menu-groups`
메뉴 그룹 목록을 가져온다


### 메뉴(menus)

POST `/api/menus`
메뉴를 생성한다

GET `/api/menus`
메뉴 목록을 가져온다


### 주문(orders)

POST `/api/orders`
주문을 생성한다

GET `/api/orders`
주문 목록을 가져온다

PUT `/api/orders/1/order-status`
특정 주문의 상태를 변경한다


### 제품(products)

POST `/api/products`
제품을 생성한다

GET `/api/products`
제품 목록을 가져온다


### 단체 지정(table groups)

POST `/api/table-groups`
테이블 그룹을 생성한다

DELETE `/api/table-groups/1`
특정 테이블 그룹을 삭제한다


### 테이블(tables)

POST `/api/tables`
테이블을 생성한다

GET `/api/tables`
테이블 목록을 가져온다

PUT `/api/tables/1/empty`
특정 테이블의 주문가능 상태를 변경한다

PUT `/api/tables/1/number-of-guests`
특정 테이블의 손님 수를 변경한다


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
