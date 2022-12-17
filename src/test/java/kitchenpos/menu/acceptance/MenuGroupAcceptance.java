package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.springframework.http.MediaType;

public class MenuGroupAcceptance {
    public static ExtractableResponse<Response> create_menu_group(String name) {
        MenuGroupRequest request = new MenuGroupRequest(name);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> menu_group_list_has_been_queried() {
        return RestAssured.given().log().all()
                .when().get("/api/menu-groups")
                .then().log().all()
                .extract();
    }
}
