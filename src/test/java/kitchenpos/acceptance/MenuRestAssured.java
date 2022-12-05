package kitchenpos.acceptance;

import static kitchenpos.domain.MenuTestFixture.generateMenu;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.springframework.http.MediaType;

public class MenuRestAssured {

    public static ExtractableResponse<Response> 메뉴_등록되어_있음(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        return 메뉴_생성_요청(id, name, price, menuGroupId, menuProducts);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Long id, String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menus = generateMenu(id, name, price, menuGroupId, menuProducts);

        return RestAssured
                .given().log().all()
                .body(menus)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }
}
