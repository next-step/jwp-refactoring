package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTestFixture extends AcceptanceTest {

    public Product 떡볶이;
    public Product 튀김;
    public Product 순대;

    @BeforeEach
    public void setUp() {
        super.setUp();

        떡볶이 = 상품_생성_되어있음(new Product(null, "떡볶이", BigDecimal.valueOf(4500)));
        튀김 = 상품_생성_되어있음(new Product(null, "튀김", BigDecimal.valueOf(2500)));
        순대 = 상품_생성_되어있음(new Product(null, "순대", BigDecimal.valueOf(4000)));
    }

    public static ExtractableResponse<Response> 상품_생성_요청(Product product) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static Product 상품_생성_되어있음(Product product) {
        return 상품(상품_생성_요청(product));
    }

    public static Product 상품(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", Product.class);
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static List<Product> 상품_목록(ExtractableResponse<Response> response) {
        return response.jsonPath().getList("", Product.class);
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }
}
