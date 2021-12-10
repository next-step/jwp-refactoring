package kitchenpos.menu.domain.fixture;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuRequest;

import static kitchenpos.utils.AcceptanceFixture.get;
import static kitchenpos.utils.AcceptanceFixture.post;

public class MenuAcceptanceFixture {

    public static ExtractableResponse<Response> 메뉴_생성(MenuRequest menuRequest) {
        return post("/new/api/menus", menuRequest);
    }

    public static ExtractableResponse<Response> 메뉴_전체_조회() {
        return get("/new/api/menus");
    }

}
