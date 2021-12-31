package kitchenpos.menu;

import static kitchenpos.menu.MenuGroupAcceptanceTest.메뉴그룹_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.utils.RestTestApi;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private static final String  URI = "/api/menus";

    private MenuRequest 메뉴_후라이드;
    private MenuRequest 메뉴_양념치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        MenuGroupResponse 한마리메뉴그룹_응답 = 메뉴그룹_등록되어_있음(new MenuGroupRequest("한마리메뉴")).as(
            MenuGroupResponse.class);

        ProductResponse 상품후라이드_응답 = ProductAcceptanceTest.상품_생성되어_있음(
            new ProductRequest("후라이드", BigDecimal.valueOf(16_000L))).as(ProductResponse.class);

        ProductResponse 상품양념치킨_응답 = ProductAcceptanceTest.상품_생성되어_있음(
            new ProductRequest("후라이드", BigDecimal.valueOf(16_000L))).as(ProductResponse.class);

        MenuProductRequest 메뉴상품_후라이드 = new MenuProductRequest(상품후라이드_응답.getId(), 1L);
        MenuProductRequest 메뉴상품_양념치킨 = new MenuProductRequest(상품양념치킨_응답.getId(), 1L);

        메뉴_후라이드 = new MenuRequest("후라이드치킨", 상품후라이드_응답.getPrice(), 한마리메뉴그룹_응답.getId(),
            Arrays.asList(메뉴상품_후라이드));
        메뉴_양념치킨 = new MenuRequest("양념치킨", 상품양념치킨_응답.getPrice(), 한마리메뉴그룹_응답.getId(),
            Arrays.asList(메뉴상품_양념치킨));
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void testCreateMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(메뉴_후라이드);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() {
        // given
        ExtractableResponse<Response> 메뉴_후라이드_응답 = 메뉴_생성_요청(메뉴_후라이드);
        ExtractableResponse<Response> 메뉴_양념치킨_응답 = 메뉴_생성_요청(메뉴_양념치킨);

        // when
        ExtractableResponse<Response> 메뉴_목록_응답 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(메뉴_목록_응답);
        메뉴_목록_포함됨(메뉴_목록_응답, Arrays.asList(메뉴_후라이드_응답, 메뉴_양념치킨_응답));
    }

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(MenuRequest menuRequest) {
        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);
        메뉴_생성됨(response);
        return response;
    }

    private static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return RestTestApi.post(URI, menuRequest);
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 메뉴_목록_포함됨(ExtractableResponse<Response> response,
        List<ExtractableResponse<Response>> expectedResponses) {

        List<Long> responseIds = response.jsonPath().getList(".", MenuResponse.class).stream()
            .map(MenuResponse::getId)
            .collect(Collectors.toList());

        List<Long> expectedIds = expectedResponses.stream()
            .map(expectedResponse -> expectedResponse.as(MenuResponse.class))
            .map(MenuResponse::getId)
            .collect(Collectors.toList());

        assertThat(responseIds).containsAll(expectedIds);
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestTestApi.get(URI);
    }
}
