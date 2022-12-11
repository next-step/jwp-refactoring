# 키친포스

## 요구 사항

### 메뉴
- [X] 메뉴를 생성할 수 있다.
  - [X] 가격은 0원 이상이어야 한다.
  - [X] 등록된 메뉴 그룹에 포함되어야 한다.
  - [X] 메뉴 상품은 모두 등록된 상품이어야 한다.
  - [X] 메뉴의 가격은 메뉴 상품 가격의 합보다 작아야 한다.
- [X] 메뉴 목록을 조회할 수 있다.

### 메뉴그룹
- [X] 메뉴 그룹을 생성할 수 있다.
- [X] 메뉴 그룹 목록을 조회할 수 있다.

### 주문
- [X] 주문을 생성할 수 있다.
  - [X] 최소 1개의 주문 항목이 존재해야 한다.
  - [X] 주문 항목에 포함된 메뉴들은 모두 존재해야 한다.
  - [X] 주문 테이블은 등록된 테이블이어야 한다.
- [X] 주문 목록을 조회할 수 있다.
- [X] 주문 상태를 변경할 수 있다. 
  - [X] 주문이 완료된 상태에는 주문 상태 변경이 불가능하다. 

### 주문 테이블
- [X] 주문 테이블을 생성할 수 있다.
- [X] 주문 테이블 목록을 조회할 수 있다.
- [X] 주문 테이블으 빈 좌석 여부에 대해 변경할 수 있다.
  - [X] 주문 테이블은 단체 지정이 되어 있지 않아야 한다.
  - [X] 주문 테이블의 주문 상태는 조리 중이거나 식사 중이면 안된다.
- [X] 주문 테이블에 방문 고객 수를 변경할 수 있다.
  - [X] 주문 테이블의 방문 고객 수는 0명 이상이어야 한다.
  - [X] 주문 테이블은 빈 좌석이 아니어야 한다.
  
### 상품
- [X] 상품을 등록할 수 있다.
  - [X] 상품의 가격은 0원 이상이어야 한다.
- [X] 상품의 목록을 조회할 수 있다.

### 단체 지정
- [X] 단체 지정을 할 수 있다.
  - [X] 등록된 주문 테이블들이 2개 이상 필요하다.
  - [X] 주문 테이블들은 비어있어야 한다.
  - [X] 주문 테이블들은 단체 지정이 되어있지 않아야 한다.
  - [X] 단체 지정을 해제할 수 있다.
    - [X] 주문 테이블들의 상태가 조리 중이거나 식사중이면 안된다.
  
## 용어 사전

| 한글명      | 영문명              | 설명                            |
|----------|------------------|-------------------------------|
| 상품       | product          | 메뉴를 관리하는 기준이 되는 데이터           |
| 메뉴 그룹    | menu group       | 메뉴 묶음, 분류                     |
| 메뉴       | menu             | 메뉴 그룹에 속하는 실제 주문 가능 단위        |
| 메뉴 상품    | menu product     | 메뉴에 속하는 수량이 있는 상품             |
| 금액       | amount           | 가격 * 수량                       |
| 주문 테이블   | order table      | 매장에서 주문이 발생하는 영역              |
| 빈 테이블    | empty table      | 주문을 등록할 수 없는 주문 테이블           |
| 주문       | order            | 매장에서 발생하는 주문                  |
| 주문 상태    | order status     | 주문은 조리 ➜ 식사 ➜ 계산 완료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정    | table group      | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목    | order line item  | 주문에 속하는 수량이 있는 메뉴             |
| 매장 식사    | eat in           | 포장하지 않고 매장에서 식사하는 것           |

## 1단계 - 테스트를 통한 코드 보호
### 요구 사항 체크리스트
- [X] 키친포스 요구사항 작성
- [X] 키친포스 테스트 코드 작성

## 2단계 - 서비스 리팩터링
### 요구 사항 체크리스트
- [ ] 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
  - [ ] Menu
  - [ ] MenuGroup
  - [ ] MenuProduct
  - [ ] Order
  - [ ] OrderLineItem
  - [ ] OrderStatus
  - [ ] OrderTable
  - [ ] Product
  - [ ] TableGroup

