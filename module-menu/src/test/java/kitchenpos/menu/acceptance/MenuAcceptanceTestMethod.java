package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.acceptance.code.AcceptanceTest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceTestMethod extends AcceptanceTest {

    private static final String MENU_GROUP_PATH = "/api/menus";
    private static final String DOT = ".";

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest params) {
        return post(MENU_GROUP_PATH, params);
    }

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(MenuRequest params) {
        return 메뉴_등록_요청(params);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get(MENU_GROUP_PATH);
    }

    public static void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(parseURIFromLocationHeader(response)).isNotBlank();
    }

    public static void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response,
                                    List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedIds = createdResponses.stream()
                .map(AcceptanceTest::parseIdFromLocationHeader)
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList(DOT, MenuResponse.class)
                .stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        assertThat(actualIds).containsAll(expectedIds);
    }
}