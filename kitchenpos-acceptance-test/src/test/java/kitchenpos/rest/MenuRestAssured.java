package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.MenuCreateRequest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

public class MenuRestAssured {

    public static MenuResponse 메뉴_등록됨(String menuName, BigDecimal price, Long menuGroupId, List<MenuProductRequest> menuProductRequests) {
        return 메뉴_등록_요청(new MenuCreateRequest(menuName, price, menuGroupId, menuProductRequests))
                .as(MenuResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuCreateRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post(UriResource.메뉴_API.uri())
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(UriResource.메뉴_API.uri())
                .then().log().all()
                .extract();
    }
}
