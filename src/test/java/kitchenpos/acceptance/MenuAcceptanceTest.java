package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관련 인수테스트")
class MenuAcceptanceTest extends AcceptanceTest {

    private static final String MENU_PATH = "/api/menus";
    private static final int PRODUCT_QUANTITY_DEFAULT = 100;

    private Product 뿌링클;
    private Product 투움바;
    private MenuGroup 인기메뉴;

    @BeforeEach
    public void setUp(){
        super.setUp();
        //given
        뿌링클 = ProductAcceptanceTest.상품_등록_되어있음("뿌링클", 27000);
        투움바 = ProductAcceptanceTest.상품_등록_되어있음("투움바", 30000);

        인기메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록_되어있음("인기 메뉴");
    }

    /**
     * Feature: 메뉴 기능
     *
     *   Background
     *     Given 상품이 등록되어있다.
     *     And   메뉴 그룹이 등록되어있다.
     *
     *   Scenario: 메뉴 기능을 관리
     *     Given  요청할 메뉴를 생성하고
     *     When   메뉴 등록 요청하면
     *     Then   메뉴가 등록된다.
     *
     *     When   메뉴 목록을 조회하면
     *     Then   메뉴 목록이 조회된다.
     * */
    @DisplayName("메뉴 기능을 관리한다.")
    @TestFactory
    Stream<DynamicTest> manageMenu() {
        return Stream.of(
                dynamicTest("메뉴를 등록 한다.", () -> {
                    //given
                    Map<String, Object> params1 = 요청할_메뉴_생성(뿌링클, 인기메뉴);
                    Map<String, Object> params2 = 요청할_메뉴_생성(투움바, 인기메뉴);

                    //when
                    ExtractableResponse<Response> response1 = 메뉴_등록_요청(params1);
                    ExtractableResponse<Response> response2 = 메뉴_등록_요청(params2);

                    //then
                    메뉴_등록됨(response1);
                    메뉴_등록됨(response2);
                }),

                dynamicTest("메뉴 그룹 목록 조회한다.", () -> {
                    //when
                    ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

                    //then
                    메뉴_목록_조회됨(response, Arrays.asList(뿌링클, 투움바));
                })
        );

    }


    public static Menu 메뉴_등록_되어있음(Product product, MenuGroup menuGroup) {
        Map<String, Object> params = 요청할_메뉴_생성(product, menuGroup);
        return 메뉴_등록_요청(params).as(Menu.class);
    }


    private static Map<String, Object> 요청할_메뉴_생성(Product product, MenuGroup menuGroup) {
        Map<String, Object> params = new HashMap<>();
        params.put("price", product.getPrice());
        params.put("menuGroupId", menuGroup.getId());
        params.put("menuProducts", 요청할_메뉴_상품_리스트_생성(Collections.singletonList(product)));
        params.put("name", product.getName());
        return params;
    }

    private static List<Map<String, Object>> 요청할_메뉴_상품_리스트_생성(List<Product> products) {
        List<Map<String, Object>> menuProducts = new ArrayList<>();

        for (Product product : products) {
            Map<String, Object> menuProduct = new HashMap<>();
            menuProduct.put("productId", product.getId());
            menuProduct.put("quantity", PRODUCT_QUANTITY_DEFAULT);
            menuProducts.add(menuProduct);
        }
        return menuProducts;
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured.given().log().all()
                .when().get(MENU_PATH)
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 메뉴_등록_요청(Map<String, Object> params) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(MENU_PATH)
                .then().log().all()
                .extract();
    }

    private void 메뉴_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response, List<Product> products) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("name", String.class))
                .containsExactlyInAnyOrderElementsOf(
                        products.stream().map(Product::getName).collect(Collectors.toList()));
        assertThat(response.body().jsonPath().getList("price", BigDecimal.class))
                .containsExactlyInAnyOrderElementsOf(
                        products.stream().map(Product::getPrice).collect(Collectors.toList()));
    }

}
