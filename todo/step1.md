# 🚀 1단계 - 테스트를 통한 코드 보호
## 요구 사항 1
`kitchenpos` 패키지의 코드를 보고 키친포스의 요구 사항을 `README.md`에 작성한다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.
- [마크다운(Markdown) - Dooray!](https://dooray.com/htmls/guides/markdown_ko_KR.html)

## 요구 사항 2
정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다. 모든 Business Object에 대한 테스트 코드를 작성한다. `@SpringBootTest`를 이용한 
통합 테스트 코드 또는 `@ExtendWith(MockitoExtension.class)`를 이용한 단위 테스트 코드를 작성한다.
인수 테스트 코드 작성은 권장하지만 필수는 아니다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.
- [Testing in Spring Boot - Baeldung](https://www.baeldung.com/spring-boot-testing)
- [Exploring the Spring Boot TestRestTemplate](https://www.baeldung.com/spring-boot-testresttemplate)

## 프로그래밍 요구 사항
Lombok은 그 강력한 기능만큼 사용상 주의를 요한다.
- 무분별한 setter 메서드 사용
- 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
- [Lombok 사용상 주의점(Pitfall)](https://kwonnam.pe.kr/wiki/java/lombok/pitfall)
이번 과정에서는 Lombok 없이 미션을 진행해 본다.

## 힌트
`http` 디렉터리의 `.http` 파일(HTTP client)을 보고 어떤 요청을 받는지 참고한다.
- [IntelliJ의 .http를 사용해 Postman 대체하기](https://jojoldu.tistory.com/266)
```shell
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

`src/main/resources/db/migration` 디렉터리의 `.sql` 파일을 보고 어떤 관계로 이루어져 있는지 참고한다.
```sql
id BIGINT(20) NOT NULL AUTO_INCREMENT,
order_table_id BIGINT(20) NOT NULL,
order_status VARCHAR(255) NOT NULL,
ordered_time DATETIME NOT NULL,
PRIMARY KEY (id)
```

아래의 예제를 참고한다.
```text
### 상품

* 상품을 등록할 수 있다.
* 상품의 가격이 올바르지 않으면 등록할 수 없다.
    * 상품의 가격은 0 원 이상이어야 한다.
* 상품의 목록을 조회할 수 있다.
```
텐트를 세우기 위해 말뚝이 필요하듯이 리팩터링을 하기 위해선 테스트 코드가 필요하다.
![step1_image.png](images/step1_image.png)

### Business Object Test
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

### Controller Test
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

## 📚 Todo List 📚
- [x] 키친포스의 요구사항 작성하기
- [x] 테스트 코드 작성하기
  - [x] MenuGroup에 관한 테스트 코드 작성하기
  - [x] Menu에 관한 테스트 코드 작성하기
  - [x] Order에 관한 테스트 코드 작성하기
  - [x] Product에 관한 테스트 코드 작성하기
  - [x] TableGroup에 관한 테스트 코드 작성하기
  - [x] Table에 관한 테스트 코드 작성하기 
