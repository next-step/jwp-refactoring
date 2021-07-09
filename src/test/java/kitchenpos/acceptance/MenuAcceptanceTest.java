package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록_되어있음;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_등록_되어있음;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("메뉴 관리 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    private ProductResponse 후라이드;
    private MenuGroupResponse 치킨메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();
        ProductRequest productRequest = new ProductRequest("후라이드", BigDecimal.valueOf(15_000));
        후라이드 = 상품_등록_되어있음(productRequest);
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("치킨메뉴");
        치킨메뉴 = 메뉴그룹_등록_되어있음(menuGroupRequest);
    }

    @DisplayName("메뉴를 관리한다.")
    @Test
    public void menuManager() throws Exception {
        MenuProductRequest 후라이드두개 = new MenuProductRequest(후라이드.getId(), 2L);
        MenuRequest 후라이드메뉴 = new MenuRequest("후라이드", BigDecimal.valueOf(29_000), 치킨메뉴.getId(),
                Arrays.asList(후라이드두개));

        ExtractableResponse<Response> postResponse = 메뉴_등록_요청(후라이드메뉴);
        메뉴_등록됨(postResponse);

        ExtractableResponse<Response> getResponse = 메뉴_목록조회_요청();
        메뉴_목록조회됨(getResponse);
    }

    public static MenuResponse 메뉴_등록_되어있음(MenuRequest menuRequest) {
        ExtractableResponse<Response> response = 메뉴_등록_요청(menuRequest);
        메뉴_등록됨(response);
        return response.as(MenuResponse.class);
    }

    private void 메뉴_목록조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
    }

    private ExtractableResponse<Response> 메뉴_목록조회_요청() {
        return get("/api/menus");
    }

    private static void 메뉴_등록됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        return post("/api/menus", menuRequest);
    }
}
