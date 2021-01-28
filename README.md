# 키친포스

## 요구 사항
### MeneGroup
-[x]  menu group 생성
-[x]  menu group 리스트 조회

### Menu
-[x]  menu 생성
   * menu의 price는 0 원 이상이어야 한다.
   * menu의 price는 해당 menu에 속한 product price의 sum보다 크면 안된다.
-[x]  menu 리스트 조회
   * menu에는 menu product 정보를 포함해야한다.

### Order
-[x]  order 생성
   * order에는 order line item이 1개 이상 존재해야한다.
   * order에는 order table이 1개 이상 존재해야한다.
-[x]  order 리스트 조회
   * order에는 LineItem 정보를 포함해야한다.
-[ ]  특정 order의 status 변경하기
   * order의 status가 Completeion일 경우 변경할 수 없다.

### Product
-[x]  product 생성
   * product의 price는 0 원 이상이어야 한다.
-[x]  product 리스트 조회

### TableGroup
-[ ]  table group 생성
   * order table의 개수는 2개 이상이어야 한다.
   * order table의 table group은 null이 아니어야 한다.
-[ ]  table group 삭제

### Table
-[ ]  table 생성
-[ ]  table 리스트 조회
-[ ]  table의 order 비우기
   * tableGroup 정보를 포함해야한다.
   * table에 OrderStatus가 Cooking이거나 Meal인 경우가 존재하지 아니어야 한다.
-[ ]  table의 Guest 숫자 변경하기
   * guest의 숫자가 음수이면 안된다.
   * 비우려는 table이 비어있으면 안된다.



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


