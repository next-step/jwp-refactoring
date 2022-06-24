package kitchenpos.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {
    private Product 상품;

    @BeforeEach
    public void setUp() {
        super.setUp();
        상품 = createProduct(1L, "상품", 1000);
    }

    /**
     * Feature: 상품을 관리한다.
     * <p>
     * Scenario: 상품 관리
     * <p>
     * When 상품 등록 요청
     * <p>
     * Then 상품 등록됨
     * <p>
     * When 상품 목록 조회 요청
     * <p>
     * Then 상품 목록이 조회됨
     */
    @DisplayName("상품을 관리")
    @Test
    void 상품_관리() {
        ExtractableResponse<Response> response;
        // when 상품 등록 요청
        response = 상품_등록_요청(상품);
        // then 상품 등록됨
        상품_등록됨(response);

        // when 상품 목록 조회 요청
        response = 상품_목록_조회();
        // then 상품 목록이 조회됨
        상품_목록_조회됨(response);
        // then 상품 목록이 조회됨
        상품_목록_포함됨(response, Arrays.asList(상품));
    }

    public static ExtractableResponse<Response> 상품_등록_요청(Product product) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }


    public static void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<Product> expectedProducts) {
        List<Long> resultProductIds = response.jsonPath().getList(".", Product.class).stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<Long> expectedProductIds = expectedProducts.stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(resultProductIds).containsAll(expectedProductIds);
    }

    private Product createProduct(Long id, String name, long price) {
        Product product = new Product();
        product.setId(id);
        product.setName(name);
        product.setPrice(BigDecimal.valueOf(price));

        return product;
    }
}