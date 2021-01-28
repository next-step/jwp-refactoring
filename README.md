# 키친포스

## 요구 사항

### 상품
* `상품`을 생성한다.
  * `상품` 가격이 없거나 0보다 작을 경우 예외 발생한다.
  * `상품`을 데이터베이스에 저장한다.
  * 저장된 `상품`을 응답한다.
* `상품` 목록을 조회한다.
  * `상품` 목록을 데이터베이스에서 조회한다.
  * `상품` 목록을 응답한다.

## 메뉴 그룹
* `메뉴 그룹`을 생성한다.
  * `메뉴 그룹`을 데이터베이스에 저장한다.
  * 저장된 `메뉴 그룹`을 응답한다.
* `메뉴 그룹` 목록을 조회한다.
  * `메뉴 그룹` 목록을 데이터베이스에서 조회한다.
  * `메뉴 그룹` 목록을 응답한다.

## 메뉴
* `메뉴`를 생성한다.
  * `메뉴` 가격이 없거나 0보다 작으면 예외 발생한다.
  * `메뉴그룹`이 없으면 예외 발생한다.
  * `메뉴`에 속한 `메뉴상품` 목록 중 데이터베이스에 없는 `상품`이 있으면 예외 발생한다.
  * 데이터베이스에서 조회한 `상품`의 가격과 해당하는 `메뉴상품`의 수량을 곱한 금액의 합이 메뉴의 가격보다 작으면 예외 발생한다.
  * `메뉴`를 등록한다.
  * 등록한 `메뉴` 아이디를 받아와 각 `메뉴 상품`의 메뉴 아이디 속성에 저장한다. 
  * 각 `메뉴 상품`을 데이터베이스에 저장한다.
  * `메뉴`를 데이터베이스에 저장한다.
  * 저장된 `메뉴`를 조회한다.
* `메뉴` 목록을 조회한다.
  * 데이터베이스에서 `메뉴` 목록을 조회한다.
  * 각 `메뉴`별로 `메뉴 상품` 목록을 조회한다. 
    * `메뉴`의 `메뉴 상품` 목록 속성에 저장한다. 
  * `메뉴` 목록을 응답한다.

### 주문테이블
* `주문테이블`을 생성한다.
  * 처음 생성시에는 `테이블그룹`이 없다.
  * `주문테이블`을 데이터베이스 저장한다.
  * 저장된 `주문테이블`을 응답한다.
* `주문테이블` 목록을 조회한다.
  * `주문테이블` 목록을 데이터베이스에서 조회한다.
* `주문테이블`을 `빈 테이블` 여부를 변경한다.
  * 데이터베이스에서 요청한 `주문테이블`을 조회한다.
    * `주문테이블`이 없으면 예외 발생한다.
  * `테이블그룹`이 있으면 예외 발생한다.
  * `주문테이블`의 주문 상태가 `조리` 또는 `식사`일 경우 예외 발생한다.
  * `주문테이블`의 `빈 테이블` 여부 속성을 요청 받은 값으로 변경한다.  
  * `주문테이블` 데이터베이스 정보를 변경한다.
  * 변경된 `주문테이블`을 응답한다.
* `주문테이블`의 `방문한 손님 수`를 변경한다.
  * `방문한 손님 수`가 0보다 작으면 예외 발생한다.
  * 요청한 `주문테이블` 정보 조회한다.
    * 정보가 없으면 예외 발생한다.
  * `빈 테이블`이면 예외 발생한다.
  * `주문테이블`의 `방문한 손님 수` 속성을 요청 받은 값으로 변경한다.
  * `주문테이블` 데이터베이스 정보를 변경한다.
  * 변경된 `주문테이블`을 응답한다.

### 주문
* `주문`을 생성한다.
  * `주문 항목` 목록이 비어 있으면 예외 발생한다.
  * `주문 항목` 목록의 메뉴 아이디 목록을 만들어 데이터베이스에 저장된 메뉴에서 카운트를 조회한다.
  * `주문 항목` 목록의 갯수와 조회한 메뉴 갯수가 일치하지 않으면 예외 발생한다.
  * `주문테이블`을 조회한다.
    * `빈 테이블`이면 예외 발생한다.
  * `주문테이블` 아이디를 `주문`의 `주문테이블 아이디` 속성에 저장한다.
  * `주문 상태`를 `조리`로 변경한다.
  * `주문 시간`을 현재 시간으로 한다.
  * `주문`을 데이터베이스에 저장한다.
  * 저장된 `주문`의 아이디 값을 각 `주문 항목`의 메뉴 아이디 속성에 저장한다.
  * 각 `주문 항목`을 데이터베이스에 저장한다.
  * 데이터베이스에 저장된 결과의 `주문 항목` 목록을 저장된 `주문`의 `주문 항목` 목록 속성에 저장한다.
  * 저장된 `주문`을 응답한다.
* `주문` 목록을 조회한다.
  * 데이터베이스에서 `주문` 목록을 조회한다.
  * 각 `주문`의 `주문 항목`을 조회하여 `주문 항목` 목록 속성에 저장한다.
  * `주문` 목록을 응답한다.
  
### 단체 지정
* `단체 지정`을 생성한다.
  * `주문 테이블` 목록이 비어있거나 2보다 적으면 예외 발생한다.
  * `주문 테이블` 목록의 아이디 목록을 만든다.
  * `주문 테이블` 아이디 목록으로 데이터베이스에서 저장된 `주문 테이블` 목록을 조회한다.
  * `주문 테이블` 목록과 저장된 `주문 테이블` 목록 갯수가 일치하지 않으면 예외 발생한다.
  * 저장된 `주문 테이블` 중 하나라도 빈 테이블이 아니거나 `단체 지정`아이디가 없을 경우 예외 발생한다.
  * `단체 지정` 생성일시를 현재시간으로 한다.
  * `단체 지정`을 데이터베이스에 저장한다.
  * 저장된 `단체 지정` 아이디를 각 `주문 테이블`의 `단체 지정` 아이디 속성에 저장한다.
  * 각 `주문 테이블`의 빈 테이블 여부를 false로 한다.
  * 각 `주문 테이블`을 데이터베이스에 저장한다.
  * 저장된 `주문 테이블`을 저장된 `단체 지정`의 `주문 테이블` 목록 속성에 저장한다.
  * 저장된 `단체 지정`을 응답한다.
* `주문 테이블`의 `단체 지정`을 삭제한다.
  * 데이터베이스에서 요청한 `단체 지정` 아이디로 `주문 테이블` 목록을 조회한다.
  * `주문 테이블` 아이디 목록으로 데이터베이스에서 저장된 `주문 테이블` 중 `단체 지정` 아이디에 속한 것 중에 주문 상태가 `조리` 또는 `식사`일 경우 예외 발생한다.
  * 각 `주문 테이블`의 `단체 지정` 아이디를 삭제한다.
  * 각 `주문 테이블`을 데이터베이스에 저장한다.
  

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
