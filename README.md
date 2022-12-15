# 키친포스

## 요구 사항

### 상품(product) - _메뉴를 관리하는 기준이 되는 데이터_

| METHOD | URI           | 설명       |
|--------|---------------|----------|
| POST   | /api/products | 상품 생성 요청 |
| GET    | /api/products | 상품 목록 조회 |

```sql
CREATE TABLE product
(
    id    BIGINT(20) NOT NULL AUTO_INCREMENT,
    name  VARCHAR(255)   NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    PRIMARY KEY (id)
);
```

- 상품은 이름과 가격은 필수 값 이다.

- 상품을 등록할 수 있다.
  - 상품을 등록할때는 상품의 이름과 가격이 필요한다.
  - 상품의 가격은 0 이상이어야 한다.
  - 상품이 저장된다.
- 상품 목록을 조회할 수 있다.

### 메뉴 그룹(menu group) - _메뉴 묶음, 분류_

| METHOD | URI              | 설명          |
|--------|------------------|-------------|
| POST   | /api/menu-groups | 메뉴 그룹 생성 요청 |
| GET    | /api/menu-groups | 메뉴 그룹 목록 조회 |
```sql
CREATE TABLE menu_group (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (id)
);
```
- 메뉴 그룹의 이름은 필수 값인다.
- 메뉴 그릅을 등록할 수 있다.
  - 메뉴 그룹을 등록할때에는 메뉴 그룹의 이름이 필요하다.
  - 메뉴 그룹이 저장된다.
- 메뉴 그룹 목록을 조회할 수 있다.


### 메뉴(menu) - _메뉴 그룹에 속하는 실제 주문 가능 단위_
| METHOD | URI        | 설명       |
|--------|------------|----------|
| POST   | /api/menus | 메뉴 생성 요청 |
| GET    | /api/menus | 메뉴 목록 조회 |
```sql
CREATE TABLE menu (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price DECIMAL(19, 2) NOT NULL,
    menu_group_id BIGINT(20) NOT NULL,
    PRIMARY KEY (id)
);
```
- 메뉴의 이름과 가격은 필수 값이다.
- 메뉴는 메뉴 그룹에 속해있어야 한다.
- 메뉴 등록 요청을 할 수 있다
  - 메뉴 등록을 요청할때에는 메뉴 이름, 가격, 메뉴 그룹, 메뉴 상품이 필요하다.
    - 메뉴 상품은 상품과 수량으로 구성되어 있다.
  - 메뉴의 가격은 0 이상이어야 한다.
  - 메뉴 그룹이 존재하지 않으면 에러가 발생한다.
  - 메뉴의 가격은 메뉴 상품에 속한 각 상품의 가격 * 수량을 모두 더한 값보다 크면 안된다.
  - 메뉴가 저장된다.
  - 메뉴 상품이 저장된다.
- 메뉴 목록을 조회할 수 있다.
  - 메뉴 조회시 메뉴에 속한 모든 상품도 조회된다.

### 주문(order) - _매장에서 발생하는 주문_
| METHOD | URI                                | 설명       |
|--------|------------------------------------|----------|
| POST   | /api/orders                        | 주문 생성 요청 |
| GET    | /api/orders                        | 주문 목록 조회 |
| PUT    | /api/orders/{orderId}/order-status | 주문 상태 변경 |
```sql
CREATE TABLE orders (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_table_id BIGINT(20) NOT NULL,
    order_status VARCHAR(255) NOT NULL,
    ordered_time DATETIME NOT NULL,
    PRIMARY KEY (id)
);
```
- 주문의 주문 상태와 주문 시간은 필수 값이다.
- 주문은 주문 테이블에 속해 있어야 한다.
- 주문을 생성할 수 있다.
  - 주문을 생성 요청시에는 주문 항목이 필요하다.
    - 주문 항목은 메뉴와 수량 목록이다.
  - 주문 항목이 없으면 에러가 발생한다.
  - 요정한 주문 항목 중 등록되지 않은 메뉴가 있으면 에러가 발생한다.
  - 주문 테이블이 존재하지 않으면 에러가 발생한다.
  - 주문 테이블이 `Empty`면 에러가 발생한다.
  - 주문의 최초 상태를 `COOKING`으로 설정된다.
  - 주문시간을 현재시간(`now()`)으로 설정된다.
  - 주문이 저장된다.
  - 주문항목이 저장된다.
