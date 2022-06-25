package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {
    public static final String PRODUCT_NAME01 = "바베큐치킨";
    public static final BigDecimal PRODUCT_PRICE01 = new BigDecimal(30000);

    public static final String PRODUCT_NAME02 = "치즈볼";
    public static final BigDecimal PRODUCT_PRICE02 = new BigDecimal(5000);

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_등록되어_있음(PRODUCT_NAME01, PRODUCT_PRICE01);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void getProducts() {
        // given
        ExtractableResponse<Response> createResponse1 = 상품_등록되어_있음(PRODUCT_NAME01, PRODUCT_PRICE01);
        ExtractableResponse<Response> createResponse2 = 상품_등록되어_있음(PRODUCT_NAME02, PRODUCT_PRICE02);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
        상품_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    public static ExtractableResponse<Response> 상품_등록되어_있음(String name, BigDecimal price) {
        Product product = new Product(name, price);
        return 상품_생성_요청(product);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(Product product) {
        return RestAssured
                .given().log().all()
                .body(product)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_생성됨(ExtractableResponse response) {
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

    public static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> resultLineIds = response.jsonPath().getList(".", Product.class).stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }

    public static Product 상품_가져옴(ExtractableResponse<Response> response) {
        return response.as(Product.class);
    }
}
