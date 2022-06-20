# 키친포스

## 요구 사항
### 기능요구사항
#### 메뉴 그룹
- 메뉴 그룹명을 입력하여 메뉴 그룹을 등록할 수 있다.
- 메뉴 그룹 목록을 조회할 수 있다.

##### API 명세
```
POST /api/menu-groups HTTP/1.1
Content-Type: application/json
Accept: application/json

{
    "name" : "추천 메뉴"
}

HTTP/1.1 201 Created
Location: /api/menu-groups/1
Content-Type: application/json

{
    "id" : 1,
    "name" : "추천 메뉴"
}
``` 
```
GET /api/menu-groups HTTP/1.1
Accept: application/json

HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id" : 1,
        "name" : "추천 메뉴"
    },
    {
        "id" : 2,
        "name" : "반반 메뉴"
    }
]
```

#### 메뉴
- 메뉴명, 메뉴 가격, 메뉴 그룹 아이디, 메뉴 상품을 입력하여 메뉴를 등록할 수 있다.
- 입력 정보가 올바르지 않으면 등록할 수 없다.
    - 메뉴 그룹이 존재해야 한다.
    - 메뉴 가격은 0원 이상 이어야 한다.
    - 메뉴 상품이 존재해야 한다.
    - 메뉴 가격은 메뉴를 구성하는 상품의 가격의 총 합보다 작아야한다.
- 메뉴 목록을 조회할 수 있다.

##### API 명세
```
POST /api/menus HTTP/1.1
Content-Type: application/json
Accept: application/json

{
    "name" : "후라이드+후라이드",
    "price" : "19000",
    "menuGroupId" : 1,
    "menuProducts" : [
        {
            "productId" : 1,
            "quantity" : 1
        },
        {
            "productId" : 2,
            "quantity" : 1
        }
    ]
}

HTTP/1.1 201 Created
Location: /api/menus/1
Content-Type: application/json

{
    "id" : 1,
    "name" : "후라이드+후라이드",
    "price" : "19000",
    "menuGroupId" : 1,
    "menuProducts" : [
        {
            "productId" : 1,
            "quantity" : 1
        },
        {
            "productId" : 2,
            "quantity" : 1
        }
    ]
}
``` 
```
GET /api/menus HTTP/1.1
Accept: application/json

HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id" : 1,
        "name" : "후라이드+후라이드",
        "price" : "19000",
        "menuGroupId" : 1,
        "menuProducts" : [
            {
                "productId" : 1,
                "quantity" : 1
            },
            {
                "productId" : 2,
                "quantity" : 1
            }
        ]
    }
]
```

#### 상품
- 상품명, 가격을 입력하여 상품을 등록할 수 있다.
- 상품의 가격이 올바르지 않으면 등록할 수 없다.
    - 상품의 가격은 0원 이상 이어야 한다.
- 상품 목록을 조회할 수 있다.

##### API 명세
```
POST /api/products HTTP/1.1
Content-Type: application/json
Accept: application/json

{
    "name" : "강정치킨",
    "price" : "17000"
}

HTTP/1.1 201 Created
Location: /api/products/1
Content-Type: application/json

{
    "id" : 1,
    "name" : "강정치킨",
    "price" : "17000"
}
``` 
```
GET /api/menus HTTP/1.1
Accept: application/json

HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id" : 1,
        "name" : "강정치킨",
        "price" : "17000"
    },
    {
        "id" : 2,
        "name" : "후라이드치킨",
        "price" : "18000"
    }
]
```

#### 주문 테이블
- 손님수, 빈 테이블 여부를 입력하여 주문 테이블을 등록할 수 있다.
- 테이블 목록을 조회할 수 있다.
- 주문 테이블을 빈 테이블로 수정할 수 있다.
    - 주문 테이블이 존재해야 한다.
    - 그룹 테이블인 경우, 수정할 수 없다.
    - 주문상태가 요리, 식사인 경우 수정할 수 없다.
- 주문 테이블의 손님수를 수정할 수 있다.
    - 테이블 손님수는 0명 이상이어야 한다.
    - 주문 테이블이 존재해야 한다.
    - 테이블이 비어있는 경우, 수정할 수 없다.

