package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.TestApiClient;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

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
        추천메뉴 = 메뉴그룹_등록되어있음(MenuGroup.of("추천메뉴"), "/api/menu-groups");
        소고기한우 = 상품_등록되어있음(Product.of("소고기한우", 30000), "/api/products");
    }

    @DisplayName("메뉴 관리")
    @Test
    void handleMenu() {
        Menu menu = Menu.of(
                "소고기+소고기",
                50000,
                추천메뉴.getId(),
                Arrays.asList(MenuProduct.of(소고기한우.getId(), 2L))
        );

        ExtractableResponse<Response> createResponse = 메뉴_생성_요청(menu, "/api/menus");
        Menu savedMenu = 메뉴_생성_확인(createResponse);

        ExtractableResponse<Response> findResponse = 모든_메뉴_조회_요청("/api/menus");
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

    private ExtractableResponse<Response> 모든_메뉴_조회_요청(String path) {
        return TestApiClient.get(path);
    }

    private Menu 메뉴_생성_확인(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        String location = createResponse.header("Location");
        assertThat(location).isEqualTo("/api/menus/" + createResponse.as(Menu.class).getId());
        return createResponse.as(Menu.class);
    }

    public static MenuGroup 메뉴그룹_등록되어있음(MenuGroup menuGroup, String path) {
        return TestApiClient.create(menuGroup, path).as(MenuGroup.class);
    }

    public static Product 상품_등록되어있음(Product product, String path) {
        return TestApiClient.create(product, path).as(Product.class);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(Menu menu, String path) {
        return TestApiClient.create(menu, path);
    }

    public static Menu 메뉴_등록되어있음(String name, int price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = Menu.of(name, price, menuGroupId, menuProducts);
        return 메뉴_생성_요청(menu, "/api/menus").as(Menu.class);
    }
}
