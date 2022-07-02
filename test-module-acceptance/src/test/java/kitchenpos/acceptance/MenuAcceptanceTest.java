package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴 관련 인수테스트")
class MenuAcceptanceTest extends AcceptanceTest {

    private static final String MENU_PATH = "/api/menus";
    private static final int PRODUCT_QUANTITY_DEFAULT = 1;

    private ProductResponse 뿌링클;
    private ProductResponse 투움바;
    private MenuGroupResponse 인기메뉴;

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
     *     Given  메뉴 상품이 N개인 메뉴를 생성하고
     *     When   메뉴 등록 요청하면
     *     Then   메뉴가 등록된다.
     *     Given  메뉴 가격이 금액(가격 * 수량)보다 큰 메뉴를 생성하고
     *     When   메뉴 등록 요청하면
     *     Then   메뉴 등록 실패된다.
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
                    Map<String, Object> params1 = 요청할_메뉴_생성("뿌링클", 27000 ,Arrays.asList(뿌링클), 인기메뉴);
                    Map<String, Object> params2 = 요청할_메뉴_생성("투움바", 30000 ,Arrays.asList(투움바), 인기메뉴);

                    //when
                    ExtractableResponse<Response> response1 = 메뉴_등록_요청(params1);
                    ExtractableResponse<Response> response2 = 메뉴_등록_요청(params2);

                    //then
                    메뉴_등록됨(response1);
                    메뉴_등록됨(response2);

                    //given
                    Map<String, Object> params3 = 요청할_메뉴_생성("인기치킨 두마리 세트", 50000, Arrays.asList(뿌링클, 투움바), 인기메뉴);

                    //when
                    ExtractableResponse<Response> response3 = 메뉴_등록_요청(params3);

                    //then
                    메뉴_등록됨(response3);

                    //given
                    Map<String, Object> params4 = 요청할_메뉴_생성("합친게 더 비싼 세트", 60000, Arrays.asList(뿌링클, 투움바), 인기메뉴);

                    //when
                    ExtractableResponse<Response> response4 = 메뉴_등록_요청(params4);

                    //then
                    메뉴_등록_실패됨(response4);

                }),

                dynamicTest("메뉴 그룹 목록 조회한다.", () -> {
                    //when
                    ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

                    //then
                    메뉴_목록_조회됨(response, Arrays.asList("뿌링클", "투움바", "인기치킨 두마리 세트"), 27000, 30000, 50000);
                })
        );

    }


    public static MenuResponse 메뉴_등록_되어있음(String menuName, int menuPrice, List<ProductResponse> products, MenuGroupResponse menuGroup) {
        Map<String, Object> params = 요청할_메뉴_생성(menuName, menuPrice, products, menuGroup);
        return 메뉴_등록_요청(params).as(MenuResponse.class);
    }

    private static Map<String, Object> 요청할_메뉴_생성(String name, int price, List<ProductResponse> products, MenuGroupResponse menuGroup) {
        Map<String, Object> params = new HashMap<>();
        params.put("price", price);
        params.put("menuGroupId", menuGroup.getId());
        params.put("menuProducts", 요청할_메뉴_상품_리스트_생성(products));
        params.put("name", name);
        return params;
    }

    private static List<Map<String, Object>> 요청할_메뉴_상품_리스트_생성(List<ProductResponse> products) {
        List<Map<String, Object>> menuProducts = new ArrayList<>();

        for (ProductResponse product : products) {
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

    private void 메뉴_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response, List<String> menuNames, int ...menuPrices) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.body().jsonPath().getList("name", String.class))
                .containsExactlyInAnyOrderElementsOf(menuNames);
        assertThat(response.body().jsonPath().getList("price", BigDecimal.class)
                .stream().mapToInt(BigDecimal::intValue).toArray())
                .containsExactly(menuPrices);

    }

}
