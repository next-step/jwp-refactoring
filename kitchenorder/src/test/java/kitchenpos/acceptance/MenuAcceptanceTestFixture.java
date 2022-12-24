package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import org.springframework.http.MediaType;

public class MenuAcceptanceTestFixture {
    public static ExtractableResponse<Response> 메뉴_생성_요청(MenuRequest menuRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menuRequest)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static MenuResponse 메뉴_생성_되어있음(MenuRequest menuRequest) {
        return 메뉴(메뉴_생성_요청(menuRequest));
    }

    public static MenuResponse 메뉴(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", MenuResponse.class);
    }
}
