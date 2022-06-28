# 키친포스

## 요구 사항
### 상품(Product)
+ [x] 상품은 상품명과 가격 정보를 가진다
+ [x] 상품을 생성한다
  + [x] 상품의 가격이 0원 이상이어야 한다.
+ [x] 상품을 조회한다

### 메뉴 그룹(Menu Group)
+ [x] 메뉴 그룹은 메뉴 그룹명 정보를 가진다
+ [x] 메뉴 그룹을 생성한다
+ [x] 메뉴 그룹을 조회한다

### 메뉴(Menu)
+ [x] 메뉴는 상품명, 가격 정보, 메뉴 그룹 정보, 메뉴 상품 정보를 가진다
+ [x] 메뉴를 생성한다
  + [x] 메뉴 가격은 0원 이상의 금액이어야 한다
    + [x] 메뉴 가격은 메뉴 상품의 상품 정보를 통해 가격 정보를 가져와 합한 금액이다
  + [x] 메뉴 그룹은 유효한 id 정보 하나를 가진다
  + [x] 메뉴 상품은 메뉴 상품 정보 여러개를 가진다
+ [x] 메뉴를 조회한다

### 메뉴 상품(Menu Product)
+ [x] 메뉴 상품은 메뉴 정보, 상품 정보, 수량 정보를 가진다
+ [x] 메뉴 상품을 생성한다
+ [x] 메뉴 상품을 조회한다

### 주문 테이블(Order Table)
+ [x] 주문 테이블은 단체 지정(Table Group) 정보, 방문한 손님 수 정보, 빈 테이블 여부를 가진다
+ [x] 주문 테이블을 생성한다
+ [x] 주문 테이블을 조회한다
+ [x] 단체 지정(Table Group) 정보를 변경한다
  + [x] 단체가 지정되지 않는 경우 null이다
  + [x] 단체를 지정하는 경우 해당 단체 지정 정보를 저장한다
  + [x] 주문 상태가 조리(COOKING), 식사(MEAL) 이면 변경이 불가능하다
+ [x] 방문한 손님 수 정보를 변경한다
  + [x] 변경하려는 방문한 손님 수는 0명 이상이어야 한다
+ [x] 빈 테이블 여부를 변경한다
  + [x] 단체를 지정하는 경우 빈 테이블 여부를 false로 변경한다

### 주문(Order)
+ [x] 주문은 주문 테이블 정보, 주문 상태, 주문 시간, 주문 항목(order line item)을 가진다
+ [x] 주문을 생성한다
  + [x] 주문 테이블 정보는 유효한 id 정보 하나를 가진다
  + [x] 주문 항목은 유효한 메뉴 id 1개 이상을 가진다
  + [x] 주문 항목은 모두 존재하는 메뉴여야 한다
  + [x] 최초 주문시 주문 상태는 조리(COOKING)이다
  + [x] 최초 주문시 주문 시간은 현재 시간을 저장한다
+ [x] 주문을 조회한다

### 주문 상태(Order Status)
+ [x] 주문 상태는 조리(COOKING), 식사(MEAL), 계산 완료(COMPLETION) 값을 가진다
+ [x] 주문 상태를 변경한다
  + [x] 변경하려는 주문의 현재 상태는 계산 완료(COMPLETION) 이면 안된다

### 단체 지정(Table Group)
+ [x] 단체 지정은 생성 시간, 주문 테이블 정보를 가진다
+ [x] 단체 지정을 생성한다
  + [x] 주문 테이블(손님)은 2개 이상이어야 한다
  + [x] 주문 테이블(손님)은 모두 존재하는 메뉴에 대한 주문을 가진다
  + [x] 주문 테이블(손님)은 단체 지정 정보를 가져야 한다
+ [x] 단체 지정을 해제한다
  + [x] 주문 테이블의 주문 상태가 조리(COOKING), 식사(MEAL) 이면 변경이 불가능하다
  + [x] 주문 테이블의 단체 지정 정보를 null로 변경한다

### 주문 항목(order line item)
+ [x] 주문 항목은 주문 정보, 메뉴 정보, 수량 정보를 가진다

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

---

## 🚀 1단계 - 테스트를 통한 코드 보호
### 요구 사항 1
+ `kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 `README.md`에 작성한다. 미션을 진행함에 있어 아래 문서를 적극 활용한다. 
  + [마크다운(Markdown) - Dooray!](https://dooray.com/htmls/guides/markdown_ko_KR.html)

### 요구 사항 2
+ 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다. 모든 Business Object에 대한 테스트 코드를 작성한다. `@SpringBootTest`를 이용한 통합 테스트 코드 또는 `@ExtendWith(MockitoExtension.class)`를 이용한 단위 테스트 코드를 작성한다.
+ 인수 테스트 코드 작성은 권장하지만 필수는 아니다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.
  + [Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
  + [Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)

### 프로그래밍 요구 사항
+ Lombok은 그 강력한 기능만큼 사용상 주의를 요한다.
  + 무분별한 setter 메서드 사용
  + 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
  + [Lombok 사용상 주의점(Pitfall)](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)
+ 이번 과정에서는 Lombok 없이 미션을 진행해 본다.

### 힌트
#### http 디렉터리의 .http 파일(HTTP client)을 보고 어떤 요청을 받는지 참고한다.
+ [IntelliJ의 .http를 사용해 Postman 대체하기](https://jojoldu.tistory.com/266)
```text
###
POST {{host}}/api/menu-groups
Content-Type: application/json

{
"name": "추천메뉴"
}

###
GET {{host}}/api/menus-groups

###
```
#### `src/main/resources/db/migration` 디렉터리의 `.sql` 파일을 보고 어떤 관계로 이루어져 있는지 참고한다.
```sql
id BIGINT(20) NOT NULL AUTO_INCREMENT,
order_table_id BIGINT(20) NOT NULL,
order_status VARCHAR(255) NOT NULL,
ordered_time DATETIME NOT NULL,
PRIMARY KEY (id)
```
#### 아래의 예제를 참고한다.
```text
### 상품

* 상품을 등록할 수 있다.
* 상품의 가격이 올바르지 않으면 등록할 수 없다.
    * 상품의 가격은 0 원 이상이어야 한다.
* 상품의 목록을 조회할 수 있다.
```
