package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_등록_되어있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_되어있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTest extends AcceptanceTest {
    private static final String URL = "/api/menus";

    @Test
    @DisplayName("메뉴를 관리한다.")
    void manageMenu() {
        // 상품 등록 되어 있음
        Product 후라이드치킨 = 상품_등록_되어있음("후라이드치킨", BigDecimal.valueOf(10000));
        Product 양념치킨 = 상품_등록_되어있음("양념치킨", BigDecimal.valueOf(11000));

        // 메뉴그룹 등록 되어 있음
        MenuGroup 치킨 = 메뉴그룹_등록_되어있음("치킨");

        // 메뉴 등록 요청
        List<MenuProduct> menuProducts = getMenuProducts(Arrays.asList(후라이드치킨, 양념치킨), Arrays.asList(1L, 1L));
        ExtractableResponse<Response> saveResponse = 메뉴_등록_요청("두마리세트", BigDecimal.valueOf(20000), 치킨, menuProducts);
        // 메뉴 등록됨
        메뉴_등록_됨(saveResponse);

        // 메뉴 목록 조회 요청
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();
        // 메뉴 목록 조회됨
        메뉴_목록_조회됨(response, Arrays.asList(saveResponse.as(Menu.class)));

    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, BigDecimal price, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(price);
        menu.setMenuProducts(menuProducts);
        menu.setMenuGroupId(menuGroup.getId());

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menu)
            .when().post(URL)
            .then().log().all()
            .extract();
    }

    public static void 메뉴_등록_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(URL)
            .then().log().all()
            .extract();
    }

    public static void 메뉴_목록_조회됨(ExtractableResponse<Response> response, List<Menu> expected) {
        List<Menu> list = response.jsonPath().getList(".", Menu.class);
        List<String> expectedNames = expected.stream()
            .map(Menu::getName)
            .collect(Collectors.toList());

        assertAll(() -> {
            assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
            assertThat(list).extracting(Menu::getName).containsAll(expectedNames);
        });
    }

    public static Menu 메뉴등록되어있음(String name, BigDecimal price, MenuGroup menuGroup, List<Product> products) {
        List<MenuProduct> menuProducts = getMenuProducts(products, Arrays.asList(1L, 1L));
        return 메뉴_등록_요청(name, price, menuGroup, menuProducts).as(Menu.class);
    }

    private static List<MenuProduct> getMenuProducts(List<Product> products, List<Long> productsQuantity) {
        List<MenuProduct> menuProducts = new ArrayList<>();
        for (int i = 0; i < products.size(); i++) {
            MenuProduct menuProduct = new MenuProduct();
            menuProduct.setSeq(Long.valueOf(i));
            menuProduct.setProductId(products.get(i).getId());
            menuProduct.setQuantity(productsQuantity.get(i));
            menuProducts.add(menuProduct);
        }

        return menuProducts;
    }
}
