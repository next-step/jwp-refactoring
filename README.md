# 키친포스

## 요구 사항
- [X] kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다.
- [X] 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다.
- [X] 모든 Business Object에 대한 테스트 코드를 작성한다.
  - @SpringBootTest를 이용한 통합 테스트 코드 또는 @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성한다.

### 상품
- [X] 상품을 등록한다.
  - 가격은 필수 항목이며, 0원 이상이어야 한다.
- [X] 상품 목록을 조회한다.

### 메뉴
- [X] 메뉴를 등록한다.
  - 가격은 필수 항목이며, 0원 이상이어야 한다.
  - 메뉴 그룹에 속해있어야 한다.
  - 메뉴에 있는 상품은 모두 등록되어 있어야 한다.
  - 메뉴 가격은 등록 된 상품의 ```가격 * 수량```의 합보다 작거나 같아야한다.
- [X] 메뉴 목록을 조회한다.
  - 메뉴에 등록 된 상품이 함께 조회되어야 한다.

### 메뉴 그룹
- [X] 메뉴 그룹을 등록한다.
- [X] 메뉴 그룹 목록을 조회한다.

### 주문 테이블
- [X] 주문 테이블을 등록한다.
- [X] 주문 테이블 목록을 조회한다.
- [X] 주문 테이블 이용 여부를 변경한다.
  - 등록 된 테이블이어야 한다.
  - 테이블 그룹에 등록되어 있지 않아야 한다.
  - 주문 테이블의 상태가 ```조리, 식사```인 경우 변경할 수 없다.
- [X] 주문 테이블의 손님수를 변경한다.
  - 변경할 수 있는 손님수는 최소 0명부터 시작한다.
  - 등록 된 테이블이어야 한다.
  - 테이블을 이용하고 있지 않은 경우 변경할 수 없다.

### 단체(테이블) 지정
- [X] 단체 테이블로 지정한다.
  - 주문 테이블이 최소 2개는 등록되어야한다.
  - 요청한 주문 테이블은 모두 등록되어 있어야 한다.
  - 요청한 주문 테이블은 모두 단체 테이블에 속해있지 않아야 한다.
- [X] 단체 테이블을 해지한다.
  - 단체 테이블에 등록 된 주문 테이블의 상태가 ```조리, 식사```인 경우 해지할 수 없다.

### 주문
- [X] 주문을 등록한다.
  - 주문 상품은 모두 등록되어 있어야 한다.
  - 주문 테이블이 등록되어 있어야 한다.
  - 주문 테이블을 이용하고 있지 않은 경우 등록할 수 없다.
- [X] 주문 목록을 조회한다.
- [X] 주문 상태를 변경한다.
  - 주문이 등록되어 있어야 한다.
  - 이미 완료 된 주문은 상태 변경이 불가하다.

## Step2 요구 사항
- [ ] 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다.
- [ ] ```서비스 레이어```에 있는 ```비즈니스 로직```은 ```도메인 레이어```로 리팩토링
  - [X] 상품 도메인
  - [ ] 메뉴 도메인
  - [ ] 메뉴 그룹 도메인
  - [ ] 주문 테이블 도메인
  - [ ] 단체(테이블) 도메인
  - [ ] 주문 도메인

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

