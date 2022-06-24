package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품 관련 인수테스트")
public class ProductAcceptanceTest extends AcceptanceTest{

    static private final String PRODUCT_URL = "/api/products";
    @BeforeEach
    void before() {
        super.setUp();
    }

    @Test
    @DisplayName("상품을 생성 할 수 있다.")
    void createTest() {
        ExtractableResponse<Response> 짬뽕_생성_요청 = 상품_생성_요청("짬뽕", BigDecimal.valueOf(1000));
        상품_생성됨(짬뽕_생성_요청);
    }

    @Test
    @DisplayName("상품 목록을 조회 할 수 있다.")
    void listTest() {
        ExtractableResponse<Response> 짬뽕_생성_요청 = 상품_생성_요청("짬뽕", BigDecimal.valueOf(1000));
        상품_생성됨(짬뽕_생성_요청);

        ExtractableResponse<Response> 상품_목록_조회_요청_응답 = 상품_목록_조회_요청();
        삼품_목록_조회됨(상품_목록_조회_요청_응답);
    }

    private void 삼품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
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
