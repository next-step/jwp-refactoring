package kitchenpos.ui.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.stream.Stream;
import kitchenpos.product.dto.ProductRequest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품 관련 인수테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    static private final String PRODUCT_URL = "/api/products";

    @TestFactory
    Stream<DynamicTest> productTest() {
        return Stream.of(
                dynamicTest("상품을 생성 할 수 있다.", () -> {
                    ExtractableResponse<Response> 짬뽕_생성_요청 = 상품_생성_요청("짬뽕", BigDecimal.valueOf(1000));
                    상품_생성됨(짬뽕_생성_요청);
                }),
                dynamicTest("상품 목록을 조회 할 수 있다.", () -> {
                    ExtractableResponse<Response> 상품_목록_조회_요청_응답 = 상품_목록_조회_요청();
                    삼품_목록_조회됨(상품_목록_조회_요청_응답);
                }),
                dynamicTest("이름값이 존재 해야 한다.", () -> {
                    ExtractableResponse<Response> 생성_요청 = 상품_생성_요청("", BigDecimal.valueOf(1000));
                    상품_생성_실패함(생성_요청);
                }),
                dynamicTest("삼품은 0원 이상 이어야 한다.", () -> {
                    ExtractableResponse<Response> 생성_요청 = 상품_생성_요청("짬뽕", BigDecimal.valueOf(-1));
                    상품_생성_실패함(생성_요청);
                })
        );
    }

    private void 삼품_목록_조회됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_생성_실패함(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(ProductRequest.of(name, price))
                .when().post(PRODUCT_URL)
                .then().log().all().
                extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get(PRODUCT_URL)
                .then().log().all()
                .extract();
    }
}
