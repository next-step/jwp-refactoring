package kitchenpos.menu.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.TestFixture;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.api.Assertions;

public class MenuTestFixture extends TestFixture {
    public static final String MENU_BASE_URI = "/api/menus";

    public static ExtractableResponse<Response> 메뉴_생성_요청함(MenuRequest menuRequest) {
        return post(MENU_BASE_URI, menuRequest);
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청함() {
        return get(MENU_BASE_URI);
    }

    public static void 메뉴_조회_요청_응답됨(ExtractableResponse<Response> response) {
        ok(response);
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        created(response);
    }

    public static void 메뉴_조회_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> menuResponses) {
        List<Long> actualIds = response.jsonPath()
                .getList(".", MenuResponse.class)
                .stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        List<Long> expectIds = menuResponses.stream()
                .map(r -> r.as(MenuResponse.class))
                .collect(Collectors.toList())
                .stream()
                .map(MenuResponse::getId)
                .collect(Collectors.toList());

        Assertions.assertThat(actualIds).containsAll(expectIds);
    }
}
