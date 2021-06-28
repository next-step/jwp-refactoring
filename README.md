# 키친포스

## 요구 사항
### 메뉴 그룹
#### 기능
1. 이름으로 메뉴 그룹 생성이 가능하다
2. 메뉴 그룹 리스트를 가져올 수 있다.
### 메뉴
#### 기능
1. 이름, 가격, 메뉴 그룹, 메뉴 상품으로 메뉴를 생성이 가능하다.
    - 등록을 원하는 메뉴의 가격이 비어 있거나, 0원보다 적을경우 IllegalArgumentException 이 발생한다.
    - 등록을 원하는 메뉴의 메뉴 그룹이 DB에 없을경우 IllegalArgumetException 이 발생한다.
    - 등록을 원하는 메뉴 상품이 DB에 있는지 확인하고, 없으면 IllegalArgumentException 이 발생한다.
    - 메뉴 상품의 금액 합계가 등록을 원하는 메뉴 가격보다 크면 IllegalArgumentException 이 발생한다.
    - 등록을 원하는 메뉴를 등록한다.
    - 메뉴 상품의 메뉴 고유 아이디를 등록된 메뉴의 고유 아이디로 변경하고 저장한다.
    - 저장된 메뉴 상품들을 저장된 메뉴에 설정하고 반환한다.
2. 메뉴의 목록을 가져올 수 있다.
    - DB에서 메뉴들을 전부 가져온다.
    - DB에서 메뉴와 연계된 메뉴 상품들을 가져온 뒤 메뉴에 저장하여 반환한다.
#### 관계
1. 메뉴 상품과는 **직접적**으로 연결이 되어있다.
2. 메뉴 그룹과는 **간접적**으로 연결이 되어있다.
### 주문
#### 기능
1. 주문을 생성한다.
    - 등록을 원하는 주문에 주문 항목이 비어있으면 IllegalArgumentException 이 발생한다.
    - 등록을 원하는 주문항목의 고유 ID를 가져온다. 
    - 등록을 원하는 주뭉항목이 DB에 전부 존재하는지 확인하여 전부 존재하지 않으면 IllegalArgumentException이 발생한다.
    - 주문 테이블이 존재하는지 확인하고, 없으면 IllegalArgumentException이 발생한다.
    - 주문 테이블이 빈 테이블일 경우 IllegalArgumentException이 발생한다. ( 테이블이 비어있는곳이면 당연히 주문을 못받는다. )
    - 등록을 원하는 주문의 주문 테이블 고유 아이디를 DB에서 가져온 주문 테이블의 고유아이디로 설정한다.
    - 등록을 원하는 주문의 상태를 조리중으로 변경한다.
    - 등록을 원하는 주문의 주문시간을 현재로 변경한다.
    - 등록을 원하는 주문을 등록한다.
    - 주문항목의 주문 고유번호를 등록된 주문의 ID로 변경한다.
    - 등록된 주문의 주문항목을 등록된 주문항목들로 변경하여 젖아하고, 반환한다.
2. 주문 목록을 가져온다.
    - DB에서 주문들을 가져온다.
    - DB에서 주문항목들을 가져온뒤 주문들에 설정을 한다.
    - 주문들을 반환한다.
3. 주문 상태를 변경한다.
    - 변경을 원하는 주문을 DB에서 가져오고, 없으면 IllegalArgumentException이 발생한다.
    - 주문의 상태가 계산 완료이고, 변경하려는 상태도 계산완료일 경우 IllegalArgumentException 이 발생한다.
    - 주문의 상태를 변경하려는 상태로 변경한다.
    - 주문을 저장한다.
    - 주문 항목들을 가져와서 주문에 설정한다음 반환한다.
