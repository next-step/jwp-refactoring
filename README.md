# 키친포스

## 1단계 - 테스트를 통한 코드 보호

### 요구사항
1. kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성
2. 요구 사항을 토대로 모든 Business Object에 대한 테스트 코드를 작성

### 프로그래밍 요구사항
* 이번 과정에서는 Lombok 없이 미션을 진행

### 힌트
1. http 디렉터리의 .http 파일(HTTP client)을 보고 어떤 요청을 받는지 참고
2. src/main/resources/db/migration 디렉터리의 .sql 파일을 보고 어떤 관계로 이루어져 있는지 참고

### 키친포스의 요구사항
1. MenuGroup
    * GET
        * endpoint : /api/menu-groups
        * requirements
            * 전체 메뉴 그룹을 조회할 수 있다.
    * POST
        * endpoint : /api/menu-groups
        * parameter
            ```
            {
               "name": "추천메뉴"
            }
            ```
        * requirements
            * 새로운 메뉴 그룹을 추가할 수 있다.
2. Menu
    * GET
        * endpoint : /api/menus
        * requirements
            * 전체 메뉴를 조회할 수 있다.
    * POST
       * endpoint : /api/menus
       * parameter
           ```
           {
               "name": "후라이드+후라이드",
               "price": 19000,
               "menuGroupId": 1,
               "menuProducts": [
                  {
                     "productId": 1,
                     "quantity": 2
                  }
               ]
            }
           ```
       * requirements
            * 새로운 메뉴를 추가할 수 있다.
            * 추가되는 메뉴는 메뉴 그룹에 포함될 수 있다.
            * 추가되는 메뉴가 가지는 메뉴 상품을 추가할 수 있다.
            * 메뉴 가격은 필수값이며, 음수여서는 안된다.
            * 존재하지 않는 메뉴 그룹에 새로운 메뉴를 포함할 수 없다.
            * 존재하지 않는 상품으로 구성된 새로운 메뉴는 추가할 수 없다.
            * 메뉴 가격은 메뉴 상품들의 총 가격의 합보다 크면 안된다.
   
3. Order
   * GET
      * endpoint : /api/orders
      * requirements
         * 전체 주문을 조회할 수 있다.
   * POST
      * endpoint : /api/orders
      * parameter
         ```
         {
            "orderTableId": 1,
            "orderLineItems": [
               {
                  "menuId": 1,
                  "quantity": 1
               }
            ]
         } 
         ```
      * requirements
         * 새로운 주문을 추가할 수 있다.
         * 주문항목은 존재하는 메뉴들로만 구성되야 한다.
         * 존재하지 않는 주문 테이블로 요청할 수 없다.
   * PUT
      * endpoint : /api/orders/{orderId}/order-status
      * parameter
         ```
         {
            "orderStatus": "MEAL"
         }
         ```
      * requirements
         * 주문 현황을 수정할 수 있다.
         * 존재하지 않는 주문으로 요청할 수 없다.
         * 완료된 상태의 주문 현황은 수정할 수 없다.
   
4. Product
   * GET
      * endpoint : /api/products
      * requirements
         * 전체 상품을 조회할 수 있다.
   * POST
      * endpoint : /api/products
      * parameter
         ```
         {
            "name": "강정치킨",
            "price": 17000
         }
         ```
      * requirements
         * 새로운 상품을 추가할 수 있다.
         * 상품 가격은 필수값이며, 음수여서는 안된다.
   
5. Table Group
   * POST
      * endpoint : /api/table-groups
      * parameter
         ```
         {
            "orderTables": [
               {
                  "id": 1
               },
               {
                  "id": 2
               }
            ]
         }
         ```
      * requirements
         * 단체 지정을 할 수 있다.
         * 주문 테이블은 필수값이며, 2개 미만이어선 안된다.
         * 주문 테이블은 존재하는 주문 테이블들로만 구성되야 한다.
         * 다른 테이블 그룹에 속한 주문 테이블로는 요청할 수 없다.
   * DELETE
      * endpoint : /api/table-groups/{tableGroupId}
      * requirements
         * 단체 지정을 제거할 수 있다.
         * 주문 테이블의 상태가 조리중이거나 식사중이면 안된다.
   
6. Table
   * GET
      * endpoint : /api/tables
      * requirements
         * 전체 주문 테이블을 조회할 수 있다.
   * POST
      * endpoint : /api/tables
      * parameter
         ```
         {
            "numberOfGuests": 0,
            "empty": true
         }
         ```
      * requirements
         * 새로운 주문 테이블을 추가할 수 있다.
   * PUT
      * endpoint : /api/tables/{tableId}/empty
      * parameter
         ```
         {
            "empty": false
         }
         ```
      * requirements
         * 주문 테이블의 공석여부를 변경할 수 있다.
         * 주문 테이블은 단체 지정이 되어있으면 안된다.
         * 주문 테이블의 상태가 조리중이거나 식사중이면 안된다.
   * PUT
      * endpoint : /api/tables/{tableId}/number-of-guests
      * parameter
         ```
         {
            "numberOfGuests": 4
         }
         ```
      * requirements
         * 주문 테이블의 게스트 수를 변경할 수 있다.
         * 게스트 수는 음수여선 안된다.
         * 존재하지 않는 주문 테이블로 요청할 수 없다.
         * 빈 주문 테이블이어선 안된다.

---

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
