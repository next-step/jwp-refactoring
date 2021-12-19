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

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 기능")
class MenuAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/menus";

    private MenuGroup 추천_메뉴_그룹;
    private Product 강정치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음("추천_메뉴_그룹");
        강정치킨 = 상품_등록되어_있음("강정치킨", 17_000);
    }

    @DisplayName("메뉴를 관리한다.")
    @Test
    void manageMenu() {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(강정치킨.getId());
        menuProduct.setQuantity(2);

        Menu menu = new Menu();
        menu.setName("강정치킨_두마리_셋트");
        menu.setPrice(BigDecimal.valueOf(30_000));
        menu.setMenuGroupId(추천_메뉴_그룹.getId());
        menu.setMenuProducts(Arrays.asList(menuProduct));

        // when
        ExtractableResponse<Response> 메뉴_생성_응답 = 메뉴_생성_요청(menu);
        // then
        메뉴_생성됨(메뉴_생성_응답);

        // when
        ExtractableResponse<Response> 메뉴_목록_조회_응답 = 메뉴_목록_조회_요청();
        // then
        메뉴_목록_조회됨(메뉴_목록_조회_응답);
    }

    private static ExtractableResponse<Response> 메뉴_생성_요청(Menu params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get(API_URL)
                .then().log().all()
                .extract();
    }

    private void 메뉴_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static Menu 메뉴_등록되어_있음(String name, long price, Long menuGroupId, List<MenuProduct> menuProducts) {
        Menu menu = new Menu();
        menu.setName(name);
        menu.setPrice(BigDecimal.valueOf(price));
        menu.setMenuGroupId(menuGroupId);
        menu.setMenuProducts(menuProducts);

        ExtractableResponse<Response> response = 메뉴_생성_요청(menu);
        return response.as(Menu.class);
    }
}
