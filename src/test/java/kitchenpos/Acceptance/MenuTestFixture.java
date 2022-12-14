package kitchenpos.Acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.TestFixture;
import kitchenpos.domain.Menu;
import org.assertj.core.api.Assertions;

public class MenuTestFixture extends TestFixture {
    public static final String MENU_BASE_URI = "/api/menus";

    public static ExtractableResponse<Response> 메뉴_생성_요청함(Menu menu) {
        return post(MENU_BASE_URI, menu);
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
                .getList(".", Menu.class)
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        List<Long> expectIds = menuResponses.stream()
                .map(r -> r.as(Menu.class))
                .collect(Collectors.toList())
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        Assertions.assertThat(actualIds).containsAll(expectIds);
    }
}
