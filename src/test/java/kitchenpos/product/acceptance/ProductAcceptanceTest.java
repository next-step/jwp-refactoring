package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class ProductAcceptanceTest extends AcceptanceTest {

    private static final String PRODUCT_URL = "/api/products";

    @Test
    @DisplayName("상품을 관리한다.")
    void manageProduct() {
        // 상품 등록 요청
        ExtractableResponse<Response> saveResponse = 상품_등록_요청("상품", BigDecimal.valueOf(10000));
        // 상품 등록 됨
        상품_등록_됨(saveResponse);

        // 상품 목록 조회 요청
        ExtractableResponse<Response> response = 상품_목록_조회_요청();
        // 상품 목록 조회 됨
        상품_목록_조회_됨(response, "상품");

    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
        Product product = new Product();
        product.setName(name);
        product.setPrice(price);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(product)
            .when().post(PRODUCT_URL)
            .then().log().all()
            .extract();
    }

    public static void 상품_등록_됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(PRODUCT_URL)
            .then().log().all()
            .extract();
    }

    public static void 상품_목록_조회_됨(ExtractableResponse<Response> response, String... expected) {
        List<Product> list = response.jsonPath().getList(".", Product.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(list).extracting(Product::getName).containsExactly(expected);
    }

    public static Product 상품_등록_되어있음(String name, BigDecimal price) {
        return 상품_등록_요청(name, price).as(Product.class);
    }
}
