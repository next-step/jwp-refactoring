# 키친포스

## 요구 사항
- [ ] 메뉴
    - [ ] 메뉴 생성
        - [ ] 메뉴 생성
        - [ ] 가격이 없거나 0보다 작을 시 생성 불가능
        - [ ] 메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러??
        - [ ] 메뉴 상품에 메뉴 id 세팅
    - [ ] 메뉴 조회
- [ ] 메뉴 그룹
    - [ ] 메뉴그룹 생성
    - [ ] 메뉴그룹 조회
- [ ] 주문
    - [ ] 주문 생성
        - [ ] 주문 생성
        - [ ] 주문 항목 없을 시 주문 불가능
        - [ ] 주문 항목과 메뉴 숫자가 다른 경우(메뉴에 없는 주문 항목) 주문 불가능
        - [ ] 주문 테이블이 비어있을 시 변경 불가능
        - [ ] 주문 항목에 주문id 세팅
    - [ ] 주문 조회
    - [ ] 주문상태 변경
        - [ ] 주문상태 변경
        - [ ] 이미 완료상태이면 변경 불가능
- [ ] 상품
    - [ ] 상품 생성
        - [ ] 가격이 없거나 0보다 작을 시 생성 불가능
    - [ ] 상품 조회
- [ ] 테이블
    - [ ] 테이블 생성
    - [ ] 테이블 조회
    - [ ] 테이블 인원수 변경
        - [ ] 테이블 인원수 변경
        - [ ] 인원수 0 미만시 변경 불가능
        - [ ] 주문 테이블이 없을 시 변경 불가능
    - [ ] 테이블 비어있는지 여부 변경
        - [ ] 테이블 그룹이 있을 시 변경 불가능
        - [ ] 요리 중이나 식사 중일 때 변경 불가능
        - [ ] 테이블 비어있는지 여부 변경
- [ ] 테이블 그룹
    - [ ] 테이블 그룹 생성
        - [ ] 주문 테이블이 없거나 2개 미만일 시 생성 불가능
        - [ ] 입력받은 주문 테이블 개수와 실제 주문 테이블 개수가 다른 경우(메뉴에 없는 주문 테이블) 생성 불가능
        - [ ] 주문을 등록할 수 없는 테이블이나 다른 그룹에 등록되어 있는 주문 테이블이 포함되어 있는 경우 생성 불가능
        - [ ] 테이블 그룹 생성
        - [ ] 주문 테이블에 테이블 그룹id 세팅
    - [ ] 테이블 그룹 삭제
        - [ ] 요리 중이나 식사 중인 주문 테이블을 포함하고 있다면 삭제 불가능
        - [ ] 주문 테이블에서 테이블 그룹id 제거

    
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


## 1단계
### 기능 요구 사항
#### 테스트 코드 생성
- [X] 메뉴
    - [X] 메뉴 생성
        - [X] 메뉴 생성
        - [X] 가격이 없거나 0보다 작을 시 생성 불가능
        - [X] 메뉴 상품 가격의 합과 입력받은 가격 비교하여 입력받은 가격이 더 크면 에러??
        - [X] 메뉴 상품에 메뉴 id 세팅
    - [X] 메뉴 조회
- [X] 메뉴 그룹
    - [X] 메뉴그룹 생성
    - [X] 메뉴그룹 조회
- [X] 주문
    - [X] 주문 생성
        - [X] 주문 생성
        - [X] 주문 항목 없을 시 주문 불가능
        - [X] 주문 항목과 메뉴 숫자가 다른 경우(메뉴에 없는 주문 항목) 주문 불가능
        - [X] 주문 테이블이 없을 시 주문 불가능
    - [X] 주문 조회
    - [X] 주문상태 변경
        - [X] 주문상태 변경
        - [X] 이미 완료상태이면 변경 불가능
- [X] 상품
    - [X] 상품 생성
        - [X] 가격이 없거나 0보다 작을 시 생성 불가능
    - [X] 상품 조회
- [X] 테이블
    - [X] 테이블 생성
    - [X] 테이블 조회
    - [X] 테이블 인원수 변경
        - [X] 테이블 인원수 변경
        - [X] 인원수 0 미만시 변경 불가능
        - [X] 주문 테이블이 비어있을 시 변경 불가능
    - [X] 테이블 비어있는지 여부 변경
        - [X] 테이블 그룹이 있을 시 변경 불가능
        - [X] 요리 중이나 식사 중일 때 변경 불가능
        - [X] 테이블 비어있는지 여부 변경
- [X] 테이블 그룹
    - [X] 테이블 그룹 생성
        - [X] 주문 테이블이 없거나 2개 미만일 시 생성 불가능
        - [X] 입력받은 주문 테이블 개수와 실제 주문 테이블 개수가 다른 경우(메뉴에 없는 주문 테이블) 생성 불가능
        - [X] 주문을 등록할 수 없는 테이블이나 다른 그룹에 등록되어 있는 주문 테이블이 포함되어 있는 경우 생성 불가능
        - [X] 테이블 그룹 생성
    - [X] 테이블 그룹 삭제
        - [X] 요리 중이나 식사 중인 주문 테이블을 포함하고 있다면 삭제 불가능
        - [X] 주문 테이블에서 테이블 그룹id 제거
        
## 2단계
### 기능 요구 사항
#### 단위 테스트하기 쉽도록 분리
- [X] 상품
    - [X] 가격 분리
    - [X] jpa 적용
    - [X] model과 Dto 분리
- [X] 메뉴 그룹
    - [X] jpa 적용
    - [X] model과 Dto 분리
- [X] 메뉴
    - [X] jpa 적용
        - [X] menuProduct
        - [X] menu
        - [X] product
        - [X] menuGroup
    - [X] model과 Dto 분리
    - [X] price 분리
    - [X] menuService 리팩토링
- [X] 주문 테이블
    - [X] jpa 적용
        - [X] 주문 테이블
        - [X] 주문 테이블 그룹
    - [X] model과 Dto 분리
    - [X] 손님수 분리
    - [X] 주문 테이블 로직 도메인으로 이동
- [X] 테이블 그룹
    - [X] model과 Dto 분리
    - [X] 테이블 그룹 로직 도메인으로 이동
- [X] 주문
    - [X] model과 Dto 분리
    - [X] jpa 적용
        - [X] order
        - [X] orderLineItem 
    - [X] 로직 도메인으로 이동
        - [X] orderLineItems 추가

#### 코드 리뷰사항
- [X] 불필요한 로직 제거
- [X] jpa 에서는 findBy 엔티티 메서드에서 별도로 _Id 로 명시 X
- [X] 그룹을 해제하려는 그룹이 없는 경우에 대한 처리도 필요
- [X] indent depth 1 로 유지
- [X] 객체의 값을 꺼내서 계산하기 보다는 객체에게 메세지 전달
- [X] DAO와 repository 차이 확인

## 3단계
### 기능 요구사항
- [X] 상품 패키지
- [ ] 테이블 그룹 패키지
    - [ ] 테이블에 대한 의존성 제거
- [ ] 테이블 패키지
    - [ ] 테이블 그룹에 대한 의존성 제거
- [ ] 메뉴 그룹 패키지
- [ ] 메뉴 페키지
    - [ ] 메뉴 -> 메뉴 상품으로 단방향으로만 연결
    - [ ] 메뉴 그룹에 대한 의존성 제거
    - [ ] 상품에 대한 의존성 제거
- [ ] 주문 패키지
    - [ ] 주문 -> 주문 항목으로 단반향으로만 연결
    - [ ] 메뉴에 대한 의존성 제거
    - [ ] 테이블에 대한 의존성 제거    
