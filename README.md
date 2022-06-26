# 키친포스

## 요구 사항

---
- step2 : 서비스 리팩토링
  - Controller - Domain 의존성 삭제
    - Controller에서 @RequestBody로 요청 받을때, domain이 아닌 RequestDTO 객체로 받음
    - Controller에서 Domain을 Body로 반환할때, domain이 아닌 ResponseDTO 객체로 반환
  - Exception 발생시 Advice를 통한 400Error발생
  - JpaRepository를 통한 구현
    - OneToMany 관계 1급 콜렉션으로 wrapping
    - 인원수, 가격에 대한 원시값을 객체로 묶어 검증 로직 이동(@Embedded)
    - Order, TableGroup에서 사용하는 시간값을 Auditing기능을 이용하여 삽입
    - 불필요한 직접참조 삭제 
      - 이로서 발생하는 ID참조 객체간 처리는 DomainService생성하여 구현
    
| Entity_A   | 관계  | Entity_B      | 관계구현방식 | 비고                                   |
|------------|-----|---------------|--------|--------------------------------------|
| Product    | 1:N | MenuProduct   | ID참조   |                                      |
| Menu       | 1:N | MenuProduct   | 직접참조   | MenuProduct에 대한 생명주기 Menu에서 관리       |
| MenuGroup  | 1:N | Menu          | ID참조   |                                      |
| MENU       | 1:N | OrderLineItem | ID참조   |                                      |
| Order      | 1:N | OrderLineItem | 직접참조   | OrderLineItem에 대한 생명주기 Order에서 관리    |
| OrderTable | 1:N | Order         | ID참조   |                                      |
| TableGroup | 1:N | OrderTable    | ID참조   |                                      |
 
   

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


