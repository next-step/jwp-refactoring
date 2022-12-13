# 키친포스

## 요구 사항

## 상품

| 한글명    | 영문명     | 설명         |
|--------|---------|------------|
| 상품     | Product | 상품 도메인 엔티티 | 
| 상품 아이디 | id      | 상품의 고유 번호  |
| 상품 이름  | name    | 상품의 이름     |
| 상품 가격  | price   | 상품의 가격     |
| 상품 가격  | price   | 상품의 가격     |

- 상품을 등록할 수 있다.
- 상품의 가격은 0원 이상이어야 한다.
- 상품의 목록을 조회할 수 있다.

### 상품 생성하기 (POST /api/products)

- HTTP Response Code : 201 CREATED

### 상품 리스트 조회 (GET /api/products)

- HTTP Response Code : 200 OK

## 메뉴 그룹

| 한글명      | 영문명       | 설명           |
|----------|-----------|--------------|
| 메뉴 그룹    | MenuGroup | 여러 메뉴의 묶음    |
| 메뉴그룹 아이디 | id        | 메뉴그룹 고유의 식별자 |
| 메뉴그룹 이름  | name      | 메뉴 그룹의 이름    |

여러개의 메뉴를 묵음으로 관리할 수 있다. 이를 '메뉴 그룹' 이라고 한다.
메뉴 그룹을 생성할 수 있다.
메뉴 그룹의 목록을 조회할 수 있다.

### 메뉴 그룹 생성하기 (POST /api/menu-groups)

- HTTP Response Code : 201 CREATED

### 메뉴 그룹 리스트 조회하기 (GET /api/menu-groups)

- HTTP Response Code : 200 OK

## 메뉴

| 한글명         | 영문명          | 설명                         |
|-------------|--------------|----------------------------|
| 메뉴          | Menu         | 메뉴                         |
| 메뉴 아이디      | id           | 메뉴 고유 번호                   |
| 메뉴 이름       | name         | 메뉴의 이름                     |
| 메뉴 가격       | price        | 메뉴의 가격                     |
| 속한 메뉴그룹 아이디 | menuGroupId  | 해당 메뉴가 속한 메뉴그룹 아이디         |
| 메뉴의 상품 리스트  | menuProducts | 해당 메뉴를 만드는데 필요한 모든 상품의 리스트 |

- 메뉴를 등록할 수 있다.
- 메뉴의 가격은 0원 이상이어야 한다.
- 하나의 메뉴에는 여러개의 상품을 포함할 수 있다.
- 메뉴의 가격은 포함하는 모든 상품의 총합을 초과할 수 없다.
- 메뉴를 등록할 때 메뉴 그룹을 지정할 수 있다.

### 메뉴 생성하기 (POST /api/menus)

- HTTP Response Code : 201 CREATED

### 메뉴 리스트 조회하기 (GET /api/menus)

- HTTP Response Code : 200 OK

## 메뉴 상품

| 한글명    | 영문명         | 설명                           |
|--------|-------------|------------------------------|
| 메뉴 상품  | MenuProduct | 메뉴에 속한 상품 리스트에 대한 관계 도메인 엔티티 |
| 상품의 순서 | seq         | 메뉴 내에서 상품이 위치한 순서            |
| 메뉴 아이디 | menuId      | 상품이 포함된 메뉴의 아이디              |
| 상품 아이디 | productId   | 상품의 고유번호                     |
| 상품 수량  | quantity    | 해당 메뉴에 포함된 상품의 개수            |

## 테이블 그룹

| 한글명         | 영문명         | 설명                              |
|-------------|-------------|---------------------------------|
| 테이블그룹       | TableGroup  | 여러 테이블의 묶음. 여러 테이블에 걸쳐서 주문하는 경우 |
| 테이블 아이디     | id          | 테이블 그룹 고유 식별자                   |
| 테이블그룹 생성 시간 | createdDate | 테이블그룹을 생성한 시간                   |
| 테이블 리스트     | orderTables | 해당 테이블 그룹에 속한 테이블 리스트           |

- 여러개의 주문 테이블을 묶어서 단체 손님을 관리할 수 있다.
- 단체 손님 정보를 등록할 수 있다.
- 단체 손님으로 지정하려면 적어도 두 개 이상의 주문 테이블을 묶어야 한다. 한 개의 주문 테이블은 단체 손님으로 지정할 수 없다.
- 비어 있는 테이블은 단체 손님 정보에 포함할 수 없다.
- 하나의 주문 테이블을 동시에 두 개 이상의 단체 손님으로 지정할 수 없다.
- 단체 손님으로 지정했던 주문 테이블을 별도로 분리하는 것도 가능하다.
- 계산을 완료한 주문 테이블만 단체 손님에서 분리할 수 없다.

