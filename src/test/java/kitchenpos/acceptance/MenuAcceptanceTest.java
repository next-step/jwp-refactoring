package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private Product product;
    private MenuGroup menuGroup;

    @BeforeEach
    public void setUp() {
        super.setUp();
        product = 상품_등록_요청("강정치킨", new BigDecimal(17000)).as(Product.class);
        menuGroup = 메뉴_그룹_등록_요청("추천메뉴").as(MenuGroup.class);
    }

    @Test
    @DisplayName("메뉴를 등록한다.")
    void createMenu() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(2);

        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청("강정치킨", new BigDecimal(17000), menuGroup.getId(), Arrays.asList(menuProduct));

        // then
        메뉴_등록됨(response);
    }

    @Test
    @DisplayName("메뉴를 등록 실패한다. (메뉴 가격 0 미만)")
    void createMenu2() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);
        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청("강정치킨", new BigDecimal(-1000), menuGroup.getId(), Arrays.asList(menuProduct));

        // then
        메뉴_등록_실패됨(response);
    }

    @Test
    @DisplayName("메뉴를 등록 실패한다. (menuGroup 누락)")
    void createMenu3() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(product.getId());
        menuProduct.setQuantity(1);
        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청("강정치킨", new BigDecimal(-1000), null, Arrays.asList(menuProduct));

        // then
        메뉴_등록_실패됨(response);
    }

    @Test
    @DisplayName("메뉴를 등록 실패한다. (상품 등록되어있지 않음)")
    void createMenu4() {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setMenuId(1L);
        menuProduct.setProductId(10L);
        menuProduct.setQuantity(1);
        // when
        ExtractableResponse<Response> response = 메뉴_등록_요청("강정치킨", new BigDecimal(-1000), null, Arrays.asList(menuProduct));

        // then
        메뉴_등록_실패됨(response);
    }

    @Test
    @DisplayName("메뉴를 조회한다.")
    void getMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_조회_요청();

        // then
        메뉴_조회됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(String name, BigDecimal price, Long menuGroupId, List<MenuProduct> menuProducts) {
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

    public static ExtractableResponse<Response> 메뉴_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all().extract();
    }

    public static void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 메뉴_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
