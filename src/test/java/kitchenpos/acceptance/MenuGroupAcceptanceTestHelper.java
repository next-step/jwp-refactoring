package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;

class MenuGroupAcceptanceTestHelper {

    static ExtractableResponse<Response> createMenuGroup(String name) {
        final MenuGroup requestBody = new MenuGroup(name);
        return AcceptanceTestHelper.post("/api/menu-groups", requestBody);
    }

    static ExtractableResponse<Response> getMenuGroups() {
        return AcceptanceTestHelper.get("/api/menu-groups");
    }

}