#### 관계
1. 주문 테이블과는 **간접적**으로 연결되어 있다.
2. 주문 항목과는 **직접적**으로 연결되어 있다.
### 상품
#### 기능
1. 이름, 가격으로 상품 생성이 가능하다.
    - 가격이 비어있거나, 0보다 적을경우 IllegalArgumentException이 발생한다.
    - 상품을 저장하고, 반환한다.
2. 상품 목록을 가져올 수 있다.
    - 상품목록 전체를 조회한다.
### 단체 지정
#### 기능
1. 생성시간, 주문 테이블로 단체지정 생성이 가능하다
    - 등록을 원하는 단체지정에서 주문 테이블을 가져온다
    - 등록을 원하는 주문 테이블이 비어있거나, 1개밖에 없을경우 IllegalArugmentException이 발생한다.
    - 등록을 원하는 주문 테이블의 고유 아이디를 기준으로 DB에서 등록된 주문 테이블들을 가져온다.
    - 등록을 원하는 주문 테이블과 등록된 주문 테이블의 개수가 틀릴경우 IllegalArgumentException이 발생한다.
    - 등록된 주문 테이블의 상태가 비어있지 않거나, 이미 단체 지정이 되어있을 경우 IllegalArgumentException이 발생한다.
    - 등록을 원하는 단체지정의 생성 날짜를 현재 시간으로 변경한다.
    - 단체 지정을 저장한다.
    - 등록된 주문 테이블의 단체 지정 고유 아이디를 저장된 단체 지정의 고유 아이디로 변경하고, 비어있는 상태로 설정한다.
    - 등록된 주문 테이블을 저장하여 변경한다.
    - 단체 지정의 주문 테이블을 등록된 주문 테이블들로 설정하여 반환한다.
2. 단체 지정 Id를 통해 단체지정 해제가 가능하다.
    - 등록된 단체지정 ID를 통해 주문 테이블들을 가져온다
    - 주문 테이블들의 고유 아이디를 가져온다.
    - 주문 테이블들의 고유 아이디를를 조회했을 때 주문 상태가 조리 이거나, 식사 일경우 IllegalArgumentException이 발생한다.
    - 주문 테이블들의 단체지 지정 고유 아이디를 제거한 후 저장하여 변경한다.
#### 관계
1. (주문, 빈) 테이블과 **직접적**으로 연결이 되어 있다.
### (주문, 빈) 테이블
#### 기능
1. 단체 지정 고유번호, 방문한 손님 수, 빈 테이블 여부를 통해 (주문, 빈) 테이블을 생성이 가능하다.
    - 단체 지정의 고유 아이디를 빈값으로 설정한다
    - 테이블을 저장한다.
2. (주문, 빈) 테이블 목록을 가져올 수 있다.
    - 테이블들을 전부 가져온다.
3. 단체 지정 Id를 통해 빈 또는 존재하는 테이블 상태로 바꿀 수 있다
    - 테이블을 테이블의 고유 아이디로 가져온다
    - 테이블이 단체지정이 되어있을경우 IllegalArgumentException이 발생한다.
    - 테이블의 상태가 조리중이거나 식사중일경우 IllegalArgumentException으로 변경한다.
    - 테이블의 상태를 빈상태 또는 존재하는 상태로 변경한다
    - 테이블을 저장한다.
4. 방문한 손님 수를 통해 주문 테이블 상태로 바꿀 수 있다.
    - 원하는 인원수가 0보다 적으면 IllegalArgumentException이 발생한다
    - DB에서 테이블을 가져온다.
    - 테이블의 상태가 비어있으면 IllegalArgumentException이 발생한다
    - 테이블의 인원수를 설정한다.
    - 테이블을 저장한다.
#### 관계
1. 단체 지정과 **간접적**으로 연결이 되어있다.
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
| 가격 | price | 가격 |
| 조리 | COOKING | 주문을 받아 음식을 조리중인 것 |
| 식사 | MEAL | 조리된 음식을 먹는 중인것 |
| 계산완료 | COMPLETION | 계산완료된 상태 |