package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceTestUtils.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.ProductAcceptanceTestUtils.상품_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class MenuAcceptanceTestUtils {
    private static final String MENU_PATH = "/api/menus";

    private MenuAcceptanceTestUtils() {}

    public static Menu 메뉴_면류_짜장면() {
        Product 짜장면 = 상품_등록되어_있음("짜장면", BigDecimal.valueOf(7000));
        MenuGroup 면류 = 메뉴_그룹_등록되어_있음("면류");
        MenuProduct 짜장면_1그릇 = new MenuProduct();
        짜장면_1그릇.setSeq(1L);
        짜장면_1그릇.setMenuId(면류.getId());
        짜장면_1그릇.setProductId(짜장면.getId());
        짜장면_1그릇.setQuantity(1L);
        return 메뉴_등록되어_있음(짜장면.getName(), 면류.getId(), 짜장면.getPrice(), Collections.singletonList(짜장면_1그릇));
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(MENU_PATH)
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post(MENU_PATH)
                .then().log().all()
                .extract();
    }

    public static Menu 메뉴_등록되어_있음(String name, Long menuGroupId, BigDecimal price, List<MenuProduct> menuProducts) {
        ExtractableResponse<Response> response = 메뉴_생성_요청(name, menuGroupId, price, menuProducts);
        메뉴_생성됨(response);
        return response.as(Menu.class);
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
