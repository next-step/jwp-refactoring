package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {
    private Product product;

    @BeforeEach
    public void setUp() {
        super.setUp();
        product = new Product("강정치킨", new BigDecimal(17000));
    }

    @DisplayName("상품을 관리한다.")
    @Test
    void manageProduct() {
        ExtractableResponse<Response> createResponse = 상품_등록_요청(product);
        상품_등록됨(createResponse);

        ExtractableResponse<Response> findResponse = 상품목록_조회_요청();
        상품목록_조회됨(findResponse);
    }

    private ExtractableResponse<Response> 상품_등록_요청(Product product) {
        return RestAssured.given().log().all().
                body(product).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/products").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 상품목록_조회_요청() {
        return RestAssured.given().log().all().
                when().get("/api/products").
                then().log().all().
                extract();
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(Product.class).getName()).isEqualTo(product.getName());
        assertThat(response.as(Product.class).getPrice().compareTo(product.getPrice())).isEqualTo(0);
    }

    private void 상품목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<Product> products = response.jsonPath().getList(".", Product.class);
        List<String> productNames = products.stream().map(product -> product.getName()).collect(Collectors.toList());

        assertThat(productNames).contains(product.getName());
    }
}
