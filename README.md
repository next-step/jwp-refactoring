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

## 요구사항
* [x] 메뉴 그룹을 생성한다.
* [x] 메뉴 그룹 목록을 조회한다.
* [x] 메뉴를 생성한다.
    - [x] 메뉴 가격이 0 보다 작은 경우 예외가 발생한다.
    - [x] 메뉴 그룹에 속하지 않은 메뉴인 경우 예외가 발생한다.
    - [x] 메뉴에 포함된 메뉴 상품이 존재하지 않는 상품인 경우 예외가 발생한다.
    - [x] 상품 금액이 메뉴 상품 금액의 총합 보다 큰 경우 예외가 발생한다.
* [x] 메뉴 목록을 조회한다.
    - [x] 각 메뉴에 메뉴 상품 목록을 추가한다.
* [x] 주문을 생성한다.
    - [x] 주문 항목이 없는 경우 예외가 발생한다.
    - [x] 주문 항목 개수가 각 주문 항목이 포함된 메뉴의 개수의 합과 같지 않은 경우 예외가 발생한다.
    - [x] 주문이 발생한 주문 테이블이 없는 경우 예외가 발생한다.
    - [x] 주문 테이블이 비어 있는 경우 예외가 발생한다.
* [x] 주문 목록을 조회한다.
    - [x] 각 주문에 주문항목 목록을 추가한다.
* [x] 주문 상태를 변경한다.
    - [x] 존재하지 않는 주문인 경우 예외가 발생한다.
    - [x] 주문 상태가 완료인 경우 예외가 발생한다.
* [x] 상품을 생성한다.
    - [x] 상품 가격이 0 보다 작은 경우 예외가 발생한다.
* [x] 상품 목록을 조회한다.
* [x] 단체 지정을 생성한다.
    - [x] 주문 테이블이 없거나 2개 이하인 경우 예외가 발생한다.
    - [x] 조회된 테이블 목록 개수와 단체 지정된 주문 테이블 목록 개수가 다른 경우 예외가 발생한다.
    - [x] 주문 테이블이 비어 있지 않거나 단체 지정 되어있는 경우 예외가 발생한다
* [x] 단체 지정을 취소한다.
    - [x] 주문 상태가 조리중 또는 식사중 이면서 단체 지정된 주문 테이블에 포함된 주문인 경우 예외가 발생한다.
* [x] 단체 지정되지 않은 주문 테이블을 생성한다.
* [x] 주문 테이블 목록을 조회한다.
* [x] 주문 테이블을 빈 테이블로 변경한다.
    - [x] 주문 테이블이 존재하지 않는 경우 예외가 발생한다.
    - [x] 단체 지정된 주문 테이블인 경우 예외가 발생한다.
    - [x] 주문 상태가 COOKING 또는 MEAL 이면서 단체 지정된 주문 테이블에 포함된 주문인 경우 예외가 발생한다.
* [ ] 손님 수를 변경한다.
    - [ ] 손님 수가 0보다 작은 경우 예외가 발생한다.
    - [ ] 주문 테이블이 없는 경우 예외가 발생한다.
    - [ ] 주문 테이블이 비어 있는 경우 예외가 발생한다.
    - [ ] 주문 테이블의 손님 수를 변경한다.
    