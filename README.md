# 키친포스

## 요구 사항

- 상품
    - [x] 상품을 등록할 수 있다.
        - [x] 상품 가격은 0원 이상이어야 한다.
        - [x] 상품 이름은 빈 값일 수 없다.
    - [x] 상품 목록을 조회할 수 있다.
- 메뉴 그룹
    - [x] 메뉴 그룹을 등록할 수 있다.
        - [x] 메뉴 그룹 이름은 빈 값일 수 없다.
    - [x] 메뉴 그룹 목록을 조회할 수 있다.
- 메뉴
    - [ ] 메뉴를 등록할 수 있다.
        - [ ] 메뉴 이름은 빈 값일 수 없다.
        - [ ] 메뉴 가격은 0원 이상이어야 한다.
        - [ ] 메뉴는 특정 메뉴 그룹에 속해야 한다.
        - [ ] 메뉴에 속한 상품들은 기등록된 상품이어야 한다.
        - [ ] 메뉴의 가격은 메뉴에 속한 상품들의 (가격 * 수량) 합을 넘을 수 없다.
    - [ ] 메뉴 목록을 조회할 수 있다.
- 주문 테이블
    - [ ] 주문 테이블을 등록할 수 있다.
        - [ ] 손님 수와 빈 상태인지 여부를 같이 등록해야 한다.
    - [ ] 주문 테이블 목록을 조회할 수 있다.
    - [ ] 주문 테이블의 빈 상태를 변경할 수 있다.
        - [ ] 주문 테이블 그룹에 속해 있다면 변경할 수 없다.
        - [ ] 주문 테이블에 완료되지 않은 주문이 있는 경우 변경할 수 없다.
    - [ ] 주문 테이블에 손님 수를 변경할 수 있다.
        - [ ] 손님 수는 0명 이상이어야 한다.
        - [ ] 빈 주문 테이블이라면 손님 수를 변경할 수 없다.
- 주문 테이블 그룹
    - [ ] 주문 테이블 그룹을 등록할 수 있다.
        - [ ] 주문 테이블 그룹으로 등록하려는 주문 테이블들의 수가 2개 미만이면 등록할 수 없다.
        - [ ] 주문 테이블 그룹으로 등록하려는 주문 테이블들은 기등록된 주문 테이블들이어야한다.
        - [ ] 주문 테이블 그룹으로 등록하려는 주문 테이블들은 빈 상태여야 하고 이미 주문 테이블 그룹에 등록되어 있지 않아야 한다.
        - [ ] 생성 날짜는 현재로 한다.
    - [ ] 주문 테이블 그룹을 해제할 수 있다.
        - [ ] 주문 테이블 그룹에 속한 주문 테이블들 중 완료되지 않은 주문이 있는 경우 그룹 해제를 할 수 없다.
- 주문
    - [ ] 주문을 등록할 수 있다.
        - [ ] 주문 항목은 1개 이상이어야 한다.
        - [ ] 주문 항목은 기등록된 메뉴여야 한다.
        - [ ] 주문 테이블은 기등록된 주문 테이블이어야 한다.
        - [ ] 주문 테이블이 비어있다면 등록할 수 없다.
        - [ ] 주문 상태는 요리중으로 등록한다.
        - [ ] 주문 날짜는 현재로 한다.
    - [ ] 주문 목록을 조회할 수 있다.
    - [ ] 주문 상태를 변경할 수 있다.
        - [ ] 주문 상태가 완료라면 변경할 수 없다.

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
