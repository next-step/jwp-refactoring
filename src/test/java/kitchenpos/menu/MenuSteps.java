package kitchenpos.menu;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;

import static kitchenpos.AcceptanceTest.*;

public class MenuSteps {

    private static final String MENU_URI = "/api/menus";

    public static MenuResponse 메뉴_등록되어_있음(MenuRequest menuRequest) {
        return 메뉴_등록_요청(menuRequest).as(MenuResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuRequest menuRequest) {
        return post(MENU_URI, menuRequest);
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return get(MENU_URI);
    }
}
