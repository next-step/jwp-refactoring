# 키친포스

## 요구 사항
## 상품
    - 상품을 생성할 수 있다.
        - 상품의 가격은 0원 이상이어야 한다.
    -  상품을 조회할 수 있다.
## 메뉴
    -  메뉴를 생성할 수 있다.
        -  메뉴의 가격은 0원 이상이어야 한다.
        -  메뉴의 가격이 각 상품 가격의 합보다 클 수 없다.
        -  메뉴는 메뉴 그룹에 포함되어야 한다.
    -  메뉴를 조회할 수 있다.
## 메뉴그룹
    -  메뉴그룹을 생성할 수 있다.
    -  메뉴그룹을 조회할 수 있다.
## 주문
    -  주문을 생성할 수 있다.
        -  주문 항목은 1건 이상이어야 한다.
        -  존재하는 메뉴만 주문할 수 있다.
        -  주문 테이블이 비어 있으면 실패한다.
    -  주문을 조회할 수 있다.
    -  주문을 변경할 수 있다.
        -  주문 상태가 COMPLETION인 주문은 변경할 수 없다.
## 테이블
    -  주문 테이블을 생성할 수 있다.
    -  주문 테이블을 조회할 수 있다.
    -  주문 테이블을 빈 테이블로 변경할 수 있다.
        -  지정된 단체 테이블이 존재하면 실패한다.
        -  주문에 테이블이 존재하면서 조리중이거나 식사중 상태이면 변경할 수 없다.
    -  주문 테이블 손님 수를 변경할 수 있다.
        -  손님 수는 0 이상이어야 한다.
        -  빈 테이블은 변경할 수 없다.
## 단체 테이블
    -  단체 테이블을 생성할 수 있다.
    -  요청한 주문 테이블 수가 2개 미만이면 생성할 수 없다.
    -  요청한 주문 테이블 중 저장되지 않은 것이 존재하면 생성할 수 없다.
    -  요청한 주문 테이블이 비어있지 않거나 이미 그룹핑 되었으면 생성할 수 없다.
    -  단체 테이블을 해제할 수 있다.
        -  주문에 테이블이 존재하면서 조리중이거나 식사중 상태이면 해제할 수 없다.
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
