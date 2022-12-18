package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    private ProductResponse 소머리국밥;
    private ProductResponse 순대국밥;

    @BeforeEach
    public void setUp() {
        super.setUp();

        소머리국밥 = 상품_등록되어_있음("소머리국밥", BigDecimal.valueOf(8000)).as(ProductResponse.class);
        순대국밥 = 상품_등록되어_있음("순대국밥", BigDecimal.valueOf(7000)).as(ProductResponse.class);
    }


    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void create() {
        // given
        String name = "김치찌개";
        BigDecimal price = BigDecimal.valueOf(6000);

        // when
        ExtractableResponse<Response> response = 상품_등록_요청(name, price);

        // then
        상품_등록됨(response);
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다.")
    void list() {
        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(response);
    }

    public static ExtractableResponse<Response> 상품_등록되어_있음(String name, BigDecimal price) {
        return 상품_등록_요청(name, price);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
