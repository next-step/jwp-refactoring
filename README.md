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
| 주문 상태 | order status | 주문은 조리 ➜ 식사 ➜ 계산 완 료 순서로 진행된다. |
| 방문한 손님 수 | number of guests | 필수 사항은 아니며 주문은 0명으로 등록할 수 있다. |
| 단체 지정 | table group | 통합 계산을 위해 개별 주문 테이블을 그룹화하는 기능 |
| 주문 항목 | order line item | 주문에 속하는 수량이 있는 메뉴 |
| 매장 식사 | eat in | 포장하지 않고 매장에서 식사하는 것 |

|비지니스명|비지니스|관련된도메인|메뉴위치|
|:---:|:---:|:---:|:---:|
|상품|ProductService|상품생성,조회|[상품](#상품)|
|메뉴그룹|MenuGroupService|메뉴그룹생성,조회|[메뉴그룹](#메뉴그룹)|
|메뉴|MenuService|메뉴생성, 조회|[메뉴](#메뉴)|
|주문|OrderService|주문생성, 주문조회, 주문상태변경|[주문](#주문)|
|주문테이블|TableService|테이블생성, 빈 테이블로 변경, 방문한 손님 수|[주문테이블](#주문테이블)|
|단체지정|TableGroupService|단체지정생성, 단체지정취소|[단체지정](#단체지정)|


# 상품
 - 키친포스 운영자는 상품(`이름,가격`)을 등록할 수 있다. 
  - 가격은 반드시 필수이다. 
  - 상품의 가격은 0보다 작을 수 없다.
 - 여러개의 상품을 조회할 수 있다.


# 메뉴그룹
 - 키친포스 운영자는 메뉴를 `메뉴그룹`할 수 있다.
 - 그룹된 메뉴를 조회할 수 있다.
 
# 메뉴
 - 키친포스 운영자는 메뉴를 생성할 수 있다.
 - 메뉴의 가격이 올바르지 않으면 메뉴를 생성할 수 없다.
   - 0원 이상
 - 메뉴의 `메뉴그룹`이 존재하지 않는다면 생성할 수 없다. 
 - `상품`의 총 금액이 올바르지 않다면 생성할 수 없다 
   - 0원 이상
 - 메뉴를 조회할 수 있다.


# 주문
 - 손님은 `주문 테이블`에서 주문을 진행한다.
   - `빈테이블`에서는 주문할 수 없다.
 - 손님은 최소 `하나 이상`의 음식을 주문해야한다.
 - 손님의 주문은 키친포스에서 `판매하고 있는 상품`이어야한다.
 - 손님이 주문시 주문 `조리상태`가 된다.
 - 전체 주문을 조회할 수 있다.
 - 주문의 상태를 변경할 수 있다. 
   - 이미 `완료` 상태인 주문은 상태를 변경할 수 없다
   - 주문의 상태는 `조리`,`식사`로 변경 가능하다.


# 주문테이블
 - `주문테이블`을 생성할 수 있다.
 - `주문테이블` 전체 조회 할 수 있다.
 - `주문 테이블`을 `빈 테이블`로 변경할 수 있다
   - `요리중`, `식사 중`이면 `빈테이블`로 변경 할 수 없다.
 - `주문테이블`에 `손님 수`를 등록한다.
   - 테이블이 `빈테이블`이면 손님수를 등록할 수 없다. 
   

# 단체지정
 - `주문테이블`을 `단체지정` 할수 있다.
   - `주문테이블`을 단체지정한 취소후 다시 `단체지정`해야한다.
 - `단체지정`을 취소할 수 있다.
   - 해당 테이블의 상태가 `조리`,`식사` 중이라면 취소할수 없다.
 - `단체지정`된 테이블을 조회한다.
 



















