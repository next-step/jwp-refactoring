# 키친포스

## 요구 사항
    * Controller
        * MenuGroupRestController : 메뉴가 가질 수 있는 메뉴 그룹을 조회하거나 만들 수 있음  
            * create : 메뉴 그룹을 요청자에게 받아 service로 전달
            * list : 메뉴 그룹 요청을 요청자에게 받아 service로 전달
        * MenuRestController : 메뉴를 조회하거나 만들 수 있음
            * create : 메뉴를 요청자에게 받아 Service로 전달
            * list : 메뉴 조회를 요청자에게 전달 받아 service로 전달
        * OrderRestController : 주문, 주문 조회, 주문 상태 변경을 할 수 있음
            * create : 요청자에게 전달 받아 주문 정보를 service에 전달
            * list : 주문 조회 요청자에게 전달 받아 service로 전달
            * changeOrderStatus : 주문 id의 상태 값을 변경
        * ProductRestController : 상품 정보를 만들거나, 조회할 수 있음
            * create : 요청자로부터 상품 정보를 받아 service로 전달  
            * list : 요청자로부터 상품전체 조회를 요청받아 service로 전달
        * TableGroupRestController : 테이블을 그룹화하거나 그룹화한 것을 풀도록 service에 전달
            * create : 요청자로부터 테이블 그룹 정보를 받아 service로 전달
            * ungroup : 요청자로 부터 분리할 테이블 그룹 id를 받아 service로 전달 
        * TableRestController : 테이블을 만들거나 조회, 테이블 상태 값을 변경하도록 service로 전달
            * create : 요청자에게 테이블을 만드는 정보를 전달 받아 service로 전달
            * list : 테이블 조회 요청자에게 전달 받아 service로 전달
            * changeEmpty : 테이블을 empty 상태로 만들도록 service로 전달
            * changeNumberOfGuests : 테이블 id와 테이블에 세팅해줄 인워을 받아 service로 전달                          
    * Service(application)
        * MenuGroupService : 메뉴 그룹의 로직 처리 및 데이터 저장 요청
            * create :  controller에게 전달받은 메뉴 그룹을 저장
            * list : 전체 메뉴 그룹을 조회
        * MenuService : 메뉴 생성 및 조회의 로직 처리 및 데이터 조회 및 저장 요청
            * create :  controller에게 전달받은 메뉴를 저장
            * list : 전체 메뉴 조회
        * OrderRestController : 주문, 주문 조회, 주문 상태 변경의 데이터 저장 요청
            * create : 주문 정보를 유효성 체크하고, 이슈가 없다면 주문 정보를 저장함
            * list : 주문 정보 전체를 조회함
            * changeOrderStatus : 주문 id 기준의 주문의 상태값을 변경함                        
        * ProductService : 상품 정보 저장 및 조회 요청
            * create : 전달 받은 상품 정보를 유효성 체크 후 저장
            * list : 상품 전체 정보 조회
        * TableGroupService : 테이블 그룹을 그룹화하거나 풀도록 로직 처리 후 저장
            * create : 테이블 그룹 정보를 받아 유효성 체크 후 이슈 없으면 그룹화 후 저장
            * ungroup : 테이블 그룹 id를 받아 유효성 체크 후 이슈 없으면 그룹화 해제 및 저장       
        * TableService : 테이블을 만들거나 조회, 테이블 상태 값을 변경하도록 로직 처리 후 데이터 저장
            * create : 테이블 정보를 받아 테이블 정보 저장
            * list : 테이블 전체를 조회
            * changeEmpty : 테이블 empty로 가능한지 유효성 체크 후 가능할 경우 empty로 데이터 저장
            * changeNumberOfGuests : 테이블 인원 변경이 가능한지 유효성 체크 후 가능할 경우 테이블 인원 변경하여 데이터 저장
            
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
