# 키친포스

## 요구 사항

### 요구사항 1

- [X] 기능 요구사항 정리

#### 기능 명세서

- 메뉴그룹 관리 기능
    + 메뉴그룹 목록을 조회할 수 있다.
    + 메뉴그룹을 등록할 수 있다.
        * 메뉴그룹의 이름은 필수값이다.
- 메뉴 관리 기능
    + 메뉴 목록을 조회할 수 있다.
    + 메뉴를 등록할 수 있다.
        * 메뉴의 이름, 가격, 메뉴그룹은 필수값이다.
        * 메뉴의 금액은 양의 정수만 가능하다.
        * 메뉴에는 등록된 상품만 포함할 수 있다.
        * 하나의 메뉴는 하나 이상의 메뉴상품을 가질 수 있다.
        * 하나의 메뉴 상품은 반드시 하나의 상품을 포함하고 있다.
- 주문 관리 기능
    + 주문 목록을 조회할 수 있다.
    + 주문을 등록할 수 있다.
        * 하나 이상의 주문 메뉴와 수량이 있어야 한다.
        * 등록된 메뉴만 주문 가능하다.
        * 주문 테이블이 존재해야 한다.
        * 최초 주문 등록 시 COOKING 상태를 가진다.
    + 주문을 수정할 수 있다.
        * 등록된 주문만 수정 가능하다.
        * 주문한 메뉴가 완료되면 수정이 불가능하다.
- 상품 관리 기능
    + 상품 목록을 조회할 수 있다.
    + 상품을 등록할 수 있다.
        * 상품의 가격와 이름은 필수값이다.
        * 상품의 가격은 0 이상의 금액이어야 한다.
- 단체지정 관리 기능
    + 통합 계산을 위해 개별 주문 테이블을 그룹화
    + 단체 지정 등록을 할 수 있다.
        * 주문 테이블이 2개 이상이어야 한다.
        * 등록 요청 테이블은 실제 등록된 주문 테이블만 가능한다.
        * 하나의 테이블은 하나의 단체 지정만 가능하다.(이미 단체 지정이 등록된 테이블은 추가 단체지정이 불가능)
        * 비어있는 테이블만 단체 지정이 가능하다.
    + 단체 지정 삭제를 할 수 있다.
        * 주문 테이블이 존재해야 하고, 해당 주문의 상태가 COOKING, MEAL 상태일 경우.
- 테이블 관리 기능
    + 테이블을 등록할 수 있다.
    + 테이블 목록을 조회할 수 있다.
    + 테이블을 비울 수 있다.
        * 대상 테이블은 주문 테이블로 등록되어 있어야 한다.
        * 대상 테이블은 단체지정된 테이블이 아니어야 한다.
        * 주문 테이블이 존재해야 하고, 해당 주문의 상태가 COOKING, MEAL 상태일 경우.
    + 테이블 인원을 수정할 수 있다.
        * 대상 테이블의 기존 인원은 0명 이상이어야 한다.
        * 대상 테이블은 주문 테이블에 등록되어 있어야 한다.

### 요구사항 2

- [X] 레거시 코드와 요구사항을 바당으로 테스트 코드 작성.

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
| 수량 | quantity | 메뉴나 주문의 수량 |
