package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTestUtils.메뉴_그룹_등록되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTestUtils.상품_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTestUtils {
    private static final String MENU_PATH = "/api/menus";

    private MenuAcceptanceTestUtils() {}

    public static MenuResponse 메뉴_면류_짜장면() {
        ProductResponse 짜장면 = 상품_등록되어_있음("짜장면", BigDecimal.valueOf(7000));
        MenuGroupResponse 면류 = 메뉴_그룹_등록되어_있음("면류");
        MenuProduct 짜장면_1그릇 = MenuProduct.of(Product.of("짜장면", BigDecimal.valueOf(7000)), 1L);
        return 메뉴_등록되어_있음(짜장면.getName(), 면류.getId(), 짜장면.getPrice(), Collections.singletonList(new MenuProductRequest(짜장면.getId(), 1L)));
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(MENU_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, Long menuGroupId, BigDecimal price, List<MenuProductRequest> menuProducts) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(new MenuRequest(name, price, menuGroupId, menuProducts))
                .when().post(MENU_PATH)
                .then().log().all()
                .extract();
    }

    public static MenuResponse 메뉴_등록되어_있음(String name, Long menuGroupId, BigDecimal price, List<MenuProductRequest> menuProducts) {
        ExtractableResponse<Response> response = 메뉴_생성_요청(name, menuGroupId, price, menuProducts);
        메뉴_생성됨(response);
        return response.as(MenuResponse.class);
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_생성_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(ExtractableResponse<Response> response, String... productNames) {
        List<String> actual = response.jsonPath()
                .getList("name", String.class);
        assertThat(actual).containsExactly(productNames);
    }
}
