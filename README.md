# 키친포스

## 요구 사항 작성

### 메뉴 그룹
```
 1. 이름을 받아 이름 기반의 메뉴 그룹을 만들 수 있다.
 
 2. 등록한 메뉴 그룹 리스트를 출력 할 수 있다.
```

### 상품
```
 1. 이름과 가격을 받아 상품을 등록할 수 있다.
    - 가격이 없거나 0보다 작으면 안된다
 
 2. 등록된 상품들의 리스트를 볼 수 있다.
```

### 메뉴
```
 
 1. 메뉴 이름, 메뉴의 가격, 메뉴 그룹, 메뉴에 속하는 상품들과 상품들의 수량을 입력받아 신규 메뉴를 만들 수 있다.
    - 메뉴의 가격이 없거나 0보다 작으면 안된다.
    - 존재하지 않는 메뉴 그룹에 등록할 수 없다.
    - 메뉴 가격은 상품 가격들의 합보다 작아야 한다.
    - 존재하는 상품들로만 메뉴를 구성할 수 있다.
 
 2. 등록된 메뉴들의 리스트를 볼 수 있다.
```
### 주문 테이블
```
 1. 테이블의 앉은 손님의 수와, 현재 테이블이 비었는지 입력 받아 주문_테이블을 만들 수 있다.

 2. 주문_테이블 리스트를 출력할 수 있다.
 
 3. 주문_테이블의 손님 수를 업데이트를 할 수 있다.
    - 손님은 0명 미만으로 등록이 불가능하다.
    - 존재하지 않는 주문_테이블을 업데이트 할 수 없다.
 
 4. 주문_테이블이 비었는지 업데이트를 할 수 있다. 
    - 존재하지 않는 주문_테이블을 업데이트 할 수 없다.
    - 주문_테이블에 속한 주문들이 현재 조리중이거나, 식사 중일 시 테이블을 업데이트 할 수 없다.

```

### 주문
```
 1. 손님이 메뉴를 선택하면 신규 주문을 할 수 있다.
    - 주문에 메뉴가 없다면 안된다.
    - 존재하지 않는 메뉴는 선택할 수 없다.
    - 존재하지 않는 테이블에서 신규 주문을 할 수 없다.
    - 존재하는 테이블 이지만 손님이 앉지 않은 빈 테이블은 주문할 수 없다.
 
 2. 주문 전체 리시트를 출력할 수 있다.
 
 3. 현재 주문의 상태를 업데이트 할 수 있다.
    - 유효하지 않은 주문은 업데이트 할 수 없다.
    - 정산된 주문은 업데이트 할 수 없다.
```

### 단체 손님
```
 1. 개별 테이블들을 단체 손님을 위한 테이블 그룹으로 묶을 수 있다.
    - 주문_테이블은 2개 이상 주어져야 한다.
    - 유효한 주문 테이블이 주어져야한다.( 중복된 주문이나, 존재하지 않는 주문이 입력으로 들어오면 안된다. )
    - 주문_테이블은 하나의 그룹에만 속할 수 있다.
    - 주문_테이블은 빈 테이블이여야 한다.
 
 2. 테이블 그룹을 제거할 수 있다.
    - 개별 테이블들의 주문 상태가 조리 중이거나, 식사 중이면 제거할 수 없다.

```

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
