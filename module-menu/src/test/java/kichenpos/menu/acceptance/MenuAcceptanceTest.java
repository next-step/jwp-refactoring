package kichenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kichenpos.menu.ui.dto.MenuCreateRequest;
import kichenpos.menu.ui.dto.MenuCreateResponse;
import kichenpos.menu.ui.dto.MenuProductCreateRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.stream.Stream;

import static kichenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록됨;
import static kichenpos.menu.acceptance.MenuGroupAcceptanceTest.세마리메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("메뉴 관리")
public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuCreateRequest 파닭_메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        파닭_메뉴 = 메뉴_요청_생성();
    }

    @TestFactory
    Stream<DynamicTest> 메뉴_관리_시나리오() {
        return Stream.of(
                dynamicTest("메뉴를 등록한다", this::메뉴를_등록한다),
                dynamicTest("메뉴 목록을 조회한다.", this::메뉴_목록을_조회한다)
        );
    }

    private void 메뉴를_등록한다() {
        // when
        ExtractableResponse<Response> 메뉴_등록_응답 = 메뉴_등록_요청(파닭_메뉴);

        // then
        메뉴_등록됨(메뉴_등록_응답);
    }

    private void 메뉴_목록을_조회한다() {
        // when
        ExtractableResponse<Response> 메뉴_목록_조회_응답 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(메뉴_목록_조회_응답);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuCreateRequest request) {
        return AcceptanceTest.post("/api/menus", request);
    }

    public static void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static MenuCreateResponse 메뉴_등록됨(MenuCreateRequest request) {
        return 메뉴_등록_요청(request).as(MenuCreateResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return AcceptanceTest.get("/api/menus");
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(ArrayList.class)).hasSize(1);
    }

    public static MenuCreateRequest 메뉴_요청_생성() {
        Long 세마리메뉴_id = 메뉴_그룹_등록됨(세마리메뉴).getId();
        Long 파닭_id = ProductAcceptanceTest.상품_등록됨(ProductAcceptanceTest.파닭).getId();

        MenuProductCreateRequest 메뉴상품 = new MenuProductCreateRequest(파닭_id, 2L);

        return new MenuCreateRequest("파닭", BigDecimal.valueOf(18_000), 세마리메뉴_id, Collections.singletonList(메뉴상품));
    }
}
