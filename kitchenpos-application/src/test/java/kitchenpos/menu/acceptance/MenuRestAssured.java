package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuRestAssured {
    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, BigDecimal price, Long menuGroupId,
                                                         List<MenuProductRequest> menuProductRequests) {
        MenuRequest request = new MenuRequest(name, price, menuGroupId, menuProductRequests);

        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }
}
