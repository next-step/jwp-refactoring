# 7주차 - 레거시 코드 리팩터링
## 1단계 - 테스트를 통한 코드 보호

### 요구사항

- `kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다
  <br>([마크다운(Markdown) - Dooray!](https://dooray.com/htmls/guides/markdown_ko_KR.html) 참고)
- 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다
  <br>-> 모든 Business Object에 대한 테스트 코드를 작성한다
  - @SpringBootTest를 이용한 통합 테스트 코드
  <br>또는 @ExtendWith(MockitoExtension.class)를 이용한 단위 테스트 코드를 작성한다.
  - 인수 테스트 코드 작성은 권장하지만 필수는 아님
  - 아래 문서 적극 활용
    <br>[Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
    <br>[Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)


### 프로그래밍 요구사항

Lombok은 그 강력한 기능만큼 사용상 주의를 요한다

- 무분별한 setter 메서드 사용
- 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
- Lombok 사용상 주의점(Pitfall)

이번 과정에서는 Lombok 없이 미션을 진행해 본다


### 힌트
- http 디렉터리의 .http 파일(HTTP client)을 보고 어떤 요청을 받는지 참고한다
<br>[IntelliJ의 .http를 사용해 Postman 대체하기](https://jojoldu.tistory.com/266)

- `src/main/resources/db/migration`디렉터리의 `.sql`파일을 보고 어떤 관계로 이루어져 있는지 참고한다

- 요구사항 정리 시 아래의 예제를 참고한다.
    ```
    ### 상품
    
    * 상품을 등록할 수 있다.
    * 상품의 가격이 올바르지 않으면 등록할 수 없다.
        * 상품의 가격은 0 원 이상이어야 한다.
    * 상품의 목록을 조회할 수 있다.
    ```

- 텐트를 세우기 위해 말뚝이 필요하듯이 리팩터링을 하기 위해선 테스트 코드가 필요하다

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
                    .andExpect(status().isOk())
            ;
        }
    }
    ```