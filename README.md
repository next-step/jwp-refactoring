# 키친포스

## 요구 사항
### 메뉴 그룹
- 새로운 `메뉴 그룹`의 이름으로 `메뉴 그룹`을 등록할 수 있다.
- `메뉴 그룹` 목록을 조회할 수 있다.

### 상품
- 새로운 `상품`의 이름과 가격으로 `상품`을 등록할 수 있다.
    - `상품` 가격은 반드시 필요하며, 0원 이상이어야 한다.
- `상품` 목록을 조회할 수 있다.

### 메뉴
- 새로운 `메뉴`의 이름, 가격, `메뉴 그룹`, `메뉴 상품들`로 메뉴를 등록할 수 있다.
    - `메뉴`의 가격은 반드시 필요하며, 0원 미만일 수 없다.
    - 존재하지 않는 `메뉴 그룹`으로 메뉴를 등록할 수 없다.
    - `메뉴`의 가격은 `메뉴 상품들` 가격의 총합보다 비쌀 수 없다.
    - 존재하지 않는 `상품들`로 구성된 `메뉴 상품들`로 메뉴를 등록할 수 없다.
    - `메뉴`와 `메뉴 상품들`간 연관성이 보장되야 한다.
- `메뉴` 목록을 조회할 수 있다.

### 단체 지정
- `주문 테이블`들로 새로운 `단체 지정`을 할 수 있다.
    - `단체 지정` 시 지정된 일시를 기록한다.
    - 제시된 `주문 테이블`들이 없거나 2개 미만이면 `단체 지정` 할 수 없다.
    - 존재하지 않는 `주문 테이블`들로 `단체 지정` 할 수 없다.
    - 비어있지 않은(이미 찬) `주문 테이블`들은 `단체 지정` 할 수 없다.
    - 이미 `단체 지정` 된 `주문 테이블`들은 또 `단체 지정` 할 수 없다.
    - `주문 테이블`과 `단체 지정`의 연관성이 보장되야 한다.
- `단체 지정`을 해제할 수 있다.
    - `주문 상태`가 `조리` 혹은 `식사`일 경우 `단체 지정`을 해제할 수 없다.
    
### 주문 테이블
- `방문한 손님 수`와 공석 여부로 `주문 테이블`을 만들 수 있다.
    - 최초 생성 시 `주문 테이블`은 `단체 지정` 되어있지 않다.
- `주문 테이블` 목록을 조회할 수 있다.
- 특정 `주문 테이블`을 비움 상태를 바꿀 수 있다.
    - 존재하지 않는 `주문 테이블`의 비움 상태를 바꿀 수 없다.
    - `단체 지정`된 `주문 테이블`의 비움 상태를 바꿀 수 없다.
    - `주문 상태`가 `조리` 혹은 `식사`일 경우 `주문 테이블`의 비움 상태를 바꿀 수 없다.
- 특정 `주문 테이블`의 `방문한 손님 수`를 바꿀 수 있다.
    - `방문한 손님 수`를 0명 미만으로 바꿀 수 없다.
    - 존재하지 않는 `주문 테이블`의 `방문한 손님 수`를 바꿀 수 없다.
    - 비어 있는 `주문 테이블`의 `방문한 손님 수`를 바꿀 수 없다.
  
### 주문
- `주문 테이블`과 `주문 항목들`로 주문할 수 있다.
  - `주문 항목들`은 최소 1개 이상 반드시 있어야 한다.
  - `메뉴`에 없는 `주문 항목들`로 주문할 수 없다.
  - 존재하지 않는 `주문 테이블`에서 `주문` 할 수 없다.
  - `주문 테이블`이 빈 상태에서 `주문` 할 수 없다.
  - 최초 `주문` 시 `주문 상태`는 `조리`다.
  - `주문` 시 주문한 `주문 테이블`과 주문시간을 기록한다.
  - `주문`과 `주문 항목들`의 연관성이 유지되야 한다.
- `주문` 목록을 조회할 수 있다.
- `주문`의 `주문 상태`를 바꿀 수 있다.
  - 존재하지 않는 `주문`의 `주문 상태`를 바꿀 수 없다.
  - `주문 상태`가 `계산 완료`인 경우 바꿀 수 없다.
  
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

## Todo-list
- [X] 서비스 레이어 테스트를 실제 오브젝트 테스트로 전환
  - 리팩토링 시 신뢰할만한 구간이 현재 Service 레이어 밖에 없느 상태
  - 인수 테스트를 적용하기엔 공수가 많이 들고, JDBC template을 쓰는 상황에서 인수 테스트 초기 구성을 위한 노력이 너무 많이 들어감.
  - 우선적으로 가장 쉽게 접근할 수 있는 서비스 레이어 테스트를 견고하게 만들고 이를 기반으로 리팩토링하면서 점진적으로 진행 필요.
  - [X] 도메인 exeption 정의
      - 모든 오류가 같아서 정확하게 원하는 오류 상황이 발생하는지 확인이 어려움
  - [X] MenuGroupService
  - [X] MenuService
  - [X] OrderService
  - [X] ProductService
  - [X] TableGroupService
  - [X] TableService

- [ ] 도메인 레이어 고립을 위한 기본사항 적용
  - [ ] MenuGroup
    - [X] 변경 영향을 파악하기 위해 service 레이어에서 실제 오브젝트로 테스트 진행
    - [X] Controller 레이어에서 도메인 오브젝트 대신 DTO 사용
    - [ ] 도메인 오브젝트의 불필요한 setter 제거
  - [ ] Menu
    - [X] 변경 영향을 파악하기 위해 service 레이어에서 실제 오브젝트로 테스트 진행
    - [x] Controller 레이어에서 도메인 오브젝트 대신 DTO 사용
    - [ ] 도메인 오브젝트의 불필요한 setter 제거
  - [X] Order
    - [x] 변경 영향을 파악하기 위해 service 레이어에서 실제 오브젝트로 테스트 진행
    - [X] Controller 레이어에서 도메인 오브젝트 대신 DTO 사용
    - [X] 도메인 오브젝트의 불필요한 setter 제거
  - [ ] Product
    - [X] 변경 영향을 파악하기 위해 service 레이어에서 실제 오브젝트로 테스트 진행
    - [ ] Controller 레이어에서 도메인 오브젝트 대신 DTO 사용
    - [ ] 도메인 오브젝트의 불필요한 setter 제거
  - [ ] TableGroup
    - [X] 변경 영향을 파악하기 위해 service 레이어에서 실제 오브젝트로 테스트 진행
    - [ ] Controller 레이어에서 도메인 오브젝트 대신 DTO 사용
    - [ ] 도메인 오브젝트의 불필요한 setter 제거
  - [ ] Table
    - [X] 변경 영향을 파악하기 위해 service 레이어에서 실제 오브젝트로 테스트 진행
    - [ ] Controller 레이어에서 도메인 오브젝트 대신 DTO 사용
    - [ ] 도메인 오브젝트의 불필요한 setter 제거
