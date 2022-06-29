# 키친포스

## 요구 사항

### 1) 키친포스 요구 사항 정리

* 메뉴그룹
    * 메뉴 그룹을 생성한다
    * 메뉴 그룹 전체 리스트를 조회한다

* 메뉴
    - 메뉴를 생성한다
        - 가격 = 상품별 가격 * 상품별 갯수이며, 합이 0원 이상이어야한다.
        - 유효한 메뉴 그룹이 있어야한다.
        - 메뉴 가격이, 메뉴에 포함된 상품들의 합보다 비싸면 안된다.
        - 유효하지 않은 상품으로 메뉴를 생성할 수 없다.
    - 메뉴 상품을 포함한 메뉴 전체 리스트를 조회한다
    
* 주문
    - 주문을 생성한다
        - 주문에 포함된 메뉴가 있어야한다.
        - 주문에 포함된 메뉴는 유효한 메뉴여야 한다.
        - 테이블이 지정되어야한다.
        - 테이블은 비어있어야한다.
        - 주문생성시 조리중으로 변경
    - 메뉴를 포함한 주문 전체 리스트를 조회한다
    - 주문상태를 변경한다
        - 변경하려는 주문은 유효한 주문이어야한다.
        - 주문상태가 '완료'일 시 변경 불가

* 상품
    - 상품을 생성한다
        - 가격 설정 0원 이상
    - 상품 리스트를 조회한다

* 단체지정
    - 단체를 생성한다
        - 테이블은 2개 이상
        - 단체로 지정된 테이블은 유효한 테이블이여야한다.
        - 중복된 테이블이 단체로 지정될 수 없다.
        - 단체에 지정된 테이블은 다른 단체에 지정되어있지 않아야한다.
        - 해당 테이블은 비어있지 않아야한다
    - 테이블을 지정된 단체에서 해제한다.
        - 조리중, 식사중 테이블이 있으면 해제 불가
        - 지정된 단체에서 해당 테이블을 제외한다

* 테이블
    - 테이블을 생성한다
    - 테이블 전체 리스트를 조회한다
    - 테이블이 비어있는지 여부를 수정한다
        - 단체가 지정되어 있지 않을 때
        - 조리중, 식사중이 아닐 때
    - 테이블 손님 숫자를 변경한다
        - 비어있지 않은 테이블의 손님 숫자만 변경가능

### 2) 테스트코드 작성
    * [v] 메뉴그룹 단위 테스트
    * [v] 메뉴 단위 테스트
    * [v] 주문 단위 테스트
    * [v] 상품 단위 테스트
    * [v] 단체지정 단위 테스트
    * [v] 테이블 단위 테스트

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


### Step2 - 서비스 리팩토링

- Lombok 사용하지 않기
- 무분별한 setter 메서드 사용하지 않기
- 자바 코드 컨벤션을 지키면서 프로그래밍한다.
    - 기본적으로 Google Java Style Guide을 원칙으로 한다.
    - 단, 들여쓰기는 '2 spaces'가 아닌 '4 spaces'로 한다.
- indent(인덴트, 들여쓰기) depth를 2를 넘지 않도록 구현한다. 1까지만 허용한다.
    - 예를 들어 while문 안에 if문이 있으면 들여쓰기는 2이다.
    - 힌트: indent(인덴트, 들여쓰기) depth를 줄이는 좋은 방법은 함수(또는 메서드)를 분리하면 된다.
- 3항 연산자를 쓰지 않는다.
- else 예약어를 쓰지 않는다.
    - else 예약어를 쓰지 말라고 하니 switch/case로 구현하는 경우가 있는데 switch/case도 허용하지 않는다.
    - 힌트: if문에서 값을 반환하는 방식으로 구현하면 else 예약어를 사용하지 않아도 된다.
- 모든 기능을 TDD로 구현해 단위 테스트가 존재해야 한다. 단, UI(System.out, System.in) 로직은 제외
    - 핵심 로직을 구현하는 코드와 UI를 담당하는 로직을 구분한다.
    - UI 로직을 InputView, ResultView와 같은 클래스를 추가해 분리한다.
- 함수(또는 메서드)의 길이가 10라인을 넘어가지 않도록 구현한다.
    - 함수(또는 메소드)가 한 가지 일만 하도록 최대한 작게 만들어라.
- 배열 대신 컬렉션을 사용한다.
- 모든 원시 값과 문자열을 포장한다
- 줄여 쓰지 않는다(축약 금지).
- 일급 컬렉션을 쓴다.
- 모든 엔티티를 작게 유지한다.
- 3개 이상의 인스턴스 변수를 가진 클래스를 쓰지 않는다.
