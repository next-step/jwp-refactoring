## 요구 사항

#### Product(상품)
- 상품은 `상품명`과 `가격`으로 구성된다.
- 상품을 생성할 수 있다.
  - 상품의 가격은 0원 이상이어야 한다.
- 상품의 목록을 조회할 수 있다.

#### MenuGroup(메뉴 그룹)
- 메뉴 그룹은 `메뉴그룹명`으로 구성된다.
- 메뉴 그룹을 생성할 수 있다.
- 메뉴 그룹의 목록을 조회할 수 있다.

#### Menu(메뉴)
- 메뉴 그룹은 `메뉴명`, `가격`, `메뉴그룹`, `상품목록`으로 구성된다.  
- 메뉴를 생성할 수 있다.
  - 메뉴의 가격은 0원 이상이어야 한다.
  - 메뉴의 가격은 메뉴 구성 상품들의 총 가격보다 작거나 같다.
  - 메뉴의 메뉴 그룹이 존재하지 않으면 메뉴를 생성할 수 없다.
  - 메뉴 상품의 상품이 등록되어 있지 않으면 메뉴를 생성할 수 없다.
- 메뉴의 목록을 조회할 수 있다.

### OrderLineItem(주문항목)
- 주문 항목은 `주문`, `주문 메뉴`, `주문 메뉴 수량`으로 구성된다.

#### OrderTable(주문 테이블)
- 주문 테이블은 `단체 주문 테이블`, `손님의 수`, `빈 테이블 여부` 로 구성된다.
- 주문 테이블을 생성할 수 있다.
- 주문 테이블의 목록을 조회할 수 있다.
- 주문 테이블의 빈 상태를 변경할 수 있다.
    - 등록된 주문 테이블이 없으면 주문 테이블의 빈 상태를 변경할 수 없다.
    - 주문 테이블이 단체로 지정이 되어있으면 빈 테이블로 상태 변경할 수 없다.
    - 주문 테이블의 주문 상태가 `조리` 또는 `식사증`이면 주문 테이블의 빈 상태를 변경할 수 없다.
- 주문 테이블의 방문한 손님 수를 변경할 수 있다.
    - 손님의 수는 최소 0 이상이다.
    - 변경할 주문 테이블이 존재해야 한다.
    - 주문 테이블이 빈 테이블이면 손님 수를 변경할 수 없다.

#### TableGroup(단체 지정)
- 단체는 `단체지정 시각`, `주문 테이블 목록`으로 구성된다.
- 단체 지정을 할 수 있다.
  - 단체로 지정할 주문 테이블은 2개 이상이어야 한다.
  - 단체로 지정할 주문 테이블이 등록된 주문 테이블이 아니면 단체로 지정할 수 없다.
  - 단체로 지정할 주문 테이블 중 빈 테이블이 존재하거나 이미 단체로 지정이되어 있다면 단제로 지정할 수 없다.
- 단체 지정을 취소할 수 있다.
    - 단체 지정된 주문 테이블들의 상태가 조리 또는 식사이면 단체 지정을 취소할 수 없다.

#### Order(주문)
- 주문은 `주문한 테이블`, `주문상태`, `주문시각`, `주문 항목 목록`로 구성된다.
- 주문을 생성 할 수 있다.
    - 주문 항목은 필수값이다.
    - 즈믄 메뉴 하나당 주문 항목이 한개씩 존재해야 한다.
    - 주문 테이블이 등록되어 있지 않다면 주문을 생성할 수 없다.
    - 주문 테이블이 빈 테이블이면 주문을 생성할 수 없다.
- 주문의 목록을 조회할 수 있다.
- 주문의 상태를 변경할 수 있다.
  - 주문 상태가 완료되었거나 주문이 없으면 주문 상태를 변경할 수 없다.

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

🚀 1단계 - 테스트를 통한 코드 보호
<details>
<summary> </summary>

#### 요구사항1
- kitchenpos 패키지의 코드를 보고 키친포스의 요구 사항을 README.md에 작성한다. 미션을 진행함에 있어 아래 문서를 적극 활용한다.
- https://dooray.com/htmls/guides/markdown_ko_KR.html

#### 요구사항2
- 정리한 키친포스의 요구 사항을 토대로 테스트 코드를 작성한다.   
- 모든 Business Object에 대한 테스트 코드를 작성한다. `@SpringBootTest`를 이용한 통합 테스트 코드 또는 `@ExtendWith(MockitoExtension.class)`를 이용한 단위 테스트 코드를 작성한다.
- https://www.baeldung.com/spring-boot-testing
- https://www.baeldung.com/spring-boot-testresttemplate

#### 프로그래밍 요구 사항
Lombok은 그 강력한 기능만큼 사용상 주의를 요한다.
  * 무분별한 setter 메서드 사용
  * 객체 간에 상호 참조하는 경우 무한 루프에 빠질 가능성
  * Lombok 사용상 주의점(Pitfall)   

이번 과정에서는 Lombok 없이 미션을 진행해 본다.

#### 힌트
`http` 디렉터리의 `.http` 파일(HTTP client)을 보고 어떤 요청을 받는지 참고한다.
  * https://jojoldu.tistory.com/266

```
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
```
id BIGINT(20) NOT NULL AUTO_INCREMENT,
order_table_id BIGINT(20) NOT NULL,
order_status VARCHAR(255) NOT NULL,
ordered_time DATETIME NOT NULL,
PRIMARY KEY (id)
```

```
### 상품

* 상품을 등록할 수 있다.
* 상품의 가격이 올바르지 않으면 등록할 수 없다.
    * 상품의 가격은 0 원 이상이어야 한다.
* 상품의 목록을 조회할 수 있다.
```

**Business Object Test**
```
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

**Controller Test**
```
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
</details>
