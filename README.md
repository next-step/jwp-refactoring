# 1단계 - 테스트를 통한 코드 보호

- [x] `kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다.

- [x] 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다.

    * 메뉴 그룹

        - [x] 메뉴 그룹 서비스 테스트

        - [x] 메뉴 그룹 인수 테스트

    * 메뉴

        - [x] 메뉴 서비스 테스트

        - [x] 메뉴 인수 테스트

    * 주문

        - [x] 주문 서비스 테스트

        - [x] 주문 인수 테스트

    * 상품

        - [x] 상품 서비스 테스트

        - [x] 상품 인수 테스트

    * 테이블 그룹

        - [x] 테이블 그룹 서비스 테스트

        - [x] 테이블 그룹 인수 테스트

    * 주문 테이블

        - [x] 주문 테이블 서비스 테스트

        - [x] 주문 테이블 인수 테스트

---

# 키친포스

## 요구 사항

* 메뉴 그룹

    * 메뉴 그룹을 생성한다.

    * 메뉴 그룹 목록을 조회한다.

* 메뉴

    * 메뉴를 생성한다.

        * 메뉴 가격은 `0`이상이어야 한다.

        * 메뉴는 메뉴 그룹에 속해야 한다.

        * 메뉴 가격은 각 메뉴 상품들 가격에 상품 수량을 곱해서 더한 금액을 초과할 수 없다. 

    * 메뉴 목록을 조회한다.

* 주문

    * 주문을 생성한다.

        * 주문 항목이 존재해야 한다.

        * 주문 항목 수와 메뉴 수가 일치해야 한다.

        * 주문 테이블이 존재해야 한다.

        * 주문 테이블이 비어있지 않아야 한다.

        * 주문에 주문 테이블을 지정한다.

        * 주문 상태를 `COOKING`으로 입데이트한다.

        * 주문 시간을 현재 시간으로 업데이트한다.        
 
    * 주문 목록을 조회한다.

    * 주문 상태를 수정한다.

        * 주문이 있어야 한다.

        * 주문 상태가 `COMPLETION`이 아니어야 한다.

* 상품

    * 상품을 생성한다.

        * 상품 가격은 `0`보다 커야 한다.
 
    * 상품 목록을 조회한다.

* 테이블 그룹

    * 테이블 그룹을 생성한다.

        * 테이블 그룹이 될 주문 테이블 수는 `2` 이상이어야 한다.

        * 테이블 그룹이 될 주문 테이블이 모두 존재해야 한다.

        * 주문 테이블에 다른 테이블 그룹이 없어야 한다.

        * 주문 테이블이 비어있어야 한다.

    * 테이블 그룹을 해제한다.

        * 테이블 그룹에 속해있는 주문 테이블이 `COOKING`이거나 `MEAL`이면 해제할 수 없다.

        * 주문 테이블의 테이블 그룹을 해제한다.

* 주문 테이블
 
    * 주문 테이블을 생성한다.

        * 주문 테이블 생성시 테이블 그룹은 설정하지 않는다. 
 
    * 주문 테이블 목록을 조회한다.
 
    * 기존 주문 테이블을 수정한다.

        * 기존 주문 테이블은 테이블 그룹이 없어야 한다.

        * 기존 주문 테이블 상태가 `COOKING` 또는 `MEAL`이 아니어야 한다.
 
    * 주문 테이블 손님 수를 수정한다.

        * 주문 테이블 손님 수는 `0`이상이어야 한다.

        * 주문 테이블이 존재해야 한다.

        * 주문 테이블이 비어있지 않아야 한다.

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
