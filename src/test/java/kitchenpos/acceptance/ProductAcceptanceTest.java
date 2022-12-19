package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
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

@DisplayName("상품 관련 기능 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {
    private Product 삼겹살;
    private Product 소고기;

    @BeforeEach
    public void setUp() {
        super.setUp();
        삼겹살 = new Product(1L, "삼겹살", BigDecimal.valueOf(5_000));
        소고기 = new Product(2L, "소고기", BigDecimal.valueOf(1_000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        ExtractableResponse<Response> response = 상품_생성_요청(삼겹살);

        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findAllProduct() {
        삼겹살 = 상품_생성_요청(삼겹살).as(Product.class);
        소고기 = 상품_생성_요청(소고기).as(Product.class);

        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        상품_목록_응답됨(response);
        상품_목록_확인됨(response, Arrays.asList(삼겹살.getId(), 소고기.getId()));
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 상품_목록_확인됨(ExtractableResponse<Response> response, List<Long> productIds) {
        List<Long> resultIds = response.jsonPath().getList(".", Product.class)
                .stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(resultIds).containsAll(productIds);
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

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

}
