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

---

## 요구사항 정리

### 상품
- [x] 상품을 등록한다.
  - [x] 가격은 필수입력항목이다.
  - [x] 가격은 0원 이상이어야 한다.
- [x] 상품 목록을 조회한다.

### 메뉴 그룹
- [x] 메뉴 그룹을 등록한다.
- [x] 메뉴 그룹 목록을 조회한다.

### 메뉴
- [x] 메뉴를 등록한다.
  - [x] 메뉴는 존재하는 메뉴그룹에 포함되어야 한다.
  - [x] 메뉴는 메뉴상품들과 함께 등록한다.
  - [x] 가격은 필수입력항목이다.
  - [x] 가격은 0원 이상이어야 한다.
  - [x] 메뉴의 가격은 메뉴상품들 가격의 합을 초과할 수 없다.
- [x] 메뉴 목록을 조회한다.

### 주문 테이블
- [ ] 주문 테이블을 등록한다.
- [ ] 주문 테이블 목록을 조회한다.
- [ ] 주문 테이블을 빈 테이블로 변경한다.
  - [ ] 단체 지정된 주문테이블인 경우 빈 테이블로 변경이 불가능하다.
  - [ ] 진행중(조리 or 식사)인 경우 빈 테이블로 변경이 불가능하다.
- [ ] 주문 테이블의 손님 수를 변경한다.
  - [ ] 변경하려는 손님 수는 최소 1명 이상이어야 한다.
  - [ ] 빈 테이블의 주문 테이블은 손님 수를 변경할 수 없다.

### 단체 지정
- [ ] 단체 지정을 등록한다.
  - [ ] 단체 지정은 지정될 주문테이블과 함께 등록한다.
  - [ ] 단체 지정될 주문테이블은 2개 이상이어야 한다.
  - [ ] 단체 지정될 주문테이블들은 모두 존재해야한다.
  - [ ] 단체 지정될 주문테이블들은 모두 빈 테이블이어야 한다.
  - [ ] 단체 지정될 주문테이블들은 다른 단체로 지정되어있지 않아야 한다.
- [ ] 단체 지정을 해제한다.
  - [ ] 단체 지정에 속하는 주문테이블들의 단체 지정을 해제한다.
  - [ ] 진행중(조리 or 식사)인 단계의 주문 이력이 존재할 경우 해제가 불가능하다.
  
### 주문
- [ ] 주문을 등록한다.
  - [ ] 주문 항목은 최소 1개 이상 이어야한다.
  - [ ] 주문 항목의 메뉴는 모두 존재해야 한다.
  - [ ] 빈 테이블인경우 주문할 수 없다.
  - [ ] 주문 상태를 조리중으로 설정한다.
- [ ] 주문 목록을 조회한다.
- [ ] 주문 상태를 변경한다.
  - [ ] 주문상태가 계산완료인 주문은 변경할 수 없다.
