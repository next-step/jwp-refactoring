package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴 관련 기능 인수테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    private MenuGroup 추천메뉴;
    private Product 허니콤보;
    private Product 레드콤보;

    @BeforeEach
    public void setUp() {
        super.setUp();
        추천메뉴 = 메뉴_그룹_등록_요청("추천메뉴").as(MenuGroup.class);
        허니콤보 = 상품_등록_요청("허니콤보", 20_000L).as(Product.class);
        레드콤보 = 상품_등록_요청("레드콤보", 19_000L).as(Product.class);
    }
    /**
     * Feature 메뉴 관련 기능
     *
     * Background
     * Given 메뉴 그룹 등록되어있음
     * And 상품 등록되어있음
     *
     * Scenario 메뉴 관련 기능
     * Given 0원 미만 가격
     * When 메뉴 등록 요청
     * Then 메뉴 등록 실패됨
     *
     * Given 상품 가격 총합보다 높은 가격
     * When 메뉴 등록 요청
     * Then 메뉴 등록 실패됨
     *
     * When 메뉴 등록 요청
     * Then 메뉴 등록됨
     * When 메뉴 목록 조회 요청
     * Then 메뉴 목록 조회됨
     */
    @Test
    @DisplayName("메뉴 관련 기능")
    void integrationTest() {
        //when
        ExtractableResponse<Response> 가격_0원_미만_메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "레드허니콤보", -1L, 허니콤보, 레드콤보);
        //then
        메뉴_등록_실패됨(가격_0원_미만_메뉴_등록_응답_결과);

        //when
        ExtractableResponse<Response> 상품_가격_총합_초과_메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "허니레드콤보", 40_000L, 허니콤보, 레드콤보);
        //then
        메뉴_등록_실패됨(상품_가격_총합_초과_메뉴_등록_응답_결과);

        //when
        ExtractableResponse<Response> 메뉴_등록_응답_결과 = 메뉴_등록_요청(추천메뉴, "허니레드콤보", 39_000L, 허니콤보, 레드콤보);
        //then
        메뉴_등록됨(메뉴_등록_응답_결과);

        //when
        ExtractableResponse<Response> 메뉴_목록_조회_응답_결과 = 메뉴_목록_조회_요청();
        //then
        메뉴_목록_조회됨(메뉴_목록_조회_응답_결과, 허니콤보, 레드콤보);
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(MenuGroup menuGroup, String name, long price, Product... products) {
        List<MenuProduct> menuProducts = 메뉴_상품_생성(products);
        Menu request = 메뉴_생성(menuGroup, name, price, menuProducts);
        return sendPost("/api/menus", request);
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response, Product... products) {
        List<Long> actualIds = 메뉴_상품_아이디_추출(response);
        List<Long> expectedIds = Arrays.stream(products)
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(actualIds).containsExactlyElementsOf(expectedIds);
    }

    private List<Long> 메뉴_상품_아이디_추출(ExtractableResponse<Response> response) {
        List<MenuProduct> menuProducts = response.jsonPath().getList(".", Menu.class).stream()
                .map(Menu::getMenuProducts)
                .flatMap(Collection::stream)
                .collect(Collectors.toList());
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return sendGet("/api/menus");
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static Menu 메뉴_생성(MenuGroup menuGroup, String name, long price, List<MenuProduct> menuProducts) {
        Menu request = new Menu();
        request.setMenuGroupId(menuGroup.getId());
        request.setName(name);
        request.setPrice(BigDecimal.valueOf(price));
        request.setMenuProducts(menuProducts);
        return request;
    }

    private static List<MenuProduct> 메뉴_상품_생성(Product[] products) {
        return Arrays.stream(products)
                .map(product -> {
                    MenuProduct menuProduct = new MenuProduct();
                    menuProduct.setProductId(product.getId());
                    menuProduct.setQuantity(1L);
                    return menuProduct;
                })
                .collect(Collectors.toList());
    }
}
