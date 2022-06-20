package kitchenpos.acceptance;

import static kitchenpos.acceptance.MenuGroupAcceptanceSupport.메뉴_그룹_등록요청;
import static kitchenpos.acceptance.ProductAcceptanceSupport.상품_등록요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("메뉴에 관한 인수 테스트")
public class MenuAcceptanceTest extends AcceptanceTest {
    private Product 후라이드_치킨;
    private Product 감자튀김;
    private MenuProduct 메뉴_상품_후라이드_치킨;
    private MenuProduct 메뉴_상품_감자튀김;
    private MenuGroup 치킨_메뉴_그룹;
    private Menu 치킨_메뉴;

    @BeforeEach
    public void setUp() {
        super.setUp();

        후라이드_치킨 = 상품_등록요청(Product.of(null, "후라이드 치킨", BigDecimal.valueOf(15000L))).as(Product.class);
        감자튀김 = 상품_등록요청(Product.of(null, "감자튀김", BigDecimal.valueOf(5000L))).as(Product.class);
        메뉴_상품_후라이드_치킨 = MenuProduct.of(null, null, 후라이드_치킨.getId(), 1);
        메뉴_상품_감자튀김 = MenuProduct.of(null, null, 감자튀김.getId(), 1);
        치킨_메뉴_그룹 = 메뉴_그룹_등록요청(MenuGroup.of(null, "치킨_메뉴")).as(MenuGroup.class);
        치킨_메뉴 = Menu.of(null, "후라이드치킨 세트", BigDecimal.valueOf(18000L), 치킨_메뉴_그룹.getId(),
            Arrays.asList(메뉴_상품_후라이드_치킨, 메뉴_상품_감자튀김));
    }

    @DisplayName("메뉴를 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 메뉴등록을_요청(치킨_메뉴);

        // then
        메뉴_정상_등록됨(response);
    }

    @DisplayName("모든 메뉴를 조회한다")
    @Test
    void find_test() {
        // given
        메뉴등록을_요청(치킨_메뉴);

        // when
        ExtractableResponse<Response> getResponse = 모든메뉴_조회요청();

        // then
        메뉴목록_정상_조회됨(getResponse);
    }


    private ExtractableResponse<Response> 메뉴등록을_요청(Menu menu) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(menu)
            .when().post("/api/menus")
            .then().log().all().
            extract();
    }

    private void 메뉴_정상_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 모든메뉴_조회요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get("/api/menus")
            .then().log().all().
            extract();
    }

    private void 메뉴목록_정상_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Menu> result = response.jsonPath().getList(".", Menu.class);
        assertThat(result).isNotNull();
    }
}
