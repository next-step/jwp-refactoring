# 키친포스

## 요구 사항

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

## 1단계 - 테스트를 통한 코드 보호
- [x] 요구사항 정리하기
- [x] 요구사항 테스트 코드 작성하기

### 실습 환경 구축

[리팩터링](https://github.com/next-step/jwp-refactoring) 저장소를 기반으로 미션을 진행한다. 온라인 코드 리뷰 요청 1단계 문서를 참고해 실습 환경을 구축한다.
[온라인 코드 리뷰 요청 1단계](https://edu.nextstep.camp/s/Reggx5FJ/ls/Pdm0q6fC) 문서를 참고해 실습 환경을 구축한다.

1. 미션 시작 버튼을 눌러 미션을 시작한다.
2. 저장소에 자신의 GitHub 아이디로 된 브랜치가 생성되었는지 확인한다.
3. 저장소를 자신의 계정으로 Fork 한다.

### 요구 사항 1
`kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 `README.md`에 작성한다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.
- [마크다운](https://dooray.com/htmls/guides/markdown_ko_KR.html)

### 요구 사항 2
- 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다. 모든 Business Object에 대한 테스트 코드를 작성한다.
- `@SpringBootTest`를 이용한 통합 테스트 코드 또는 `@ExtendWith(MockitoExtension.class)`를 이용한 단위 테스트 코드를 작성한다.
- 인수 테스트 코드 작성은 권장하지만 필수는 아니다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.
- [Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
- [Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)

### 프로그래밍 요구 사항
- Lombok은 그 강력한 기능만큼 사용상 주의를 요한다.
    - 무분별한 setter 메서드 사용
    - 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
    - [Lombok 주의사항](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)
- 이번 과정에서는 Lombok 없이 미션을 진행해 본다.

### 힌트
- `http` 디렉터리의 `.http` 파일(HTTP client)을 보고 어떤 요청을 받는지 참고한다.
- [intellij의 .http 사용하기](https://jojoldu.tistory.com/266)
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

- `src/main/resources/db/migration` 디렉터리의 `.sql` 파일을 보고 어떤 관계로 이루어져 있는지 참고한다.
    ```sql
    id BIGINT(20) NOT NULL AUTO_INCREMENT,
    order_table_id BIGINT(20) NOT NULL,
    order_status VARCHAR(255) NOT NULL,
    ordered_time DATETIME NOT NULL,
    PRIMARY KEY (id)
    ```
- 아래의 예제를 참고한다.
    ```text
    ### 상품
    
    * 상품을 등록할 수 있다.
    * 상품의 가격이 올바르지 않으면 등록할 수 없다.
        * 상품의 가격은 0 원 이상이어야 한다.
    * 상품의 목록을 조회할 수 있다.
   ```

  #### Business Object Test
    ```java
    @ExtendWith(MockitoExtension.class)
    public class BoTest {
        @Mock
        private Dao dao;
    
        @InjectMocks
        private Bo bo;
    
        @Test
        public void test() {
            given(dao.findById(anyLong()))
                    .willReturn(new Object());
        }
    }
    ```

  #### Controller Test
    ```java
    @WebMvcTest
    public class ControllerTest {
        @Autowired
        private MockMvc mockMvc;
        
        @Test
        public void test() {
            webMvc.perform(get("/"))
                    .andDo(print())
                    .andExpect(status().isOk());
           
        }
    }
    ```

--- 

## 1. 키친포스 요구사항 정리

```text
### 상품
* 상품의 목록을 조회할 수 있다. 
* 상품을 등록할 수 있다.
* 상품의 가격이 올바르지 않으면 등록할 수 없다.
    * 상품의 가격은 0 원 이상이어야 한다.


### 메뉴그룹 
* 메뉴 그룹을 등록할 수 있다.
* 메뉴 그룹을 조회할 수 있다.

### 메뉴
* 메뉴의 목록을 조회할 수 있다. 
* 메뉴를 등록할 수 있다.
* 메뉴의 가격이 올바르지 않으면 등록할 수 없다.
    * 메뉴의 가격은 0원 이상이어야 한다.
    * 메뉴의 가격은 메뉴상품들의 수량과 가격의 합과 일치하여야 한다.
* 메뉴는 메뉴상품이 올바르지 않으면 등록할 수 없다.
    * 메뉴상품은 상품이 등록되어 있어야 한다.
* 메뉴는 메뉴그룹이 올바르지 않으면 등록할 수 없다.
    * 메뉴그룹이 등록되어 있어야 한다.


### 테이블 
* 테이블 목록을 조회할 수 있다.
* 테이블을 등록할 수 있다.
* 테이블의 상태를 변경할 수 있다.
* 테이블 정보가 올바르지 않은 경우 상태를 변경할 수 없다.
    * 존재하지 않은 테이블은 상태를 변경할 수 없다.
    * 테이블이 그룹 테이블인 경우 변경할 수 없다.
    * 테이블의 주문 상태가 조리, 식사인 경우 변경할 수 없다.
* 테이블의 사용자 수를 변경할 수 있다.
* 테이블 정보가 올바르지 않을 경우 사용자 수를 변경할 수 없다.
    * 테이블의 사용자 수는 0명 이상이어야 한다.
    * 존재하지 않은 테이블의 사용자 수를 변경할 수 없다.
    * 빈 테이블의 사용자 수를 변경할 수 없다.


### 그룹 테이블
* 테이블을 그룹화할 수 있다.
* 테이블 리스트가 올바르지 않은 경우 그룹화 할 수 없다.
    * 테이블 리스트가 2개 보다 작은 경우 그룹화 할 수 없다.
    * 등록되지 않은 테이블인 경우 등록할 수 없다.
    * 테이블이 비어있지 않거나 그룹화된 테이블 인 경우 등록할 수 없다.
* 테이블의 그룹화를 해제할 수 있다.
* 테이블 리스트가 올바르지 않은 경우 그룹회를 해제할 수 없다.
    * 테이블 리스트의 주문 상태가 조리, 식사중인 경우 해제할 수 없다.
        
### 주문 
* 주문을 등록할 수 있다.
* 주문은 주문정보로 구성되어 있다.
* 주문은 주문정보가 올바르지 않으면 등록할 수 없다.
    * 주문정보가 존재하지 않을 경우 등록할 수 없다.
    * 주문정보의 메뉴는 등록되어 있어야 한다.
* 주문은 테이블정보가 올바르지 않으면 등록할 수 없다.
    * 테이블정보가 존재하지 않을 경우 등록할 수 없다.
    * 테이블이 빈 테이블인 경우 등록할 수 없다.
* 주문의 목록을 조회할 수 있다.
* 주문의 상태를 변경할 수 있다.
    * 주문이 존재하지 않은 경우 상태를 변경할 수 없다.
    * 주문 상태가 완료인 경우 상태를 변경할 수 없다.
```
