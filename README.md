# 키친포스

## 요구 사항

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

### 기능 목록
- [ ] 키친포스 시스템의 요구사항을 작성
  - [ ] table create 문 분석 후 각 domain 간 관계를 찾아본다 (가능하면, ERD를 직접 그려볼 것)  
  - [ ] 각 도메인의 관계를 분석하여, `Bounded Context`를 설정 해 본다.
  - [ ] 분석한 도메인 간의 관계를 말로 잘 풀어서 요구사항을 정리 해 본다.
- [ ] 키친포스의 요구사항을 토대로 테스트 코드를 작성
  - [ ] Business Object 에 대한 테스트 코드 작성
    - [ ] `Product`
    - [ ] `Menu`
    - [ ] `Menu Group` 
    - [ ] `Menu Product` 
    - [ ] `Table Group` 
    - [ ] `Order Table` 
    - [ ] `Order` 
    - [ ] `Order Line Item` 
  - [ ] `@SpringBootTest`로 통합 테스트 코드 작성
    - [ ] 메뉴 통합테스트
    - [ ] 주문 통합테스트
  - [ ] `@ExtendWith(MockitoExtension.class)` 를 이용해 단위 테스트 코드 작성
    - [ ] `Product`
      - [ ] create 단위 테스트 작성 
      - [ ] list 단위 테스트 작성
    - [ ] `MenuGroup`
      - [ ] create 단위 테스트 작성
      - [ ] list 단위 테스트 작성
    - [ ] `Menu`
      - [ ] create 단위 테스트 작성
      - [ ] list 단위 테스트 작성
    - [ ] `TableGroup`
      - [ ] create 단위 테스트 작성
      - [ ] ungroup 단위 테스트 작성
    - [ ] `Table`
      - [ ] create 단위 테스트 작성
      - [ ] list 단위 테스트 작성
      - [ ] changeEmpty 단위 테스트 작성
      - [ ] changeNumberOfGuests 단위 테스트 작성
    - [ ] `Order`
      - [ ] create 단위 테스트 작성
      - [ ] list 단위 테스트 작성
      - [ ] changeOrderStatus 단위 테스트 작성
  - [ ] 인수 테스트 코드 작성
    - [ ] 메뉴 인수 테스트 작성
    - [ ] 주문 인수 테스트 작성
