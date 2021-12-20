# 키친포스

## 요구 사항

#### 상품

- 상품 정보
    - ```이름``` (필수값)
    - ```가격``` (필수값)
- 상품을 등록할 수 있다.
    - ```상품 가격```의 유효성 검증을 해야한다.
        - 0원 이상이어야 한다.
- 상품 목록을 조회할 수 있다.

#### 메뉴 그룹

- 메뉴 그룹 정보
    - ```이름``` (필수값)
- 메뉴 그룹을 등록할 수 있다.
- 메뉴 그룹 목록을 조회할 수 있다.

#### 메뉴

- 메뉴 정보
    - ```이름``` (필수값)
    - ```가격``` (필수값)
    - 메뉴가 속하는 ```메뉴 그룹 정보``` (필수값)
    - ```메뉴에 속한 상품 리스트```
- 메뉴를 등록할 수 있다.
    - ```메뉴 가격```의 유효성 검증을 해야한다.
        - 0원 이상이어야 한다.
        - ```메뉴에 속한 상품 리스트의 가격 합```보다 작거나 같아야 한다.

- 메뉴 목록을 조회할 수 있다.

#### 테이블 그룹

- 테이블 그룹 정보
    - ```생성시간``` (필수값)
    - ```주문 테이블 리스트``` (필수값)
- 테이블 그룹을 등록할 수 있다.
    - ```주문 테이블 리스트``` 유효성 검증을 해야한다.
        - 2개 이상이어야 한다.
        - ```빈 테이블``` 이어야 한다.
        - 기존에 ```테이블 그룹```에 속한 ```주문 테이블```이 포함되면 안된다.
- 테이블 그룹을 해제할 수 있다.
    - 주문 테이블 리스트의  ```주문 상태```를 검증 해야한다.
        - ```조리```, ```식사``` 상태인 주문 테이블이 존재하는 경우 삭제할 수 없다.

#### 테이블

- 테이블 정보

    - ```테이블 그룹 정보``` (필수값 아님)
    - ```방문한 손님 수```  (필수값)
        - 기본값 : 0명
    - ```빈 테이블 여부``` (필수값)
        - 기본값 : false (주문 테이블)

- 테이블을 등록할 수 있다.

- 테이블 목록을 조회할 수 있다.

- 테이블 상태를 변경할 수 있다.

    - 테이블이 상태 변경 가능한지 검증해야 한다.

        - 테이블은 어떤 ```테이블 그룹```에도 속해있지 않아야 한다.

        - 해당 테이블의 주문 중 ```주문 상태```가 ```조리```, ```식사``` 가 존재하면 안된다.

- 테이블 방문 손님 수를 변경할 수 있다.

    - ```변경 요청 방문손님 수``` 유효성 검증을 해야한다.
        - 0명 이상 이어야 한다.
        - ```빈 테이블``` 상태는 변경할 수 없다.

#### 주문

- 주문 정보
    - 주문 테이블 정보
    - 주문 상태
    - 주문 시간
    - 주문 항목 리스트
- 주문을 등록할 수 있다.
    - 주문 항목 리스트 유효성 검증을 해야한다.
        - 주문 항목은 1개 이상이어야 한다.
        - 주문 항목 리스트에 중복된 메뉴가 존재하면 안된다.
    - 주문 테이블 유효성 검증을 해야한다.
        - 빈 테이블은 주문할 수 없다.
- 주문 목록을 조회할 수 있다.
- 주문 상태를 변경할 수 있다.
    - 주문 상태가 변경 가능한지 검증해야 한다.
        - 주문 상태가 ```계산 완료``` 이면 변경 할 수 없다.

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
