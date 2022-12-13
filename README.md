# 키친포스

> 식당을 전산화 할때는 좋았는데 왜 개발자들이 점점더 개발을 힘들어 할까?
>
> 뭔가 변화가 필요해 보인다...... 미션시작

## 요구 사항

### 상품

* 상품을 등록할 수 있어야 합니다.
  * 상품 가격은 필수로 입력해야 합니다.
  * 상품 가격이 0원 이상이여야 합니다.
* 상품을 조회할 수 있어야 합니다.

### 메뉴 그룹

* 메뉴 그룹을 등록할 수 있어야 합니다.
* 메뉴 그룹 목록을 조회할 수 있어야 합니다.

### 메뉴

* 메뉴를 등록할 수 있어야 합니다.
  * 메뉴 가격은 필수로 입력해야 합니다.
  * 메뉴 가격은 0원 이상이여야 합니다.
  * 메뉴 그룹에 없는 메뉴는 등록할 수 없습니다.
  * 메뉴 상품에 없는 메뉴는 등록할 수 없습니다.
  * 금액[삼품 가격 * 메뉴 상품 수량]이 0원 이상이여야 합니다.
* 메뉴 목록을 조회할 수 있어야 합니다.

### 테이블

* 주문 테이블을 추가할 수 있어야 합니다.
  * 주문 테이블 등록시 단체 지정은 해제 됩니다.
* 주문 테이블을 빈 테이블로 변경할 수 있어야 합니다.
  * 주문 테이블 정보가 없으면 안됩니다.
  * 주문 테이블이 단체로 지정되어 있으면 안됩니다.
  * 주문 테이블의 주문 상태가 조리나 식사면 변경 불가능 합니다.
  * 주문 후 주문 상태는 조리가 됩니다.
  * 주문 후 주문 시간이 표시 됩니다.
* 빈 테이블을 주문 테이블로 변경할 수 있어야 합니다.
  * 주문 테이블 정보가 없으면 안됩니다.
  * 주문 테이블이 단체로 지정되어 있으면 안됩니다.
  * 주문 테이블의 주문 상태가 조리나 식사면 변경 불가능 합니다.
* 주문 테이블의 방문한 손님 수를 변경할 수 있어야 합니다.
  * 방문한 손님 수 는 0이상이여야 합니다.
  * 주문 테이블 정보가 없으면 안됩니다.

### 단체 지정

* 통합 계산을 위해 개별 주문을 단체 지정 할 수 있어야 합니다.
  * 주문 테이블이 없거나 주문 테이블이 2개 미만이라면 단체 지정이 불가능 합니다.
  * 단체 지정 요청한 주문 테이블이 하나라도 없으면 안됩니다.
  * 단체 지정 요청한 주문 테이블이 하나도 없거나 주문 테이블의 그룹이 이미 있으면 안됩니다.
* 단체 지정한 주문 테이블을 단체 지정을 해제할 수 있어야 합니다.
  * 단체 지정한 주문 테이블의 주문 상태가 조리 이거나 식사 라면 단체 지정 해제가 불가능 합니다.

### 주문

* 주문을 할 수 있어야 합니다.
  * 주문 항목은 필수로 입력해야 합니다.
  * 주문 항목중 메뉴에 없는 항목이 있어서는 안됩니다.
  * 주문 테이블 없이 주문을 할 수 없습니다.
  * 주문 테이블이 빈 테이블이면 안됩니다.
* 주문 상태를 수정할 수 있어야 합니다.
  * 상태를 수정할 주문이 없으면 안됩니다.
  * 주문 상태가 계산 완료 상태면 수정이 불가능 합니다.
* 주문 목록을 조회할 수 있어야 합니다.

## 용어 사전


| 한글명         | 영문명           | 설명                                                |
| ---------------- | ------------------ | ----------------------------------------------------- |
| 상품           | product          | 메뉴를 관리하는 기준이 되는 데이터                  |
| 메뉴 그룹      | menu group       | 메뉴 묶음, 분류                                     |
| 메뉴           | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위              |
| 메뉴 상품      | menu product     | 메뉴에 속하는 수량이 있는 상품                      |
| 금액           | amount           | 가격 * 수량                                         |
| 주문 테이블    | order table      | 매장에서 주문이 발생하는 영역                       |
| 빈 테이블      | empty table      | 주문을 등록할 수 없는 주문 테이블                   |
| 주문           | order            | 매장에서 발생하는 주문                              |
| 주문 상태      | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다.   |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다.   |
| 단체 지정      | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목      | order line item  | 주문에 속하는 수량이 있는 메뉴                      |
| 매장 식사      | eat in           | 포장하지 않고 매장에서 식사하는 것                  |
