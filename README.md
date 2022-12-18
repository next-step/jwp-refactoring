# 키친포스

## 요구 사항

### 상품
- 정보 : 상품 ID, 상품명, 상품가격


- 생성 (POST, /api/products)  
상품 정보를 저장한다.  
상품가격이 null 이거나 0 미만인 경우 상품을 생성 할 수 없다. (IllegalArgumentException)


- 조회 (GET, /api/products)  
저장된 전체 상품 목록을 조회한다.

### 메뉴 그룹
- 정보 : 메뉴 그룹 ID, 메뉴 그룹명


- 생성 (POST, /api/menu-groups)  
메뉴 그룹명을 생성한다.  


- 조회 (GET, /api/menu-groups)  
저장된 전체 메뉴 그룹명을 조회한다.

### 메뉴
- 정보 : 메뉴 ID, 메뉴명, 메뉴가격, 메뉴 그룹 ID, 메뉴 상품 목록


- 생성 (POST, /api/menus)  
메뉴가격이 null 이거나 0 미만인 경우 메뉴를 생성할 수 없다. (IllegalArgumentException)  
메뉴의 메뉴 그룹 ID 가 기존에 등록된 값이 아니면 메뉴를 생성할 수 없다. (IllegalArgumentException)  
메뉴 상품 목록의 상품 ID 가 기존에 등록된 값이 아니면 메뉴를 생성할 수 없다. (IllegalArgumentException)  
메뉴 상품 목록의 상품을 조회하여 메뉴 상품 개수와 상품 가격의 곱의 합을 구한다.  
메뉴 가격이 구한 상품 목록 가격의 합 보다 작으면 메뉴를 생성할 수 없다. (IllegalArgumentException)  
메뉴를 저장한다.  
메뉴 상품 목록의 각 메뉴 상품에 저장한 메뉴 ID 를 set 하고 메뉴 상품을 저장한 후, 메뉴 상품 목록을 저장한다.


- 조회 (GET, /api/menus)  
저장된 전체 메뉴를 조회한다.  
이 때 해당 메뉴 ID 값을 가진 메뉴 상품 정보도 함께 조회한다.

### 주문
- 정보 : 주문 ID, 주문 테이블 ID, 주문 상태, 주문 시각, 주문 아이템 목록


- 생성 (POST, /api/orders)  
주문 아이템 목록이 비어있는 요청은 주문을 생성할 수 없다. (IllegalArgumentException)  
주문 아이템 목록 개수가 주문 아이템 목록에 포함된 메뉴 ID 목록 개수와 같지 않으면 주문을 생성할 수 없다. (IllegalArgumentException)  
주문의 주문 테이블 ID 가 기존에 등록된 값이 아니면 주문을 생성할 수 없다. (IllegalArgumentException)  
주문의 주문 테이블의 상태가 empty 이면 주문을 생성할 수 없다. (IllegalArgumentException)  
주문을 저장한다.  
주문 아이템 목록의 주문 아이템에 주문 ID 를 set 하고 주문 아이템을 저장한 후, 주문 아이템 목록을 set 한다.


- 조회 (GET, /api/orders)  
저장된 전체 주문 목록을 조회한다.  
이 때 해당 주문 ID 값을 가진 메뉴 상품 목록도 함께 조회한다.


- 수정 (PUT, /api/orders/{orderId}/order-status)  
주문 ID 가 기존에 등록된 값이 아니면 주문을 수정할 수 없다. (IllegalArgumentException)  
현 주문 상태가 COMPLETION 이면 주문 상태를 갱신할 수 없다. (IllegalArgumentException)  
주문 상태를 갱신하여 저장한다.

### 주문 테이블
- 정보 : 주문 테이블 ID, 테이블 그룹 ID, 방문한 손님 수, 빈 테이블 여부


- 생성 (POST, /api/tables)
테이블 그룹 ID에 null 을 set 한다.
주문 테이블을 저장한다.


- 조회 (GET, /api/tables)
전체 주문 테이블 목록을 조회한다.


- 빈 테이블 여부 값 갱신 (PUT, /api/tables/{orderTableId}/empty)  
갱신 요청의 주문 테이블 ID 가 기존에 등록된 값이 아니면 빈 테이블 여부 값을 갱신할 수 없다. (IllegalArgumentException)  
주문 테이블 ID로 조회한 주문 테이블의 테이블 그룹 ID 가 null 이 아니면 빈 테이블 여부 값을 갱신할 수 없다. (IllegalArgumentException)  
주문 테이블에 요리 중 상태의 주문이 있는 경우 빈 테이블 여부 값을 갱신할 수 없다. (IllegalArgumentException)  
주문 테이블의 빈 테이블 여부 값 갱신한다.


- 방문한 손님 수 갱신 (PUT, /api/tables/{orderTableId}/number-of-guests)  
갱신 요청의 방문한 손님 수가 0 미만이면 방문한 손님 수 값을 갱신할 수 없다. (IllegalArgumentException)  
갱신 요청의 주문 테이블 ID 가 기존에 등록된 값이 아니면 빈 테이블 여부 값을 갱신할 수 없다. (IllegalArgumentException)  
주문 테이블 ID로 조회한 주문 테이블이 빈 테이블이면 방문한 손님 수 값을 갱신할 수 없다. (IllegalArgumentException)  
주문 테이블의 방문한 손님 수를 갱신한다.

### 테이블 그룹
- 정보 : 테이블 그룹 ID, 생성 시각, 주문 테이블 목록


- 생성 (POST, /api/table-groups)  
생성 요청의 주문 테이블 목록이 비어 있거나 단일 테이블로 구성된 경우 테이블 그룹을 생성할 수 없다. (IllegalArgumentException)  
생성 요청의 주문 테이블 목록 개수가 조회된 테이블 목록 개수와 다른 경우 테이블 그룹을 생성할 수 없다. (IllegalArgumentException)  
테이블 목록의 주문 테이블 중 빈 테이블이 아닌 테이블이 존재하거나 다른 테이블 그룹 정보로 등록된 경우 테이블 그룹을 생성할 수 없다. (IllegalArgumentException)  
테이블 그룹을 저장한다.  
저장된 테이블 그룹의 ID 값으로 주문 테이블의 테이블 그룹 정보를 set 하고 빈 테이블 여부의 값을 변경한 후 주문 테이블 목록을 저장한다.


- 삭제 (DELETE, /api/table-groups/{tableGroupId})  
삭제 요청의 테이블 그룹 ID 로 등록된 주문 테이블 목록을 조회한다.  
주문 테이블에 요리 중 상태의 주문이 있는 경우 테이블 그룹을 삭제할 수 없다. (IllegalArgumentException)   
주문 테이블의 테이블 그룹 ID를 null 로 set 하고 주문 테이블을 저장한다. 

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
