package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import kitchenpos.dto.menu.MenuProductRequest;
import kitchenpos.dto.menu.MenuRequest;
import kitchenpos.dto.menu.MenuResponse;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class MenuAcceptanceTest extends AcceptanceTest {

    private MenuGroupResponse 메뉴그룹;
    private ProductResponse 제품1;
    private ProductResponse 제품2;

    @BeforeEach
    void setUpData() {
        MenuGroupRequest 메뉴그룹_요청 = new MenuGroupRequest("메뉴그룹");
        ExtractableResponse<Response> 메뉴그룹_요청_결과 = MenuGroupAcceptanceTest.메뉴_그룹_생성_요청(메뉴그룹_요청);
        메뉴그룹 = MenuGroupAcceptanceTest.메뉴_그룹_생성_성공(메뉴그룹_요청_결과);

        ProductRequest 제품_요청_1 = new ProductRequest("제품1", BigDecimal.valueOf(1000L));
        제품1 = ProductAcceptanceTest.제품_생성_성공(ProductAcceptanceTest.제품_생성_요청(제품_요청_1));

        ProductRequest 제품_요청_2 = new ProductRequest("제품2", BigDecimal.valueOf(2000L));
        제품2 = ProductAcceptanceTest.제품_생성_성공(ProductAcceptanceTest.제품_생성_요청(제품_요청_2));
    }


    @DisplayName("메뉴 생성 통합 테스트")
    @Test
    void createTest() {
        MenuRequest menuRequest = MenuRequest.Builder.of("맛좋은테스트메뉴", BigDecimal.valueOf(2000L))
                                                     .menuGroupId(메뉴그룹.getId())
                                                     .menuProducts(Arrays.asList(
                                                         new MenuProductRequest(제품1.getId(), 2),
                                                         new MenuProductRequest(제품2.getId(), 4)))
                                                     .build();

        // when
        ExtractableResponse<Response> response = 메뉴_생성_요청(menuRequest);

        // then
        MenuResponse actual = 메뉴_생성_성공(response);
        assertAll(() -> {
            assertThat(actual).isNotNull();
            assertThat(actual.getName()).isEqualTo("맛좋은테스트메뉴");
            assertThat(actual.getPrice()).isEqualTo(BigDecimal.valueOf(2000L));
            assertThat(actual.getMenuGroupId()).isEqualTo(메뉴그룹.getId());
            assertThat(actual.getMenuProducts()).hasSize(2);
        });
    }

    @DisplayName("전체 메뉴 조회 통합 테스트")
    @Test
    void listTest() {
        // when
        ExtractableResponse<Response> response = 전체_메뉴_조회_요청();

        // then
        전체_메뉴_조회_성공(response);
    }

    public static ExtractableResponse<Response> 메뉴_생성_요청(final MenuRequest menuRequest) {
        // when
        return RestAssured.given().log().all()
                   .body(menuRequest)
                   .contentType(MediaType.APPLICATION_JSON_VALUE)
                   .when().post("/api/menus")
                   .then().log().all().extract();
    }

    public static MenuResponse 메뉴_생성_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return response.as(MenuResponse.class);
    }
    
    public static ExtractableResponse<Response> 전체_메뉴_조회_요청() {
        return RestAssured.given().log().all()
                .when().get("/api/menus")
                .then().log().all().extract();
    }

    public static void 전체_메뉴_조회_성공(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }


}
