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

# 1단계 - 테스트를 통한 코드 보호

## 요구사항 1 - 키친포스 요구사항 정리

### 상품

1. API table

| Method | URI | Request | Response | Description |
|:---:|:---:|:---:|:---:|---|
| POST | `/api/products` | (Body) Product | 생성된 상품의 데이터와 URI | 상품 생성 | 
| GET | `/api/products`  |         | List<Product> | 상품 목록 조회 |

2. Business 상세 요구사항
    - 상품 (Product)
        - Long id
        - String name (상품명은 255자까지 입력 가능)
        - Decimal price
    - POST `/api/products`
        - 상품 가격(price)은 **0 보다 큰 실수**여야 한다.
    
### 메뉴 그룹

1. API table

| Method | URI | Request | Response | Description |
|:---:|:---:|:---:|:---:|---|
| POST | `/api/menu-groups` | (Body) MenuGroup | 생성된 메뉴 그룹의 데이터와 URI | 메뉴 그룹 생성 |
| GET | `/api/menu-groups`  |           | List<MenuGroup> | 메뉴 그룹 목록 조회 |

2. Business 상세 요구사항
    - 메뉴 그룹 (MenuGroup)
        - Long id
        - String name (메뉴 그룹명은 255자까지 입력 가능)
    
### 메뉴

1. API table

| Method | URI | Request | Response | Description |
|:---:|:---:|:---:|:---:|---|
| POST | `/api/menus` | (Body) Menu | 생성된 메뉴 데이터와 URI | 메뉴 생성 |
| GET | `/api/menus` |  | List<Menu> | 메뉴 목록 조회 |

2. Business 상세 요구사항
    - 메뉴 (Menu)
        - Long id
        - String name (메뉴명은 255자까지 입력 가능) 
        - Decimal price
        - Long menuGroupId
        - List<MenuProduct> menuProducts
    - 메뉴 상품 (MenuProduct)
        - Long menuId
        - Long productId
        - long quantity
    - POST `/api/menus`
        - 메뉴를 등록한다.
        - 메뉴의 가격(price)은 **0 보다 큰 실수**여야 한다.
        - 메뉴가 속할 **메뉴 그룹이 미리 등록**되어 있어야 한다.
        - 메뉴의 가격(price)은 (각 메뉴 상품의 가격 * 각 메뉴 상품의 재고 합계) 와 같거나 그보다 작아야 한다.
    
### 주문 테이블

1. API table

| Method | URI | Request | Response | Description |
|:---:|:---:|:---:|:---:|---|
| POST | `/api/tables` | (Body) OrderTable | 생성된 주문 데이터와 URI | 주문 테이블 생성 |
| GET | `/api/tables` |  | List<OrderTable> | 주문 테이블 조회 |
| PUT | `/api/tables/{orderTableId}/empty` | (Body) OrderTable | 변경에 성공한 주문 테이블 데이터 | 주문 테이블 상태를 empty로 변경 |
| PUT | `/api/tables/{orderTableId}/number-of-guests` |  | 변경에 성공한 주문 테이블 데이터 | 주문 테이블의 방문 손님 수 변경 |

2. Business 상세 요구사항
    - 주문 테이블 (OrderTable)
        - Long id
        - Long tableGroupId
        - int numberOfGuests
        - boolean empty 
    - POST `/api/tables`
        - 주문 테이블을 등록한다.
        - 최초 등록 시 단체 지정이 되어 있지 않은 상태이다.
    - PUT `/api/tables/{orderTableId}/empty`
        - 주문 테이블 그룹에 **속하지 않은** 상태여야 한다.
        - 주문 상태가 `COOKING`, `MEAL` 이 아니어야 한다.
    - PUT `/api/tables/{orderTableId}/number-of-guests`
        - 주문 테이블의 상태가 `empty` 가 아니어야 한다.

### 주문

1. API table

| Method | URI | Request | Response | Description |
|:---:|:---:|:---:|:---:|---|
| POST | `/api/orders` | (Body) Order | 생성된 주문 데이터와 URI | 주문 생성 |
| GET | `/api/orders` | | List<Order> | 주문 목록 조회 |
| PUT | `/api/orders/{orderId}/order-status` | (Body) Order | 변경에 성공한 주문 데이터와 URI | 주문 상태 변경 |

2. Business 상세 요구사항
    - 주문 (Order)
        - Long id
        - Long orderTableId
        - String orderStatus
        - LocalDateTime orderedTime
        - List<OrderLineItem> orderLineItems
    - 주문 항목 (OrderLineItem)
        - Long seq
        - Long orderId
        - Long menuId
        - long quantity
    - POST `/api/orders`
        - 주문 항목이 1개 이상 존재해야 한다.
        - 각 주문 항목은 메뉴 당 1개씩 존재한다. 
            - 같은 메뉴를 여러 개 시켰다면 주문 항목의 quantity를 증가시킨다.
        - 주문 테이블 상태는 `empty`가 아니어야 한다.
        - 최초 주문 등록 시 주문 상태는 `COOKING`로 등록된다.
        - 모든 주문 항목을 주문에 속하게 해야 한다.
    - PUT `/api/orders/{orderId}/order-status`
        - 주문 상태가 `COMPLETION` 이면 상태 변경이 불가능하다.
        - 변경 가능한 상태는 `COOKING`, `MEAL` 이다.

### 단체 지정

1. API table

| Method | URI | Request | Response | Description |
|:---:|:---:|:---:|:---:|---|
| POST | `/api/table-groups` | (Body) TableGroup | 생성된 단체 지정 데이터와 URI | 단체 지정 생성 |
| DELETE | `/api/table-groups/{tableGroupId}` | | | 단체 지정 삭제 |

2. Business 상세 요구사항
    - 단체 지정 (TableGroup)
        - Long id
        - LocalDateTime createdDate
        - List<OrderTable> orderTables
    - POST `/api/table-groups`
        - 주문 테이블이 2개 이상이어야만 생성 가능하다.
        - 입력한 주문 테이블의 개수와 실제 저장되어 있는 주문 테이블의 개수가 같아야 한다.
        - 모든 주문 테이블 상태는 `empty` 가 아니어야 한다.
        - 모든 주문 테이블을 `empty` 가 아닌 상태로 만들고 같은 단체로 지정한다.
    - DELETE `/api/table-groups/{tableGroupId}'
        - 주문 상태가 `COOKING`, `MEAL`이면 삭제할 수 없다.
        - 주문 테이블의 단체 지정을 해제한다.

## 요구사항 2 - 모든 Business Object의 테스트코드 작성

> Presentation layer 부터 하위 layer로 내려가며 단계적으로 테스트코드 작성

- [x] MenuGroupRestController
- [ ] MenuRestController
- [ ] OrderRestController
- [x] ProductRestController
- [ ] TableGroupRestController
- [ ] TableRestController
