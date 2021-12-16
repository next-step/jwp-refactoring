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
- [ ] 요구사항 테스트 코드 작성하기

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


#### 1) 상품(tb.product)을 관리한다
- `상품명` 과 `가격`으로 구성된 상품을 `등록`할 수 있어야 한다.
- 등록된 상품의 `목록을 조회`할 수 있어야 한다.

#### 2) 메뉴그룹(tb.menu_group)을 관리한다.
- `이름`을 가지는 메뉴 그룹을 `등록`할 수 있어야 한다.
- 등록된 메뉴그룹의 `목록을 조회`할 수 있어야 한다.

#### 3) 메뉴(tb.menu)를 관리한다.
- `메뉴그룹`에 해당하는 `상품`들로 이루어진 `메뉴`를 등록할 수 있어야 한다.
- 등록된 메뉴들의 `목록을 조회`할 수 있다.

#### 4) 메뉴 상품(tb.menu_product)을 관리한다.
- 메뉴 상품은 `상품번호`, `메뉴`, `수량`을 등록할 수 있어야 한다.

#### 5) 주문(tb.orders)을 관리한다.
- `주문테이블정보`, `주문 상태`, `주문정보`, `주문시간`으로 이루어진 주문을 등록할 수 있어야 한다.
- 등록된 주문의 `목록을 조회`할 수 있어야 한다.
- `주문 상태를 변경`할 수 있어야 한다.

#### 5) 주문 정보(tb.order_line_item)을 관리한다.
- 주문 정보은 `주문번호`, `메뉴`, `메뉴의 수량`을 등록할 수 있어야 한다.

#### 6) 주문 테이블(tb.order_table)을 관리한다.
- `방문손님의 수`와 `테이블의 사용유무`를 나타내는 테이블 정보를 등록할 수 있어야 한다.
- 단체 지정을 할 경우 `테이블그룹정보`를 포함해야 한다.

#### 7) 그룹 테이블(tb.table_group)
- 단체 지정을 위한 `그룹 테이블`을 등록할 수 있어야 한다.
- `그룹 테이블`을 해제할 수 있어야 한다.
