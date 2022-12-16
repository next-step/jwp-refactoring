package kitchenpos.menu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.dto.MenuProductRequest;
import kitchenpos.dto.MenuRequest;
import org.springframework.http.MediaType;

public class MenuFixture {

    public static ExtractableResponse<Response> 메뉴_등록(String name, BigDecimal price,
        Long menuGroupId, List<MenuProductRequest> menuProducts) {
        MenuRequest menuRequest = new MenuRequest(name, price, menuGroupId, menuProducts);

        return RestAssured
            .given().log().all()
            .body(menuRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/menus")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회() {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/menus")
            .then().log().all()
            .extract();
    }
}
