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

## 미션

## 🚀 1단계 - 테스트를 통한 코드 보호

### 상품

* `상품`을 등록할 수 있다.
* `상품`의 `가격`이 올바르지 않으면 등록할 수 없다.
    * 가격은 0 원 이상이어야 한다.
* `상품`의 목록을 조회할 수 있다.

### 메뉴그룹

* `메뉴그룹`을 등록할 수 있다.
* `메뉴그룹`의 목록을 조회할 수 있다.

### 메뉴

* `메뉴`를 등록할 수 있다.
* `메뉴`는 여러개의 상품을 가진다.
    * `메뉴`에 상품이 없을 수 있다.
    * `메뉴`에 상품은 여러개가 있을 수 있다.
* `메뉴`의 정보가 올바르지 않으면 등록할 수 없다.
    * 가격은 0 원 이상이어야 한다.
    * `메뉴`의 가격은 상품목록의 가격(상품가격 * 갯수)의 총합보다 클 수 없다.
* `메뉴`가 속할 `메뉴그룹`이 필수로 있어야 한다.
* `메뉴`의 목록을 조회할 수 있다.

### 주문 테이블

* `주문 테이블`을 등록할 수 있다.
    * `주문테이블` 초기값은 `단체 지정`은 없다.
    * `주문테이블` 초기값은 `빈 테이블` 이다.
    * `주문테이블` 초기값은 `방문한 손님 수`는 `0` 이다.
* `주문 테이블`은 `빈 테이블` 유무를 변경 할 수 있다.
    * `주문테이블`에 `단체 지정`이 되어 있으면 `빈 테이블` 여부를 변경 할 수 없다.
    * `주문 테이블`에서 `주문 항목`들의 `주문상태`가 `조리`,`식사`인 경우 `빈 테이블` 여부를 변경 할 수 없다.
* `주문 테이블` 목록을 조회할 수 있다.
* `주문 테이블`의 `방문한 손님 수`를 변경 할 수 있다.
    * `주문 테이블`의 `방문한 손님 수`는 0명 이상이어야 변경할 수 있다.
    * `주문 테이블`이 `빈 테이블`이 아니어야, 손님 수 를 변경할 수 있다.

### 단체 지정

* `단체 지정`은 등록 할 수 있다.
    * `단체 지정`은 `주문 테이블`이 최소 2개이상 이어야 등록 할 수 있다.
    * `단체 지정`은 `주문 테이블`이 다른 `단체 지정`에 속해있지 않아야 등록 할 수 있다.
    * `단체 지정` 등록에 속한 `주문 테이블`은 모두 `빈 테이블` 아닌 상태로 변경 된다.
    * `단체 지정`에 속할 `주문 테이블`은 모두 등록되어 있어야 한다.
* `단체 지정` 해지 할 수 있다.
    * `단체 지정`에 속한 `주문 테이블`들의 `주문상태`가 `조리`,`식사`인 경우 해지 할 수 없다.

### 주문

* `주문`은 등록 할 수 있다.
    * `주문 항목`은 필수 이다.
    * `주문 항목` 모두 등록되어 있어야한다.
    * `주문`이 속할 `주문 테이블`은 `빈 테이블`상태가 아니어야 한다.
    * `주문`이 속할 `주문 테이블`이 등록되어있어야한다.
    * `주문` 최초 상태는 `조리`다.
* `주문`목록을 조회 할 수 있다.
* `주문`의 `주문 상태`를 변경 할 수 있다.
    * `주문 상태`가 `계산 완료`이면 상태를 변경 할 수 없다.

## 2단계 - 서비스 리팩터링

- 단위 테스트
    - 테스트하기 어려운 코드와 쉬운코드 분리해보기
    - 단위 테스트 가능한 코드에 대해 단위 테스트를 구현하기
- 데이터 베이스 스키마 변경 및 마이그레이션 활용하기
- 모델에 `setter` 넣지 않기
- 객체지향 체조원칙 지향하기
- `JPA`로 리팩토링 하기
    - [X] `JdbcTemplateProductDao` -> `ProductRepository`
    - [X] `JdbcTemplateMenuGroupDao` -> `MenuGroupRepository`
    - [X] `JdbcTemplateMenuDao` -> `MenuRepository`
    - [X] `JdbcTemplateMenuProductDao` -> `MenuProductRepository`
    - [X] `JdbcTemplateOrderTableDao` -> `OrderTableRepository`
    - [X] `JdbcTemplateTableGroupDao` -> `TableGroupRepository`
    - [X] `JdbcTemplateOrderDao` -> `OrderRepository`
    - [X] `JdbcTemplateOrderLineItemDao` -> `OrderLineItemRepository`
- [X] 서비스 레이어로직 도메인으로 분리
- [X] 인수 테스트 리팩토링
- [X] `Request`, `Response` DTO 분리 