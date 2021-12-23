# 키친포스

## 요구 사항

### 메뉴 관리
- 메뉴를 그룹화 할 수 있다. 
  - ex) 추천메뉴, 신메뉴, 두마리메뉴...
  - 메뉴는 하나 이상의 메뉴 그룹에 속해야 한다
- 메뉴 그룹을 조회할 수 있다.
- 메뉴를 조회할 수 있다.
- 메뉴에 상품을 등록할 수 있다.
  - 메뉴의 가격은 0원 이상이다.
  - 메뉴의 가격은 메뉴를 구성하는 상품의 가격 합보다 크면 안된다.

### 상품 관리
- 상품의 이름과 가격을 등록할 수 있다.
  - 상품의 가격은 0원 이상이다.
- 상품 목록을 조회할 수 있다.

### 주문
- 테이블별 주문 정보를 관리할 수 있다.
  - 주문 상태, 주문시각, 주문한 메뉴
- 주문 목록을 조회할 수 있다.
- 주문은 테이블이 있어야 가능하다.
- 주문 상태를 변경할 수 있다.
  - 요리중, 식사, 완료
  - 완료 상태인 경우 변경할 수 없다.

### 테이블 관리
- 단체 손님이 사용중인 테이블을 알 수 있다.
  - 테이블이 두 개 이상일 때만 그룹화할 수 있다.
  - 테이블은 비어있으면 안된다.
  - 주문 상태가 요리중 또는 식사중이면 그룹화를 해제할 수 없다.
- 테이블 목록을 조회할 수 있다.
- 테이블 사용 여부를 관리할 수 있다.
  - 주문 상태가 요리중 또는 식사중이면 빈 상태로 변경할 수 없다.
  - 그룹화된 테이블은 비어 있는 상태로 변경할 수 없다.
- 테이블 손님 수를 관리할 수 있다.
  - 손님 수는 0 이상이다.
  - 빈 테이블은 인원을 변경할 수 없다.

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

### step2 요구사항

- [ ] 상품 관리 리팩토링
  - [X] 모델 
    - [X] Product 엔티티 변환
    - [X] Name 래핑 클래스 추가
  - [X] ProductRepository 사용하도록 수정
  - [X] dto 모델 추가
- [ ] 메뉴 관리 리팩토링
  - [ ] 모델
    - [X] MenuGroup 엔티티 변환
    - [X] Menu 엔티티 변환
    - [X] MenuProduct 엔티티 변환
  - [X] MenuRepository 사용하도록 수정
  - [X] MenuGroupRepository 사용하도록 수정
  - [X] MenuProductRepository 사용하도록 수정
  - [X] 메뉴 그룹 dto 모델 추가
  - [X] 메뉴 dto 모델 추가
  - [X] 메뉴 상품 dto 모델 추가
- [ ] 테이블 관리 리팩토링
  - [ ] 모델
    - [X] TableGroup 엔티티 변환
    - [X] OrderTable 엔티티 변환
  - [X] TableGroupRepository 사용하도록 수정
  - [X] OrderTableRepository 사용하도록 수정
  - [X] 테이블그룹 dto 모델 추가
  - [X] 주문테이블 dto 모델 추가
- [ ] 주문 관리 리팩토링
  - [ ] 모델
    - [X] Order 엔티티 변환
    - [X] OrderLineItem 엔티티 변환
  - [X] OrderRepository 사용하도록 수정
  - [ ] OrderLineItemRepository 사용하도록 수정
  - [ ] dto 모델 추가

### step1 요구사항
- [X] 키친포스 기능 요구사항 분석
- [X] 테스트 코드 작성
  - [X] service 테스트
    - [X] ProductService
    - [X] MenuGroupService
    - [X] MenuService
    - [X] TableService
    - [X] TableGroupService
    - [X] OrderService
  - [X] 인수테스트
    - [X] MenuGroupRestController
    - [X] MenuRestController
    - [X] ProductRestController
    - [X] OrderRestController
    - [X] TableGroupRestController
    - [X] TableRestController
