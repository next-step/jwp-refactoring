package kitchenpos.acceptance.step;

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
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.springframework.http.HttpStatus;

public class MenuAcceptanceStep {

    public static Menu 메뉴_등록_되어_있음(String name, BigDecimal price,
        long menuGroupId, long productId, int quantity) {
        return 메뉴_등록_요청(name, price, menuGroupId, productId, quantity).as(Menu.class);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, BigDecimal price,
        long menuGroupId,
        long productId, int quantity) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(menuCreateRequest(name, price, menuGroupId,
                Collections.singletonList(menuProductCreateRequest(productId, quantity))))
            .when()
            .post("/api/menus")
            .then().log().all()
            .extract();
    }

    public static void 메뉴_등록_됨(ExtractableResponse<Response> response, String name,
        BigDecimal price, int quantity, MenuGroup menuGroup, Product product) {
        Menu menu = response.as(Menu.class);
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(menu.getId()).isNotNull(),
            () -> assertThat(menu.getName()).isEqualTo(name),
            () -> assertThat(menu.getPrice()).isEqualByComparingTo(price),
            () -> assertThat(menu.getMenuGroupId()).isEqualTo(menuGroup.getId()),
            () -> assertThat(menu.getMenuProducts()).first()
                .satisfies(menuProduct -> {
                    assertThat(menuProduct.getSeq()).isNotNull();
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

    public static void 메뉴_목록_조회_됨(ExtractableResponse<Response> response, Menu menu) {
        List<Menu> menus = response.as(new TypeRef<List<Menu>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(menus)
                .first()
                .extracting(Menu::getId)
                .isEqualTo(menu.getId())
        );
    }

    private static Menu menuCreateRequest(String name, BigDecimal price, long menuGroupId,
        List<MenuProduct> menuProductRequests) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuProducts(menuProductRequests);
        menu.setMenuGroupId(menuGroupId);
        return menu;
    }

    private static MenuProduct menuProductCreateRequest(long productId, int quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(productId);
        menuProduct.setQuantity(quantity);
        return menuProduct;
    }
}
