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
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
public class MenuAcceptanceTest extends AcceptanceTest {

    private Product 소고기한우;
    private MenuGroup 추천메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        // 상품 등록
        // 메뉴 그룹
        추천메뉴 = 메뉴그룹_등록되어있음(MenuGroup.of("추천메뉴"));
        소고기한우 = 상품_등록되어있음(Product.of("소고기한우", 30000));
    }

    @DisplayName("메뉴 관리")
    @Test
    void handleMenu() {
        // 메뉴 생성
        // 메뉴 조회
        Menu menu = Menu.of(
                "소고기+소고기",
                BigDecimal.valueOf(50000),
                추천메뉴.getId(),
                Arrays.asList(MenuProduct.of(소고기한우.getId(), 2L))
        );

        ExtractableResponse<Response> createResponse = 메뉴_생성_요청(menu);
        Menu savedMenu = 메뉴_생성_확인(createResponse);

        ExtractableResponse<Response> findResponse = 모든_메뉴_조회_요청();
        모든_메뉴_조회_확인(findResponse, savedMenu);

    }

    private void 모든_메뉴_조회_확인(ExtractableResponse<Response> findResponse, Menu expected) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Menu> menus = findResponse.jsonPath().getList(".", Menu.class);
        List<MenuProduct> menuProductList = menus.stream()
                .map(menu -> menu.getMenuProducts())
                .flatMap(menuProducts -> menuProducts.stream())
                .collect(Collectors.toList());
        assertThat(menus).contains(expected);
        assertThat(menuProductList).containsAll(expected.getMenuProducts());
    }

    private ExtractableResponse<Response> 모든_메뉴_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private Menu 메뉴_생성_확인(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = createResponse.header("Location");
        assertThat(location).isEqualTo("/api/menus/" + createResponse.as(Menu.class).getId());
        return createResponse.as(Menu.class);
    }


    public static MenuGroup 메뉴그룹_등록되어있음(MenuGroup menuGroup) {
        return RestAssured
                .given().log().all()
                .body(menuGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menu-groups")
                .then().log().all()
                .extract()
                .as(MenuGroup.class);
    }

    public static Product 상품_등록되어있음(Product product) {
        return RestAssured
                .given().log().all()
                .body(product)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract()
                .as(Product.class);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .body(menu)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static Menu 메뉴_등록되어있음(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = Menu.of(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);
        return 메뉴_생성_요청(menu).as(Menu.class);
    }
}
