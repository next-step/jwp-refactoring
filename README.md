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

## 요구 사항 1

- kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.

- [마크다운(Markdown) - Dooray!](https://dooray.com/htmls/guides/markdown_ko_KR.html)

---

### 상품

- 컨트롤러

| api url | method |  설명 |
| --- | --- | --- |
| /api/products | post | 새로운 상품을 등록한다. |
| /api/products | get | 상품 목록을 조회한다. |

- 기능

| method |  설명 |
| --- | --- |
| create | **새로운 상품을 등록한다.** |
| create | 가격은 null이나 -가 될 수 없다. |
| list | **상품 목록을 조회한다.** |

---

### 메뉴 그룹

- 컨트롤러

| api url | method |  설명 |
| --- | --- | --- |
| /api/menu-groups | post | 새로운 메뉴 그룹을 등록한다. |
| /api/menu-groups | get | 메뉴 그룹 목록을 조회한다. |

- 기능

| method |  설명 |
| --- | --- |
| create | **새로운 메뉴 그룹을 등록한다.** |
| list | **메뉴 그룹 목록을 조회한다.** |

---

### 메뉴

- 컨트롤러

| api url | method |  설명 |
| --- | --- | --- |
| /api/menus | post | 새로운 메뉴를 등록한다. |
| /api/menus | get | 메뉴 목록을 조회한다. |

- 기능

| method |  설명 |
| --- | --- |
| create | **새로운 메뉴를 등록한다.** |
| create | 가격은 null이나 -가 될 수 없다. |
| create | 메뉴 그룹이 존재하지 않는다면 추가될 수 없다. |
| create | 메뉴의 가격이 상품들의 총 가격의 합보다 커야한다. |
| create | 메뉴에 상품들을 등록한다. |
| list | **메뉴 목록을 조회한다.** |

---

### 주문

- 컨트롤러

| api url | method |  설명 |
| --- | --- | --- |
| /api/orders | post | 새로운 주문을 등록한다. |
| /api/orders | get | 주문 목록을 조회한다. |
| /api/orders/{orderId}/order-status | put | 주문 상태를 변경한다. |

- 기능

| method |  설명 |
| --- | --- |
| create | **새로운 주문을 등록한다.** |
| create | 주문 항목은 반드시 존재한다. |
| create | 메뉴에 존재하는 상품들은 모두 존재해야 한다. - jpa 보장이라고 생각 - |
| create | 주문 항목의 개수가 동일해야 한다. - jpa 보장이라고 생각 - |
| create | 주문 테이블이 존재해야 한다. |
| create | 주문 테이블은 비어있지 않아야 한다. |
| create | 주문 항목에 주문 id를 등록한다. |
| list | **주문 목록을 조회한다.** |
| changeOrderStatus | **주문 상태를 변경한다.** |
| changeOrderStatus | 주문 id는 반드시 존재한다. |
| changeOrderStatus | 주문 상태는 완료가 아니어야 한다. |
| changeOrderStatus | 주문에 주문 항목이 바뀐 것을 등록한다. |

---

### 테이블 그룹

- 컨트롤러

| api url | method |  설명 |
| --- | --- | --- |
| /api/table-groups | post | 새로운 테이블 그룹을 등록한다. |
| /api/table-groups/{tableGroupId} | delete | 테이블 그룹을 삭제한다. |

- 서비스

| method |  설명 |
| --- | --- |
| create | **새로운 테이블 그룹을 등록한다.** |
| create | 주문 테이블은 반드시 존재하고 2개이상 있어야 한다. |
| create | 테이블 그룹이 가진 주문 테이블과 데이터베이스에서 가져온 주문 테이블 갯수는 같다. |
| create | 테이블 그룹 생성 시 주문 테이블은 비어있어야 한다. |
| create | 테이블 그룹에 주문 테이블들을 등록한다. |
| ungroup | **테이블 그룹을 해제한다.** |
| ungroup | 테이블 그룹은 주문 상태가 cooking or meal 이 아니어야 한다. |
| ungroup | 주문 테이블에 테이블 그룹을 해제한다. |

---

### 주문 테이블

- 컨트롤러

| api url | method |  설명 |
| --- | --- | --- |
| /api/tables | post | 새로운 테이블을 등록한다. |
| /api/tables | get | 테이블 목록을 조회한다. |
| /api/tables/{orderTableId}/empty | put | 빈 테이블로 변경한다. |
| /api/tables/{orderTableId}/number-of-guests | put | 테이블 게스트 숫자를 변경한다. |

- 서비스

| method |  설명 |
| --- | --- |
| create | **새로운 테이블을 등록한다.** |
| list | **테이블 목록을 조회한다.** |
| changeEmpty | **빈 테이블로 변경한다.** |
| changeEmpty | 주문 테이블이 반드시 존재한다. |
| changeEmpty | 테이블 그룹이 아닌 주문 테이블만 빈 테이블로 변경 가능하다. |
| changeEmpty | 주문 상태는 cooking이나 meal이 아니어야 한다. |
| changeNumberOfGuests | **테이블 게스트 숫자를 변경한다.** |
| changeNumberOfGuests | 테이블 게스트 숫자는 0 이하일 수 없다. |
| changeNumberOfGuests | 주문 테이블이 반드시 존재해야 한다. |

## 관계도

- 관계도 그림
  

  ![image](https://user-images.githubusercontent.com/17772475/146971725-05a656c6-22ca-48ab-923f-7ee8180344fa.png)

- 관계도 2차 그림
  

  ![image](https://user-images.githubusercontent.com/17772475/147706656-f8619599-3b3a-4ac6-81e4-67bb48eb779f.png)
  
- 관계도 3차 그림


  ![image](https://user-images.githubusercontent.com/17772475/147825104-c6d9e7a1-51dd-42db-ba00-04f24da86fcc.png)



## 2단계 할 일
- [x] Dao 기반 -> Repository 기반 (spring-data-jpa) 로 변경
- [x] Entity 설정
- [x] Dao 제거
- [x] 디렉토리 정리 (도메인 기반)
- [x] mock 제거 -> inside-out 기반 테스트 작성 -> 취소 (직접 작성해보니 도메인 테스트에서 inside-out 방식으로 거르고 service 테스트 시에는 mock이 더 영속성에 제약이 없었다.)
- [x] getter setter 제거
- [x] 한글로 변수명 바꾸기
- [x] 도메인 테스트로 더 단단하게 보호하기
- [x] 인수테스트 작성 (시간이 있으면.. 12시까지) - 시간 부족으로 인해... delay

## 2 단계 의문 정리
- product는 menu에 속하기만 하는 것 같은데 product와 menuproduct 둘 다 필요할까..?
- menu와 menuProduct도 겹치는 것 같은 느낌인데 그럴까..?
- menu 를 생성하면서 menuproduct를 생성하는데 menuproduct는 menu를 가지는 것이 필수다 => 여기 설정 부분이 자꾸 꼬인다.
_ menuGroup은 왜 필수일까..?
- jpa 연결 이후 보장되는 것 같아서 생략한 `주문 항목의 개수가 동일해야 한다.` 는 정말 보장될까? (내가 생각하지 못한 예외가 있는 것이 아닐까 생각 중)
- changeEmpty 와 changeNumbersOfGuest의 애매모호함 때문에 부딪히는 면이 있는 것 같다.

## 주요 변화 기록
- TableService -> OrderTableService로 변경

### 3단계 할 일
- [x] 의존성 관계 정리
- [x] 매직넘버 없애기
- [x] TODO -> DONE 완료 시키기..
- [x] 생성자 protected 적용
- [x] changeEmpty는 개인적으로 changeNumbersOfGuests(0)로 컨버팅 (메서드 자체는 이해 관계자 설득 전에는 살려둔다는 느낌 또는 empty 명시적 표현의 느낌으로 살려 둠)
- [ ] **Bean Validation 극한으로 써 보기**
- [x] 메뉴 <-> 주문 항목 간접 참조로 변경
- [x] 메뉴상품 <-> 상품 간접 참조로 변경
- [x] 주문 테이블 <-> 테이블 그룹 간접 참조로 변경
- [ ] 주문 <-> 주문 테이블 간접 참조로 변경
- [ ] 각 도메인마다 validator 작성
- [ ] 이벤트 pub-sub 패턴 쓰기

### 의문 해소
- 도메인을 점점 이해하게되면서 menu와 product가 이해관계에 따라 필요할 수 있다고 생각하게 됨
    - ex. 짜장면, 탕수육 세트 => 메뉴 짜장면 => 상품
    - 이렇게 되면 menu -> menuProduct, product는 필요하게 됨
    - menu, menuProduct는 일대다 단방향 설정이 가능하지만 일대다 단방향에서는 연관 관계 처리를 위해 update 쿼리가 발동하므로 양방향으로 놔둠
- menuGroup도 필수로 하는 것이 필요할 수도 있겠다고 생각하게 됨
    - 1메뉴그룹 1~n 메뉴 그룹을 필수적으로 가짐으로써 메뉴 그룹이 없는 메뉴에 대한 조회 걱정이 없어진다면 그럴 수 있겠다고 생각
- changeEmpty는 개인적으로 changeNumbersOfGuests(0)와 같다고 생각

### 생각
- 결합도가 높은 도메인 관계 찾기
  - Menu 와 OrderLineItem 의 관계
    - 메뉴의 이름과 가격이 변할 때 같이 변하지 않게 하기
  
- 도메인 목록
  - 상품
  - 메뉴
  - 메뉴 상품
  - 메뉴 그룹
  - 주문
  - 주문 항목
  - 주문 테이블
  - 테이블 그룹
  
- 연관 관계 목록
  - 메뉴 <-> 메뉴 상품
  - 메뉴상품 <-> 상품
  - 메뉴 <-> 메뉴 그룹
  - 주문 <-> 주문 항목
  - 메뉴 <-> 주문 항목
  - 주문 <-> 주문 테이블
  - 주문 테이블 <-> 테이블 그룹

  
- 생각하는 직접 참조 목록
  - 메뉴 <-> 메뉴 상품 (같이 생성)
  - 메뉴 <-> 메뉴 그룹 (도메인 제약 사항 공유 - 요구 조건에 있던 사항)
  - 주문 <-> 주문 항목 (같이 생성 같이 삭제)
  
- 생각하는 간접 참조 목록 
  - 메뉴상품 <-> 상품
  - 메뉴 <-> 주문 항목
  - 주문 테이블 <-> 테이블 그룹
  - 주문 <-> 주문 테이블
  