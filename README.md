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

# 1단계 - 테스트를 통한 코드 보호

## 요구사항 1 - 키친포스 요구사항 정리

1. 상품

- API table

| Method | URI | Description | Request | Response |
| --- | --- | --- | --- | --- |
| POST | "/api/products" | 상품 생성 | Number id, String name, Number price | 생성된 상품의 URI와 상품 데이터 |
| GET | "/api/products" | 상품 목록 조회 |  | 모든 상품 목록 |

- Business 요구사항 
    - POST "/api/products"
        - price는 0보다 큰 정수여야 상품을 생성할 수 있다.
    
## 요구사항 2 - 모든 Business Object의 테스트코드 작성
