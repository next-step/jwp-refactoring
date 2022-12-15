package kitchenpos.rest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import kitchenpos.resource.UriResource;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

public class MenuRestAssured {

    public static ExtractableResponse<Response> 메뉴_등록됨(Product product, MenuGroup menuGroup, Menu menu, long quantity) {
        return 메뉴_등록_요청(menu.getName(),
                menu.getPrice(),
                menuGroup.getId(),
                Arrays.asList(new MenuProduct(null, product.getId(), quantity)));
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name,
                                                         BigDecimal price,
                                                         Long menuGroupId,
                                                         List<MenuProduct> menuProducts) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new Menu(name, price, menuGroupId, menuProducts))
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
