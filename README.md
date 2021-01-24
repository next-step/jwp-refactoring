# 키친포스

# Step1 - 테스트를 통한 코드 보호 
## 요구 사항 1
### 메뉴그룹(/menu-groups)
* 메뉴 그룹을 등록할 수 있다
* 메뉴 그룹 목록을 조회할 수 있다

### 메뉴(/menus)
* 메뉴를 등록할 수 있다
    * 메뉴를 등록할 때 메뉴그룹과 메뉴상품 목록이 필요하다 
    * 아래와 같은 조건에서 등록이 가능하다
        * 가격은 0이상이다
        * menuGroupId가 존재해야 한다
        * 메뉴가격의 합이 각 상품들의 합보다 작거나 같아야 한다
* 메뉴 목록을 조회할 수 있다

### 상품(/products)
* 상품을 등록할 수 있다
* 상품의 가격이 올바르지 않으면 등록할 수 없다
    * 상품의 가격은 0원 이상이어야 한다 
* 상품의 목록을 조회할 수 있다
                   
### 테이블그룹(/table-groups)
* 테이블 그룹을 등록할 수 있다 
    * 아래와 같은 조건에서 등록이 가능하다
        * 주문 테이블이 비어있지 않고 주문 테이블의 사이즈가 2이상이다
    * 테이블 그룹이 등록되면서 그룹에 들어있는 주문 테이블의 테이블 그룹 id가 등록되고 상태가 비어있지않음으로 변경된다
* 각 주문 테이블의 테이블 그룹 id를 비운다
    * 아래와 같은 조건에서 비울 수 있다
        * OrderStatus가 COOKING, MEAL이 아님

### 테이블(/tables)
* 테이블을 등록할 수 있다
* 테이블 목록을 조회할 수 있다
* 테이블의 상태를 변경할 수 있다
    * 아래와 같은 조건에서 변경이 가능하다
        * 테이블 존재
        * 테이블 그룹 존재하지 않음 
        * OrderStatus가 COOKING, MEAL이 아님
* 테이블의 고객수를 변경할 수 있다
    * 아래와 같은 조건에서 변경이 가능하다
        * 게스트수가 0이상이다
        * orderTableId가 존재해야한다
        * 테이블의 상태가 비어있지 않아야 한다

### 주문(/orders)
* 주문을 등록할 수 있다
    * 아래와 같은 조건에서 등록이 가능하다
        * orderLineItems가 비어있지 않다
    * 등록한 주문은 COOKING 상태이다
            
* 주문 목록을 조회할 수 있다
* 주문 ID와 주문 정보를 받아서 주문 상태를 변경할 수 있다
    * OrderStatus.COMPLETION이면 익셉션 발생


### 요구 사항 2
* 요구사항 1을 토대로 테스트 코드를 작성한다
### 메뉴그룹(/menu-groups)
* MenuGroupRestControllerTest
* MenuServiceTest

### 메뉴(/menus)
* MenuGroupRestControllerTest
* MenuServiceTest

### 상품(/products)
* ProductRestControllerTest
* ProductServiceTest 
    
### 테이블그룹(/table-groups)    
* TableGroupRestControllerTest
* TableGroupServiceTest

### 테이블(/tables)
* TableRestControllerTest
* TableServiceTest

### 주문(/orders)
* OrderRestControllerTest
* OrderServiceTest

# Step2 - 서비스 리팩터링
## 요구 사항
단위 테스트하기 어려운 코드와 단위 테스트 가능한 코드를 분리해 단위 테스트 가능한 코드에 대해 단위 테스트를 구현한다
* JDBC -> JPA


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
