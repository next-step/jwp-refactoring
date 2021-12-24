# 키친포스

## 요구 사항

### 메뉴 그룹
* 메뉴 그룹을 등록 할 수 있다.
  * 메뉴 그룹 이름으로 등록 할 수 있다.
* 메뉴 그룹 목록을 조회 할 수 있다.

### 메뉴
* 메뉴를 등록 할 수 있다.
  * 메뉴는 메뉴명, 금액, 메뉴 그룹, 메뉴 상품 목록 으로 이루어진다.
    * 금액은 꼭 필요하고 0원 이상이어야 한다.
    * 가격 x 수량 = 금액 보다 등록한 금액이 더 작아야 한다.
* 메뉴 목록을 메뉴 상품 목록과 함께 조회 할 수 있다.

### 주문
* 주문을 등록 할 수 있다.
  * 주문 테이블, 주문 항목 목록으로 이루어진다.
  * 주문 항목에는 수량, 메뉴가 있다.
  * 메뉴는 미리 등록 되어있어야한다.
  * 메뉴는 중복될 수 없다.
  * 주문 테이블이 없으면 주문은 안 만들어 진다.
  * 주문은 만들어 질때 조리상태가 되고 주문 항목들도 만들어 진다.
* 주문 목록을 조회 할 수 있다.
* 주문 상태를 식사 또는 계산 완료로 변경 할 수 있다.
  * 주문이 계산 완료 상태면 변경 할 수 없다.

### 상품
* 상품을 이름,가격으로 등록 할 수 있다.
  * 존재한 상품은 등록 할 수 없다.
  * 가격은 0원 보다 커야 한다.
* 상품 목록을 조회 할 수 있다.

### 단체 지정
* 단체 지정을 주문 테이블 목록 으로 등록 할 수 있다.
  * 주문 테이블이 2개 이상이여야 한다.
  * 주문 테이블은 빈 테이블이어야한다.
  * 등록되어 있는 주문테이블 목록만 사용하여 만들수 있다.
  * 단체 지정이 될때 빈 테이블 상태에서 손님이 채워진 테이블 상태로 변경 된다.
* 단체 지정 해체 할 수 있다.
  * 단체지정 해제는 주문 테이블 계산 완료일때만 가능하다.

### 주문 테이블
* 주문 테이블은 방문한 손님 수,빈 테이블 상태로 등록 할 수 있다.
* 주문 테이블 목록을 조회 할 수 있다.
* 주문 테이블 상태를 빈 테이블로 수정 할 수 있다.
  * 조리, 식사 상태 일땐 바꿀 수 없다. 
  * 주문 테이블이 단체 지정이라면 바꿀 수 없다.
* 빈 테이블 상태를 손님이 채워진 테이블 상태로 수정 할 수 있다.
  * 조리, 식사 상태 일땐 바꿀 수 없다.
* 방문한 손님 수를 변경 할 수 있다.
  * 0명이상 가능하다.

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

### 1단계 - 테스트를 통한 코드 보호
요구사항1 : kitchenpos `패키지의 코드`를 보고 키친포스의 요구 사항을 `README.md`에 작성한다.

요구사항2 : Business Object에 대한 `테스트 코드`를 작성한다. @SpringBootTest를 이용한 통합 테스트 코드 또는 @ExtendWith(MockitoExtension.class)를 이용한 `단위 테스트 코드`를 작성한다.

#### 공부 정리
* 참고자료 @WebMvcTest : https://velog.io/@woo00oo/SpringBoot-Test-2

### 2단계 - 서비스 리팩터링
* [x] 단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현
* [x] 서비스계층의 비즈니스 로직을 도메인 계층으로 변경
* [x] menuService 리팩토링
* [x] menuGroupService 리팩토링
* [x] orderService 리팩토링
* [x] productService 리팩토링
* [x] tableService 리팩토링
* [x] tableGroupService 리팩토링
* [x] DTO 를 이용하여 Response, Request 분리
* [x] fixture 사용

### 3단계 - 의존성 리팩터링

* Menu
  * MenuGroup 간접참조
  * MenuProduct 직접참조
* MenuGroup
  * 없음
* MenuProduct
  * Menu 간접참조
  * Product 간접참조
* Product
  * 없음
* TableGroup
  * OrderTable 간접참조
* OrderTable
  * TableGroup 간접참조
* Order
  * OrderTable 간접참조
  * OrderLineItem 직접참조
* OrderLineItem
  * Order 간접참조
  * Menu 간접참조

* [ ] EventHandler 를 사용하여 의존성객체를 리팩터링