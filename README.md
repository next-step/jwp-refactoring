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
| create | 메뉴에 존재하는 상품들은 모두 존재해야 한다. |
| create | 주문 항목의 개수가 동일해야 한다. |
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

### 테이블

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
  

## 2단계 할 일
- [x] Dao 기반 -> Repository 기반 (spring-data-jpa) 로 변경
- [x] Entity 설정
- [x] Dao 제거
- [x] 디렉토리 정리 (도메인 기반)
- [x] mock 제거 -> inside-out 기반 테스트 작성 -> 취소 (직접 작성해보니 도메인 테스트에서 inside-out 방식으로 거르고 service 테스트 시에는 mock이 더 영속성에 제약이 없었다.)
- [x] getter setter 제거
- [ ] 인수테스트 작성 (시간이 있으면.. 12시까지)