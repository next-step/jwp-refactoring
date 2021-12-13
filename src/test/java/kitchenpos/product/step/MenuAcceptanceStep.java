package kitchenpos.product.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.product.ui.request.MenuProductRequest;
import kitchenpos.product.ui.request.MenuRequest;
import kitchenpos.product.ui.response.MenuGroupResponse;
import kitchenpos.product.ui.response.MenuResponse;
import kitchenpos.product.ui.response.ProductResponse;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceStep {

    public static MenuResponse 메뉴_등록_되어_있음(String name, BigDecimal price,
        long menuGroupId, long productId, long quantity) {
        return 메뉴_등록_요청(name, price, menuGroupId, productId, quantity).as(MenuResponse.class);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, BigDecimal price,
        long menuGroupId, long productId, long quantity) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new MenuRequest(name, price, menuGroupId,
                Collections.singletonList(new MenuProductRequest(productId, quantity))))
            .when()
            .post("/api/menus")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_등록_됨(ExtractableResponse<Response> response, String name,
        BigDecimal price, int quantity, MenuGroupResponse menuGroup, ProductResponse product) {
        MenuResponse menu = response.as(MenuResponse.class);
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(menu.getName()).isEqualTo(name),
            () -> assertThat(menu.getPrice()).isEqualByComparingTo(price),
            () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
            () -> assertThat(menu.getMenuProducts()).first()
                .satisfies(menuProduct -> {
                    assertThat(menuProduct.getProductId()).isEqualTo(product.getId());
                    assertThat(menuProduct.getQuantity()).isEqualTo(quantity);
                })
        );
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured.given().log().all()
            .when()
            .get("/api/menus")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_목록_조회_됨(ExtractableResponse<Response> response, MenuResponse menu) {
        List<MenuResponse> menus = response.as(new TypeRef<List<MenuResponse>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(menus)
                .first()
                .extracting(MenuResponse::getId)
                .isEqualTo(menu.getId())
        );
    }
}
