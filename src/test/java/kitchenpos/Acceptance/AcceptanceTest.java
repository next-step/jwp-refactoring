package kitchenpos.Acceptance;

import io.restassured.RestAssured;
import kitchenpos.Acceptance.utils.DatabaseCleanup;
import kitchenpos.menuGroup.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.table.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import java.util.Collections;

import static kitchenpos.menu.MenuGenerator.*;
import static kitchenpos.product.ProductGenerator.상품_생성_API_요청;
import static kitchenpos.table.TableGenerator.주문_테이블_생성_요청;
import static kitchenpos.table.TableGenerator.테이블_생성_API_호출;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("acceptance")
public class AcceptanceTest {

    protected Long 주문_테이블_아이디;
    protected Long 메뉴_그룹_아이디;
    protected Long 상품_아이디;
    protected Long 메뉴_아이디;

    @LocalServerPort
    int port;

    @Autowired
    private DatabaseCleanup databaseCleanup;

    @BeforeEach
    public void setUp() {
        RestAssured.port = port;
        databaseCleanup.execute();

        주문_테이블_아이디 = 테이블_생성_API_호출(주문_테이블_생성_요청(10)).as(OrderTableResponse .class).getId();
        메뉴_그룹_아이디 = 메뉴_그룹_생성_API_호출("메뉴 그룹").as(MenuGroupResponse.class).getId();
        상품_아이디 = 상품_생성_API_요청("상품", 1_000).as(ProductResponse.class).getId();
        MenuProductRequest 메뉴_상품_생성_요청 = 메뉴_상품_생성_요청(상품_아이디, 1L);
        메뉴_아이디 = 메뉴_생성_API_호출("메뉴", 1_000, 메뉴_그룹_아이디, Collections.singletonList(메뉴_상품_생성_요청))
                .body().jsonPath().getLong("id");
    }
}
