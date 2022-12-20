package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 관련 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {

    private Product 삼겹살;
    private Product 김치;
    private MenuGroup 한식;
    private Menu 삼겹살세트메뉴;
    private MenuProduct 삼겹살메뉴싱품;
    private MenuProduct 김치상품;

    @BeforeEach
    public void setUp() {
        super.setUp();
        삼겹살 = 상품_생성_요청(new Product(1L, "삼겹살", BigDecimal.valueOf(5_000))).as(Product.class);
        김치 = 상품_생성_요청(new Product(2L, "김치", BigDecimal.valueOf(3_000))).as(Product.class);
        한식 = 메뉴_그룹_생성_요청(new MenuGroup(1L, "한식")).as(MenuGroup.class);
        삼겹살세트메뉴 = new Menu(1L, "삼겹살세트메뉴", BigDecimal.valueOf(8_000), 한식);
        삼겹살메뉴싱품 = new MenuProduct(1L, 삼겹살세트메뉴, 삼겹살, 1L);
        김치상품 = new MenuProduct(2L, 삼겹살세트메뉴, 김치, 1L);
        삼겹살세트메뉴.getMenuProducts().setMenuProducts(Arrays.asList(삼겹살메뉴싱품, 김치상품));
    }

    @DisplayName("메뉴를 생성한다.")
    @Test
    void createMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(삼겹살세트메뉴);

        // then
        메뉴_생성됨(response);
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void findAllMenu() {
        // given
        삼겹살세트메뉴 = 메뉴_생성_요청(삼겹살세트메뉴).as(Menu.class);

        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(response);
        메뉴_목록_확인됨(response, Arrays.asList(삼겹살세트메뉴.getId()));
    }

    private void 메뉴_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 메뉴_목록_확인됨(ExtractableResponse<Response> response, List<Long> menuIds) {
        List<Long> resultIds = response.jsonPath().getList(".", Menu.class)
                .stream()
                .map(Menu::getId)
                .collect(Collectors.toList());

        assertThat(resultIds).containsAll(menuIds);
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

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

}
