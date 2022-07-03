package kitchenpos.menu.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.common.acceptance.BaseAcceptanceTest;
import kitchenpos.fixture.acceptance.AcceptanceTestMenuGroupFixture;
import kitchenpos.fixture.acceptance.AcceptanceTestProductFixture;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTest extends BaseAcceptanceTest {
    private AcceptanceTestMenuGroupFixture 메뉴_그룹;
    private AcceptanceTestProductFixture 상품;

    @BeforeEach
    public void setUp() {
        super.setUp();
        메뉴_그룹 = new AcceptanceTestMenuGroupFixture();
        상품 = new AcceptanceTestProductFixture();
    }

    @DisplayName("메뉴를 관리한다")
    @Test
    void manageMenu() {
        // given
        final String newMenuName1 = "돼지모듬";
        final Map<Product, Long> 상품_수량1 = new HashMap<>();
        상품_수량1.put(상품.삼겹살, 2L);
        상품_수량1.put(상품.목살, 1L);

        final String newMenuName2 = "김치찌개정식";
        final Map<Product, Long> 상품_수량2 = new HashMap<>();
        상품_수량2.put(상품.김치찌개, 1L);
        상품_수량2.put(상품.공깃밥, 1L);

        // when
        ExtractableResponse<Response> created1 = 메뉴_생성_요청(
                newMenuName1, BigDecimal.valueOf(43000L), 메뉴_그룹.구이류, 상품_수량1);
        // then
        메뉴_생성됨(created1);

        // when
        ExtractableResponse<Response> created2 = 메뉴_생성_요청(
                newMenuName2, BigDecimal.valueOf(9000L), 메뉴_그룹.식사류, 상품_수량2);
        // then
        메뉴_생성됨(created2);

        // when
        ExtractableResponse<Response> list = 메뉴_목록_조회_요청();
        // then
        메뉴_목록_조회됨(list);
        // then
        메뉴_목록_포함됨(list, Arrays.asList(newMenuName1, newMenuName2));
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(final String name,
                                                         final BigDecimal price,
                                                         final MenuGroup menuGroup,
                                                         final Map<Product, Long> productQuantity) {
        final Map<String, Object> body = new HashMap<>();
        body.put("name", name);
        body.put("price", price);
        body.put("menuGroupId", menuGroup.getId());
        body.put("menuProducts", convertToMenuProductsParam(productQuantity));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    private static List<Map<String, Object>> convertToMenuProductsParam(final Map<Product, Long> productQuantity) {
        return productQuantity.entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> menuProduct = new HashMap<>();
                    menuProduct.put("productId", entry.getKey().getId());
                    menuProduct.put("quantity", entry.getValue());
                    return menuProduct;
                })
                .collect(Collectors.toList());
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    public static void 메뉴_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_목록_조회됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 메뉴_목록_포함됨(final ExtractableResponse<Response> response, final List<String> menuNames) {
        assertThat(response.body().asString()).contains(menuNames);
    }
}