- 주문 목록을 조회할 수 있다.
- 각 주문의 상태를 변경할 수 있다.
  - 주문의 상태 변경 요청시 주문의 상태가 필요하다.
    - 주문의 상태는 `COOKING`, `MEAL`, `COMPLETION` 가 존재한다.
  - 주문이 존재하지 않으면 에러가 발생한다.
  - 요청한 주문의 상태가 `COMPLETION`이면 에러가 발생한다.
  - 요청한 상태로 주문의 상태가 변경 저장된다.

  
### 주문 테이블(order table) - _매장에서 주문이 발생하는 영역_
| METHOD | URI                                         | 설명                    |
|--------|---------------------------------------------|-----------------------|
| POST   | /api/tables                                 | 주문 테이블 생성 요청          |
| GET    | /api/tables                                 | 주문 테이블 목록 조회 요청       |
| PUT    | /api/tables/{orderTableId}/empty            | 주문 테이블 상태 변경 요청       |
| PUT    | /api/tables/{orderTableId}/number-of-guests | 주문 테이블 방문한 손님 수 변경 요청 |

```sql
CREATE TABLE order_table (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    table_group_id BIGINT(20),
    number_of_guests INT(11) NOT NULL,
    empty BIT(1) NOT NULL,
    PRIMARY KEY (id)
);
```
- 주문 테이블의 방문한 손님 수와 빈 테이블 상태는 필수 값이다.
- 주문 테이블 생성 요청할 수 있다.
  - 테이블 생성 요청시 방문한 손님 수와 빈테이블 상태가 필요하다.
  - 단체 지정은 적용되지 않는다.
- 주문 테이블 목록 조회 요청할 수 잇다.
- 주문 테이블에 빈 테이블 상태를 변경할 수 있다.
  - 요청시 빈 테이블 상태 값 `true` or `false`이 필요하다. 
  - 등록되지 않은 주문 테이블이면 에러가 발생한다.
  - 주문 테이블이 단체 지정이 되어 있으면 에러가 발생한다.
  - 주문의 상태가 `COOKING`이나 `MEAL`에 속하면 에러가 발생한다.
  - 주문 테이블의 상태가 변경 저장된다.
- 주문 테이블의 방문한 손님 수를 변경할 수 잇다.
  - 방문한 손님 수가 필요하다.
  - 방문한 손님 수는 0명 이상이어야 한다.
  - 요청한 주문테이블이 등록되어 있지 않으면 에러가 발생한다.
  - 주문테이블의 상태가 `Empty`이면 변경할 수 없다.
  - 요청한 손님 수로 수정 저장된다.

### 단체 지정(table group) - _통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능_
| METHOD | URI                              | 설명               |
|--------|----------------------------------|------------------|
| POST   | /api/table-groups                | 단체 지정을 생성할 수 있다. |
| DELETE | /api/table-groups/{tableGroupId} | 단체 지정을 해제할 수 있다. |
```sql
CREATE TABLE table_group (
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    created_date DATETIME NOT NULL,
    PRIMARY KEY (id)
);
```
- 단체 지정 생성 요청할 수 있다.
  - 단체 지정 생성 요청시 주문 테이블 목록이 요청된다.
  - 주문 테이블은 2개 이상이어야 한다.
  - 등록되지 않은 주문 테이블은 단체지정으로 요청할 수 없다.
  - 주문 테이블들이 빈 테이블이어야 한다.
  - 단체 지정된 주문테이블은 단체 지정할 수 없다.
  - 단체 지정시 생성 날짜가 설정된다.
  - 단체 지정이 저장된다.
  - 요청된 주문 테이블의 테이블 그룹이 지정되고 빈 테이블 상태가 `false`로 변경된다.
- 단체 지정 삭제 요청할 수 있다.
  - 테이블 그룹 아이디로 주문 테이블을 조회한다.
  - 조회된 주문 테이블의 상태가 `COOKING`이나 `MEAL`에 속하면 에러가 발생한다.
  - 주문 테이블의 단체 지정이 `null`로 변경되고 저장된다.

## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |

## ERD
![ERD](src/main/resources/db/kichenpsErd.png)
---

# 🚀 1단계 - 테스트를 통한 코드 보호

### 요구사항

1. 요구사항 정리
    - [x] `kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 `README.md`에 작성한다.
2. 테스트 코드 작성
    - [ ] 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다.
    - [ ] 모든 Business Object에 대한 테스트 코드를 작성한다.
    - [ ] 인수 테스트 코드 작성은 권장하지만 필수는 아니다.

### 힌트

```text
### 상품

* 상품을 등록할 수 있다.
* 상품의 가격이 올바르지 않으면 등록할 수 없다.
    * 상품의 가격은 0 원 이상이어야 한다.
* 상품의 목록을 조회할 수 있다.
```
