# 키친포스

## 요구 사항

### Step1 - 테스트를 통한 코드 보호
> - [x] 키친포스의 요구사항을 작성
> - [ ] 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성 
>   - 모든 Business Object에 대한 테스트 코드를 작성
>   - @SpringBootTest를 이용한 통합 테스트 코드 또는 @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성


### 상품(product)
> - [x] 상품 생성(POST /api/products)
>   - [x] 상품의 가격은 0원 이하일 수 없다.
> - [x] 상품 목록 조회 (GET /api/products)

### 메뉴(menu)
> - [x] 메뉴 생성 (POST /api/menus)
>   - [x] 메뉴의 가격은 0원 이상이어야 한다.
>   - [x] 메뉴는 등록된 메뉴 그룹에 속해야 한다.
>   - [x] 메뉴 상품은 모두 등록된 상품이어야 한다.
>   - [x] 메뉴의 가격은 메뉴 상품들의 가격의 합보다 클 수 없다.
> - [x] 메뉴 목록 조회(GET /api/menus)

### 메뉴그룹(menu group)
> - [x] 메뉴 그룹 생성(POST /api/menu-groups)
> - [x] 메뉴 그룹 목록 조회(GET /api/menu-groups)

### 주문 테이블(table)
> - [x] 주문 테이블을 생성(POST /api/tables)
> - [x] 주문 테이블 목록 조회(GET /api/tables)
> - [x] 주문 테이블 상태 변경(PUT /api/tables/{orderTableId}/empty)
>   - [x] 주문 테이블은 미등록 되어 있지 않아야 한다.
>   - [x] 주문 테이블은 단체 지정이 되어 있지 않아야 한다.
>   - [x] 주문 테이블의 주문 상태는 조리 중 이거나 식사 중이면 안된다.
> - [x] 주문 테이블 방문한 손님 수 변경(PUT /api/tables/{orderTableId}/number-of-guests)
>   - [x] 주문 테이블의 방문한 손님 수가 0명 이상이어야 한다.
>   - [x] 주문 테이블은 등록되어 있어야 한다.
>   - [x] 주문 테이블은 빈 테이블이 아니어야 한다.

### 단체 지정(table group)
> - [x] 단체 지정 등록(POST /api/table-groups)
>   - [x] 주문 테이블이 2개 이상 있어야 단체 지정이 가능하다.
>   - [x] 주문 테이블들은 모두 등록된 주문 테이블이어야 한다.
>   - [x] 주문 테이블들은 빈 테이블이어야 한다.
>   - [x] 이미 단체 지정된 주문 테이블은 단체 지정이 불가능 하다.
> - [x] 단체 지정 해제(GET /api/table-groups/{tableGroupId})
>   - [x] 단체 등록된 주문 테이블의 상태가 조리 중이거나 식사중이면 안된다.

### 주문(order)
> - [x] 주문 생성(POST /api/orders)
>   - [x] 주문 항목은 1개 이상 있어야 한다.
>   - [x] 주문 항목 속 메뉴들은 모두 등록된 메뉴어야 한다.
>   - [x] 주문 테이블은 등록된 테이블이어야 한다.
>   - [x] 주문 테이블은 빈, 테이블이 아니어야 한다.
> - [x] 주문 목록을 조회(GET /api/orders)
> - [x] 주문 상태 변경(PUT /api/orders/{orderId}/order-status)

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
