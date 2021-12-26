package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        Product product = ProductAcceptanceTest.상품_등록_요청("짜장면", new BigDecimal(5000)).as(Product.class);
        MenuGroup menuGroup = MenuGroupAcceptanceTest.메뉴_그룹_등록_요청("중국음식").as(MenuGroup.class);
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);

        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청("짜장면", product.getPrice(), menuGroup.getId(), Arrays.asList(menuProduct));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @Test
    @DisplayName("메뉴를 조회한다.")
    void getMenu() {
        // when
        Map<String, String> params = new HashMap<>();
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all().extract();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.jsonPath().getList(".")).hasSize(6);
    }

    private ExtractableResponse<Response> 메뉴_등록_요청(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);
        menu.setName(name);
        menu.setPrice(price);

        return RestAssured
                .given().log().all()
                .body(menu)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all().extract();
    }
}
