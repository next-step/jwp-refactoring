package kitchenpos.menu.acceptance;

import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_생성_요청;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("메뉴를 관리한다.")
public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 추천_메뉴;

    private ProductResponse 후라이드_치킨;

    private MenuProductRequest 메뉴_후라이드_치킨;

    @BeforeEach
    void init() {
        // given
        추천_메뉴 = 메뉴_그룹_생성_요청("추천메뉴").as(MenuGroupResponse.class);
        후라이드_치킨 = 상품_생성_요청("후라이드치킨", 17_000L).as(ProductResponse.class);

        메뉴_후라이드_치킨 = 메뉴_상품_생성(후라이드_치킨.getId(), 2);
    }

    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청("후라이드+후라이드", 19_000L, 추천_메뉴.getId(), Arrays.asList(메뉴_후라이드_치킨));

        // then
        메뉴_생성_요청됨(response);
    }
    
    @Test
    @DisplayName("메뉴 목록을 조회한다.")
    void findAll() {
        // when
        ExtractableResponse<Response> response = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회_요청됨(response);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(String name, Long price, Long menuGroupId,
        List<MenuProductRequest> menuProducts) {
        MenuRequest menuRequest = new MenuRequest(name, new BigDecimal(price), menuGroupId, menuProducts);

        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(menuRequest)
            .when().post("/api/menus")
            .then().log().all().extract();
    }

    private static ExtractableResponse<Response> 메뉴_목록_조회_요청() {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .when().get("/api/menus")
            .then().log().all().extract();
    }

    private static void 메뉴_생성_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 메뉴_목록_조회_요청됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static MenuProductRequest 메뉴_상품_생성(Long productId, long quantity) {
        return new MenuProductRequest(productId, quantity);
    }
}
