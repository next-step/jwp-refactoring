# 키친포스

## 요구 사항

### 메뉴 그룹

* [X] `메뉴 그룹`을 생성할 수 있다.
* [X] 전체 `메뉴 그룹`을 조회할 수 있다.

### 메뉴

* [X] `메뉴`를 생성할 수 있다.
    * [X] `메뉴`의 가격은 **필수사항**이며 **0이상**이어야 한다.
    * [X] `메뉴`는 이미 생성된 `메뉴 그룹`에 포함되어야 한다.
    * `메뉴`는 0개 이상의 `메뉴 상품`으로 구성된다.
        * [X] `메뉴 상품`은 생성된 `상품`과 수량의 정보를 포함한다.
    * [X] `메뉴`의 가격은 `메뉴 상품`의 전체 **금액**보다 클 수 없다.
        * `메뉴 상품`의 **금액**은 상품 가격에 수량을 곱한 값이다.
* [X] 전체 `메뉴`를 조회할 수 있다.

### 주문

* [X] `주문`을 생성할 수 있다.
    * [X] `주문`은 하나 이상의 `주문 항목`을 포함해야 한다.
        * 하나의 `주문 항목`은 하나의 `메뉴`를 포함한다.
        * [X] `주문`은 중복된 `메뉴`를 포함한 `주문 항목`을 가질 수 없다.
    * [X] `주문`은 생성된 `주문 테이블`을 포함한다.
        * [X] `주문 테이블`이 `빈 테이블`이면 주문을 생성할 수 없다.
    * [X] 생성된 `주문`의 `주문 상태`는 **조리**이다.
* [X] 전체 `주문`을 조회할 수 있다.
* [X] 생성된 `주문`의 `주문 상태`를 변경할 수 있다.
    * [X] **계산 완료**가 된 `주문`은 변경할 수 없다.

### 상품

* [X] `상품`을 생성할 수 있다.
    * [X] `상품`의 **가격은 0 이상**이어야 한다.
* [X] 전체 `상품`을 조회할 수 있다.

### 단체 지정

* [ ] `단체 지정`은 **2개 이상**의 `주문 테이블`을 포함해야 한다.
    * [ ] `단체 지정`은 이미 생성된 `주문 테이블`만 포함할 수 있다.
    * [ ] `단체 지정`에 중복된 `주문 테이블`을 포함할 수 없다.
    * [ ] `단체 지정`은 `빈 주문 테이블`만 포함할 수 있다.
    * [ ] 이미 `단체 지정`된 `주문 테이블`은 새로운 `단체 지정`에 포함될 수 없다.
* [ ] `단체 지정`을 삭제할 수 있다.
    * [ ] `단체 지정`된 `주문 테이블` 중 `주문 상태`가 **조리**, **식사**인 경우 `단체 지정`을 삭제할 수 없다.

### 주문 테이블

* [ ] `단체 지정`이 되지 않은 `주문 테이블`을 생성할 수 있다.
* [ ] 전체 `주문 테이블`을 조회할 수 있다.
* [ ] 이미 생성된 `주문 테이블`을 수정할 수 있다.
    * [ ] `단체 지정`된 `주문 테이블` 중 `주문 상태`가 **조리**, **식사**가 아니면 `빈 주문 테이블`로 변경할 수 있다.
    * [ ] `빈 주문 테이블`이 아닌 `주문 테이블`의 **손님 수**를 변경할 수 있다.
        * [ ] **손님 수**는 0이상이어야 한다.

---

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
