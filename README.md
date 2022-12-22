# 키친포스

--- 

## Step 1 - 테스트를 통한 코드 보호
### 요구 사항
- [x] `kitchenpos` 패키지의 코드를 보고 요구사항 작성
- [x] 모든 business object 에 대한 테스트 코드 작성
  - [x] 서비스 테스트 코드 작성
  - [x] 인수 테스트 작성

### kitchen pos 어플리케이션 요구사항

---

#### 주문
- 주문을 등록할 수 있다.
  - 주문을 하면 초기 주문 상태는 `조리중` 상태이다.
  - 주문 항목 정보가 올바르지 않으면 주문을 할 수 없다.
    - 주문 항목이 1개 이상 존재하여야 한다.
    - 주문 항목들의 메뉴들은 모두 메뉴에 등록되어 있어야 한다.
    - 주문을 받은 테이블은 등록된 테이블이어야 한다.
    - 주문을 받은 테이블은 비어있지 않아야 한다.
- 주문을 조회 할 수 있다.
  - 주문 정보에는 주문 항목 정보들이 포함되어 있다.
- 주문 상태를 변경 할 수 있다
  - 주문 정보가 올바르지 않으면 주문 상태를 변경할 수 없다.
    - 변경하려는 주문은 이미 등록된 주문이어야 한다.
    - 주문 상태가 `계산 완료` 상태이면 변경할 수 없다.

---

#### 메뉴
- 메뉴를 등록할 수 있다.
  - 메뉴 정보가 올바르지 않으면 등록할 수 없다.
    - 메뉴의 가격은 반드시 존재하여야 한다.
    - 메뉴의 가격은 0 이상이어야 한다.
    - 메뉴의 가격은 메뉴 상품들 가격의 총 합보다 클 수 없다.
    - 메뉴의 메뉴 그룹은 이미 저장되어 있어야 한다.
    - 등록하려는 메뉴의 상품들은 이미 저장되어 있어야 한다. 
- 메뉴의 목록을 조회할 수 있다.
  - 메뉴의 정보에는 메뉴 상품들의 정보가 포함되어 잇다.

--- 

#### 메뉴 그룹
- 메뉴 그룹을 등록할 수 있다.
- 메뉴 그룹의 목록을 조회할 수 있다.

---

#### 상품
- 상품을 등록할 수 있다.
  - 상품의 가격은 반드시 존재하여야 한다.
  - 상품의 가격은 0 이상이어야 한다.
- 상품의 목록을 조회할 수 있다

---

#### 주문 테이블
- 주문 테이블을 등록할 수 있다.
  - 주문 테이블 등록 시에는 단체 지정 정보가 없는 상태이다.
- 주문 테이블의 목록을 조회할 수 있다.
- 주문 테이블의 상태를 빈 테이블로 변경할 수 있다.
  - 변경하려는 주문 테이블의 정보가 이미 저장되어 있어야 한다.
  - 단체 지정이 되어있지 않은 상태이어야 한다.
  - 주문 상태가 `조리중`, `식사중` 이 아니어야 한다.
- 방문한 손님 수를 변경할 수 있다.
  - 방문한 손님 수는 0 이상이어야 한다.
  - 변경하려는 주문 테이블의 정보가 이미 저장되어 있어야 한다.
  - 주문 테이블이 비어있지 않은 상태여야 한다.

---

#### 단체 지정
- 단체 지정을 할 수 있다.
  - 단체 지정 할 주문 테이블은 2개 이상이어야 한다.
  - 등록하려는 모든 테이블의 정보는 이미 등록되어 있어야 한다.
  - 등록하려는 주문 테이블이 비어있지 않거나, 이미 단체 지정이 되어있으면 등록할 수 없다.
  - 등록하려는 모든 테이블의 상태를 비어있지 않음 상태로 변경한다.
  - 등록하려는 모든 테이블의 단체 지정 정보를 저장한다.
- 테이블을 단체 지정에서 제외할 수 있다.
  - 주문 상태가 `식사중`, `조리중` 이 아니어야 한다.
  - 주문 테이블들의 단체 지정 정보가 없어진다.

--- 

## Step2 - 서비스 리팩터링

### 요구사항
- [x] 단위 테스트 가능한 코드를 분리하고 단위 테스트 작성
- [x] 서비스 리팩터링
  - [x] Spring data JPA 적용
    - [x] 엔티티를 도메인 객체로 변환
    - [x] JPA를 사용한 리포지토리 구현
  - [x] 요청값, 응답값을 DTO 로 변환
  - [x] 도메인 객체의 setter 제거
  - [x] 도메인 객체의 기본 생성자 접근 제한
  - [x] 테스트 코드 리팩터링
- [x] flyway 적용
--- 

## Step3 - 의존성 리팩터링
### 요구사항
- [ ] 의존성 관점에서 설계 재검토
  - 메뉴의 이름과 가격이 변경되면 주문 항목도 함께 변경. 메뉴 정보가 변경되더라돋 주문 항목이 변경되지 않도록 구현
  - 클래스 사이, 패키지 사이의 의존 관계는 단방향이 되도록 설계


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
