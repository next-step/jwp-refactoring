package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;

public class MenuAcceptance {
    public static ExtractableResponse<Response> create_menu(String name, BigDecimal price, Long menuGroupId,
            HashMap<Long, Long> quantityOfProducts) {
        MenuRequest request = new MenuRequest(name, price, menuGroupId, quantityOfProducts);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> menu_list_has_been_queried() {
        return RestAssured.given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }
}
