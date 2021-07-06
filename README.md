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



## 테이블(용어) 설명

### orders (주문)

|id|order_table_id(FK)|order_status|order_time|
|------|---|---|---|
|식별자|주문테이블id|주문상태|주문시간|

### order_line_item (주문 항목)

|seq|order_id(FK)|menu_id(FK)|quantity|
|------|---|---|---|
|순번|주문id|메뉴id|주문수량|

### menu (메뉴)

|seq|name|price|menu_group_id(FK)|
|------|---|---|---|
|순번|이름|가격|메뉴그룹id|

### menu_group (메뉴 그룹)

|id|name|
|------|---|
|식별자|이름|

### menu_product (메뉴_상품)

|seq|menu_id(FK)|product_id(FK)|quantity|
|------|---|---|---|
|순번|메뉴id|상품id|메뉴상품수량|

### order_table (주문 테이블)

|id|table_group_id(FK)|number_of_guests|empty|
|------|---|---|---|
|식별자|테이블그룹id|손님수|비어있는지여부|

### table_group (테이블 그룹)

|id|created_date|
|------|---|
|식별자|생성날짜시간|

### product (상품)

|id|name|price|
|------|---|---|
|식별자|이름|가격|

## 요구 사항 명세

<메뉴그룹>
* 사용자는 메뉴 그룹을 생성 할 수 있다.
* 사용자는 메뉴 그룹 전체를 조회 할 수 있다.

<메뉴>
* 사용자는 메뉴를 만들 수 있다.
    1. 메뉴 가격이 음수 일 수 없다.
    2. 메뉴는 메뉴 그룹에 반드시 포함 되어야 한다.
    3. 메뉴의 상품은 반드시 존재해야 한다.
    4. 상품들의 수량과 가격으로 전체 가격 합계를 구한다.

* 사용자는 메뉴 리스트를 조회 할 수 있다.

<주문>
* 사용자는 주문을 생성 할 수 있다.
    1. 사용자는 주문시 주문테이블id, 그리고 메뉴id와 수량을 요청으로 한다.
    2. 요청시 기입한 주문테이블이 존재해야한다.
    4. 주문 생성 시 주문 상태를 요리중으로 한다.
    5. 주문 항목에 주문 데이터를 저장한다.
  
* 사용자는 주문 리스트를 조회 할 수 있다.

* 사용자는 주문 상태를 변경 할 수 있다.

<상품>
* 사용자는 상품을 생성할 수 있다.
    1. 요청의 상품 가격이 비어있지 않은지 0보다 낮은 가격인지 체크
  
* 사용자는 상품 리스트를 조회 할 수 있다.

<단체 지정>
* 사용자는 단체 지정을 할 수 있다.
    1. 단체의 주문테이블의 id를 요청으로 받는다.
    2. 주문테이블의 요청 id의 개수가 2보다 작은지 체크한다.
    3. 주문테이블의 데이터를 체크한다. 이 때 요청 받은 주문테이블의 데이터가 모두 있는지 체크한다.
    4. 단체 지정 시 테이블의 비어있음을 비어있지 않음으로 한다.


* 사용자는 단체를 취소 할 수 있다.
    1. 단체를 조회한다.
    2. 조회 시 주문 상태가 요리중, 식사중 상태가아닌지 체크한다.
    3. 주문테이블의 취소를 한다.


<테이블>
* 사용자는 테이블을 생성(예약) 할 수 있다.

* 사용자는 테이블 리스트를 조회 할 수 있다.

* 사용자는 테이블을 빈 테이블(empty)로 셋팅 할 수 있다.
    1. 테이블을 조회하여 데이터가 있는지 체크한다.
    2. 테이블의 주문 상태가 Cooking, Meal 상태가 아닌지 체크한다.
    3. 테이블의 비어있음 상태를 비어있음으로 체크한다.


* 사용자는 게스트의 숫자를 변경 할 수 있다.
    1. 게스트의 숫가자 음수인지 체크한다.
    2. 주문테이블id를 통하여 데이터가 있는지 체크한다.
    3. 게스트의 숫자를 변경하고 저장한다.


## 2단계 요구사항
* *spring.jpa.hibernate.ddl-auto=validate* 옵션을 추가한다.
* 각 도메인들을 jpa를 사용 할 수 있도록 바꾼다.
* 요구 명세서의 기능을 하나하나씩 도메인으로 이동하고, 도메인의 테스트 케이스를 작성한다.
* 각 요청과 응답에 대해서 dto를 작성한다.  
  <메뉴>
  * 사용자는 메뉴를 만들 수 있다.
  * 사용자는 메뉴 리스트를 조회 할 수 있다.

  <주문>
  * 사용자는 주문을 생성 할 수 있다.
  * 사용자는 주문 리스트를 조회 할 수 있다.

  <상품>
  * 사용자는 상품을 생성할 수 있다.
  * 사용자는 상품 리스트를 조회 할 수 있다.
  * 사용자는 주문 상태를 변경 할 수 있다.

  <단체 지정>
  * 사용자는 단체 지정을 할 수 있다.
  * 사용자는 단체를 취소 할 수 있다.

  <테이블>
  * 사용자는 테이블을 생성(예약) 할 수 있다.
  * 사용자는 테이블 리스트를 조회 할 수 있다.
  * 사용자는 테이블을 빈 테이블(empty)로 셋팅 할 수 있다.
  * 사용자는 게스트의 숫자를 변경 할 수 있다.