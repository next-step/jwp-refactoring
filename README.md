# 키친포스

## 요구 사항
### 메뉴 그룹
1. 생성
2. 조회
### 메뉴
1. 생성
   - 메뉴의 가격이 null이거나 0보다 작을 수 없다.
   - 메뉴 그룹이 존재하지 않으면 생성할 수 없다.
   - 메뉴 가격이 메뉴 상품들의 합보다 클 수 없다.
2. 조회
   - 메뉴 리스트를 조회한다. 
### 주문
1. 생성
    - 주문 항목이 비어있을 수 없다.
    - 주문 항목 수와 주문 항목 메뉴의 수는 다를 수 없다.
    - 주문을 하면 주문 테이블이 없을 수 없다.
2. 조회
   - 주문 리스트를 조회한다.
3. 수정
   - 주문 상태를 변경한다.
     - 완료된 주문 상태를 변경할 수 없다.
### 상품
1. 생성
   - 상품 가격이 null이거나 0보다 작을 수 없다.
2. 조회
   - 상품 리스트를 조회한다. 
### 테이블 그룹 
1. 생성
   - 주문 테이블의 수가 0일 수 없고 크기가 2보다 작을 수 없다.
   - 저장되어 있는 테이블의 수가 주문 테이블의 수가 다를 수 없다. 
2. 삭제
### 테이블
1. 생성
2. 조회
   - 테이블 리스트를 조회한다.
3. 수정
   - 빈 테이블로 변경한다.
   - 손님 수를 변경한다.
     - 손님 수가 0일 수 없고 저장된 테이블에 대해서도 0일 수 없다.

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
