# 키친포스

## 요구 사항
### 상품
* `상품`을 등록할 수 있다.
* `상품`의 가격이 올바르지 않으면 등록할 수 없다.
    * `상품`의 가격은 0원 이상이어야 한다.
* `상품`의 목록을 조회할 수 있다.

### 메뉴 그룹
* `메뉴 그룹`을 등록할 수 있다.
* `메뉴 그룹`들을 조회할 수 있다.

### 메뉴
* 아래의 항목들을 충족시켜야 `메뉴`를 등록할 수 있다.
    * `메뉴`의 가격이 올바르지 않으면 등록할 수 없다.
        * `메뉴`의 가격은 0원 이상이어야 한다.
    * 해당 `메뉴 그룹`이 등록되어 있지 않으면 등록할 수 없다.
    * `메뉴`에 속해 있는 `메뉴 상품`이 등록되어 있지 않으면 등록할 수 없다.
    * `메뉴`의 가격이 속해 있는 `메뉴 상품들`의 총 `금액`보다 크면 등록할 수 없다.
* 모든 `메뉴`를 조회할 수 있다.

### 주문
* 아래의 항목들을 충족시켜야 `주문`를 등록할 수 있다.
    * `주문 항목`이 하나도 없을 경우 등록할 수 없다.
    * 선택한 `주문 항목`들이 모두 등록되어 있지 않으면 등록할 수 없다.
    * 해당 `주문 테이블`이 등록되어 있지 않으면 등록할 수 없다.
    * `빈 테이블`일 경우 등록할 수 없다.
* 모든 `주문`을 조회할 수 있다.
* `주문 상태`를 변경할 수 있다.
    * `주문`이 등록되어 있지 않으면 변경할 수 없다.
    * `주문 상태`가 계산 완료일 경우 변경할 수 없다.

### 테이블
* `주문 테이블`을 등록할 수 있다.
* 모든 `주문 테이블`을 조회할 수 있다.
* `빈 테이블`로 변경할 수 있다.
    * `주문 테이블`이 등록되어 있지 않으면 변경할 수 없다.
    * `주문 테이블`이 `단체 지정`일 경우 변경할 수 없다.
    * * 들어간 `주문`들 중에서 `주문 상태`가 조리 또는 식사가 하나라도 있을 경우 변경할 수 없다.
* `방문한 손님 수`를 변경할 수 있다.
    * 입력한 `방문한 손님 수`가 0보다 작으면 변경할 수 없다.
    * `주문 테이블`이 등록되어 있지 않으면 변경할 수 없다.
    * `빈 테이블`일 경우 변경할 수 없다.

### 단체 지정
* `주문 테이블`들을 `단체 지정`할 수 있다.
    * 입력한 `주문 테이블` 갯수가 2개 미만일 경우 지정할 수 없다.
    * `주문 테이블`들이 등록되어 있지 않으면 지정할 수 없다.
    * `주문 테이블`이 `빈 테이블`이거나 이미 `단체 지정`되어 있는 경우 지정할 수 없다.
* `단체 지정`을 해제할 수 있다.
    * 들어간 `주문`들 중에서 `주문 상태`가 조리 또는 식사가 하나라도 있을 경우 변경할 수 없다.

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