### 테이블 그룹 생성하기 (POST /api/table-groups)

- HTTP Response Code : 201 CREATED

### 테이블 그룹 해제하기 (DELETE /api/table-groups/{tableGroupId})

- HTTP Response Code : 204 No Content

## 주문

| 한글명           | 영문명            | 설명                                       |
|---------------|----------------|------------------------------------------|
| 주문            | Order          | 주문 정보 도메인 엔티티                            |
| 주문 아이디        | id             | 주문 고유 번호                                 |
| 주문 테이블 아이디    | orderTableId   | 주문을 요청한 테이블 아이디                          |
| 주문 상태         | orderStatus    | COOKING, MEAL, COMPLETION 중에 하나의 상태를 가진다 |
| 주문 요청 시간      | orderedTime    | 주문 요청이 이루어진 시간                           |
| 주문 내 메뉴 수량 정보 | orderLineItems | 주문 내 메뉴 리스트. 개별 메뉴의 수량 정보를 포함한다          |

## 주문 항목

| 한글명    | 영문명           | 설명                           |
|--------|---------------|------------------------------|
| 주문 항목  | OrderLineItem | 주문에 포함된 메뉴의 상세 정보 관계 도메인 엔티티 |
| 메뉴의 순서 | seq           | 주문 항목 내 메뉴의 순서               |
| 주문 아이디 | orderId       | 해당 주문 항목이 속하는 주문의 고유 식별자     |
| 메뉴 아이디 | menuId        | 해당 주문 항목이 가리키는 메뉴의 고유 식별자    |
| 주문 수량  | quantity      | 주문한 메뉴의 수량                   |

- 주문 요청이 들어오면 서버에 정보를 저장한다.
- 주문에는 여러개의 항목을 포함할 수 있다.
- 1개 이상의 항목을 선택해야만 주문을 요청할 수 있다.
- 주문 목록을 조회할 수도 있다.
- 외부 시스템에서 처리를 한 뒤에 주문 상태를 변경하는 것도 가능하다.
- 조리중 또는 식사중 상태인 주문만 상태를 변경 가능하다. 계산 완료한 뒤에는 더 이상 주문 상태를 변경할 수 없다.

### 주문 생성하기 (POST /api/orders)

- HTTP Response Code : 201 CREATED

### 주문 리스트 조회하기 (GET /api/orders)

- HTTP Response Code : 200 OK

### 주문 상태 변경하기 (PUT /api/orders/{orderId}/order-status)

- HTTP Response Code : 200 OK

## 주문 테이블

| 한글명           | 영문명            | 설명                                                       |
|---------------|----------------|----------------------------------------------------------|
| 주문 테이블        | OrderTable     | 주문은 기본적으로 테이블 단위로 요청한다. 테이블을 여러개 묶어서 테이블 그룹으로 관리할 수도 있다. |
| 주문 테이블 아이디    | id             | 주문테이블 고유 번호                                              |
| 주문 테이블 그룹 아이디 | tableGroupId   | 주문 테이블이 속한 테이블 그룹의 고유 식별자                                |
| 손님 수          | numberOfGuests | 해당 테이블에 앉은 손님의 숫자                                        |
| 빈 테이블 여부      | empty          | 해당 테이블에 손님이 있는지 없는지 확인하기 위한 플래그 변수                       |

- 주문 테이블 정보를 등록할 수 있다.
- 주문 테이블의 목록을 조회할 수 있다.
- 계산을 마친 주문 테이블은 비어있는 테이블로 표시할 수 있다.
- 단체 손님으로 지정한 주문 테이블은 빈 테이블로 표시할 수 없다.
- 조리중 또는 식사중 상태인 주문 테이블은 빈 테이블로 만들 수 없고, 계산 완료한 주문 테이블만 빈 테이블로 만들 수 있다.
- 테이블에 앉아 있는 손님의 숫자를 기록할 수 있다.
- 테이블에는 항상 1명 이상의 손님이 앉아있다.
- 빈 테이블은 손님의 숫자를 변경할 수 없다.

## 테이블 생성 (POST /api/tables)

- HTTP Response Code : 201 CREATED

### 테이블 리스트 조회 (GET /api/tables)

- HTTP Response Code : 200 OK

### 빈 테이블로 표시하기 (PUT /api/tables/{orderTableId}/empty)

- HTTP Response Code : 200 OK

### 테이블의 손님 숫자 변경하기 (PUT /api/tables/{orderTableId}/number-of-guests)

- HTTP Response Code : 200 OK

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
