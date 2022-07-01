package kitchenpos.menu.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.menugroup.acceptance.MenuGroupAcceptanceTest.메뉴_그룹_등록_되어_있음;
import static kitchenpos.product.acceptance.ProductAcceptanceTest.상품_등록_되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class MenuAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/menus";

    private MenuGroupResponse 추천_메뉴;
    private ProductResponse 피자;

    @BeforeEach
    public void setUp() {
        super.setUp();

        추천_메뉴 = 메뉴_그룹_등록_되어_있음("추천 메뉴");
        피자 = 상품_등록_되어_있음("피자", BigDecimal.valueOf(20_000));
    }

    public static MenuResponse 메뉴_등록되어_있음(String name, long price, Long menuGroupId, List<MenuProductRequest> menuProducts) {
        ExtractableResponse<Response> response = 메뉴_생성_요청(name, BigDecimal.valueOf(price), menuGroupId, menuProducts);

        return response.as(MenuResponse.class);
    }

    private static ExtractableResponse<Response> 메뉴_생성_요청(
            String name,
            BigDecimal price,
            Long menuGroupId,
            List<MenuProductRequest> menuProducts
    ) {
        MenuRequest menu = MenuRequest.of(name, price, menuGroupId, menuProducts);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(menu)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    @Test
    @DisplayName("메뉴를 관리한다 (메뉴 생성, 메뉴 목록조회)")
    void menu() {
        // given
        MenuProductRequest 메뉴_상품 = MenuProductRequest.of(피자.getId(), 1L);

        // when
        ExtractableResponse<Response> 메뉴_생성_요청_결과 = 메뉴_생성_요청(
                "상품_등록_되어_있음",
                BigDecimal.valueOf(20_000),
                추천_메뉴.getId(),
                Arrays.asList(메뉴_상품)
        );

        // then
        메뉴_생성_요청됨(메뉴_생성_요청_결과);

        // when
        ExtractableResponse<Response> 메뉴_목록_조회_요청_결과 = 메뉴_목록_조회_요청();

        // then
        메뉴_목록_조회됨(메뉴_목록_조회_요청_결과);
    }

    private void 메뉴_생성_요청됨(ExtractableResponse<Response> response) {
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
}
