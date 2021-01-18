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

public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroup 추천메뉴;
    private Product 양념치킨;
    private Product 후라이드치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();

        MenuGroup 추천메뉴 = new MenuGroup("추천메뉴");
        Product product_양념치킨 = new Product("양념치킨", BigDecimal.valueOf(16000));
        Product product_후라이드치킨 = new Product("후라이드치킨", BigDecimal.valueOf(15000));

        this.추천메뉴 = MenuGroupAcceptanceTest.메뉴_그룹_등록되어_있음(추천메뉴).as(MenuGroup.class);
        this.양념치킨 = ProductAcceptanceTest.상품_등록되어_있음(product_양념치킨).as(Product.class);
        this.후라이드치킨 = ProductAcceptanceTest.상품_등록되어_있음(product_후라이드치킨).as(Product.class);
    }

    @DisplayName("메뉴를 관리한다.")
    @Test
    void manageMenu() {
        // given
        MenuProduct menuProduct_양념치킨 = new MenuProduct(양념치킨.getId(), 1);
        MenuProduct menuProduct_후라이드치킨 = new MenuProduct(후라이드치킨.getId(), 1);

        Menu 잘못된_가격 = new Menu("양념후라이드세트", BigDecimal.valueOf(32000), 추천메뉴.getId(), Arrays.asList(menuProduct_양념치킨, menuProduct_후라이드치킨));
        Menu 올바른_가격 = new Menu("양념후라이드세트", BigDecimal.valueOf(31000), 추천메뉴.getId(), Arrays.asList(menuProduct_양념치킨, menuProduct_후라이드치킨));

        // when
        ExtractableResponse<Response> wrongResponse = 메뉴_등록_요청(잘못된_가격);

        // then
        메뉴_생성_실패됨(wrongResponse);

        // when
        ExtractableResponse<Response> createResponse = 메뉴_등록_요청(올바른_가격);

        // then
        메뉴_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_응답됨(findResponse);
        메뉴_목록_포함됨(findResponse, Arrays.asList(createResponse));
    }

    public static ExtractableResponse<Response> 메뉴_등록_요청(Menu menu) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post("/api/menus")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 메뉴_등록_되어있음(String menuName, BigDecimal menuPrice, MenuGroup menuGroup, List<MenuProduct> menuProducts) {
        return 메뉴_등록_요청(new Menu("양념+후라이드", menuPrice, menuGroup.getId(), menuProducts));
    }

    public static void 메뉴_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 메뉴_생성_실패됨(ExtractableResponse<Response> wrongResponse) {
        assertThat(wrongResponse.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/menus")
                .then().log().all()
                .extract();
    }

    private void 메뉴_목록_응답됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 메뉴_목록_포함됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponse) {
        List<Long> createMenuIds = createResponse.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> findMenuIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(findMenuIds).containsAll(createMenuIds);
    }
}