##### API 명세
```
POST /api/tables HTTP/1.1
Content-Type: application/json
Accept: application/json

{
    "numberOfGuests" : 0,
    "empty" : true
}

HTTP/1.1 201 Created
Location: /api/tables/1
Content-Type: application/json

{
    "id" : 1,
    "numberOfGuests" : 0,
    "empty" : true
}
``` 
```
GET /api/tables HTTP/1.1
Accept: application/json

HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id" : 1,
        "numberOfGuests" : 0,
        "empty" : true
    },
    {
        "id" : 2,
        "numberOfGuests" : 3,
        "empty" : false
    }
]
```
```
PUT /api/tables/1/empty HTTP/1.1
Content-Type: application/json
Accept: application/json

{
  "empty" : false
}

HTTP/1.1 200 OK
Content-Type: application/json

{
    "id" : 1,
    "numberOfGuests" : 0,
    "empty" : false
}
```
```
PUT /api/tables/1/number-of-guests HTTP/1.1
Content-Type: application/json
Accept: application/json

{
  "numberOfGuests" : 4
}

HTTP/1.1 200 OK
Content-Type: application/json

{
    "id" : 1,
    "numberOfGuests" : 4,
    "empty" : false
}
```

#### 테이블 그룹
- 테이블끼리 테이블 그룹으로 등록할 수 있다.
- 테이블 수가 올바르지 않으면 테이블 그룹을 등록할 수 없다.
    - 테이블 수는 2개 이상이어야 한다.
    - 테이블이 비어있지 않거나 다른 그룹이 없어야 한다.
- 테이블 그룹을 해제할 수 있다.
- 테이블 그룹 정보가 올바르지 않으면 해제할 수 없다.
    - 주문 테이블의 주문이 요리, 식사 상태인 경우 테이블 그룹을 해제할 수 없다.

##### API 명세
```
POST /api/table-groups HTTP/1.1
Content-Type: application/json
Accept: application/json

{
    "orderTables" : 
    [
        {
            "id" : 1
        },
        {
            "id" : 2
        }
    }
}

HTTP/1.1 201 Created
Location: /api/table-groups/1
Content-Type: application/json

{
    "id" : 1
    "orderTables" : 
    [
        {
            "id": 1
        },
        {
            "id": 2
        }
    }
}
``` 
```
DELETE /api/table-groups/1 HTTP/1.1
accept: */*

HTTP/1.1 204 No Content
```

#### 주문
- 주문을 등록할 수 있다.
- 주문 정보가 올바르지 않으면 등록할 수 없다.
    - 주문 상품이 존재해야 한다.
    - 주문 상품이 메뉴에 존재해야 한다.
    - 주문 테이블이 존재해야 한다.
- 주문 목록을 조회할 수 있다.
- 주문 정보를 수정할 수 있다.
    - 주문이 존재해야 한다.
    - 주문 상태가 계산 완료이면 수정할 수 없다.

##### API 명세
```
POST /api/orders HTTP/1.1
Content-Type: application/json
Accept: application/json

{
    "orderTableId" : 1,
    "orderLineItems" : [
        {
            "menuId" : 1,
            "quantity" : 1
        }
    ]
}

HTTP/1.1 201 Created
Location: /api/orders/1
Content-Type: application/json

{
    "id" : 1,
    "orderTableId" : 1,
    "orderLineItems" : [
        {
            "orderId" : 1,
            "menuId" : 1,
            "quantity" : 1
        }
    ],
    "orderStatus" : "COOKING",
    "orderedTime" : "2022-06-20T18:12:32.922507" 
}
``` 
```
GET /api/orders HTTP/1.1
Accept: application/json

HTTP/1.1 200 OK
Content-Type: application/json

[
    {
        "id" : 1,
        "orderTableId" : 1,
        "orderLineItems" : [
            {
                "orderId" : 1,
                "menuId" : 1,
                "quantity" : 1
            }
        ],
        "orderStatus" : "COOKING",
        "orderedTime" : "2022-06-20T18:12:32.922507" 
    }
]
```
```
PUT /api/orders/1/order-status HTTP/1.1
Content-Type: application/json
Accept: application/json

{
    "orderStatus": "MEAL"
}

HTTP/1.1 200 OK
Content-Type: application/json

{
        "id" : 1,
        "orderTableId" : 1,
        "orderLineItems" : [
            {
                "orderId" : 1,
                "menuId" : 1,
                "quantity" : 1
            }
        ],
        "orderStatus" : "MEAL",
        "orderedTime" : "2022-06-20T18:12:32.922507" 
    }
```

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
