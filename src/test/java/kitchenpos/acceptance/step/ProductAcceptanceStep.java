package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.product.Product;
import org.springframework.http.MediaType;

public class ProductAcceptanceStep {

    private static final String API_URL = "/api/products";

    private ProductAcceptanceStep() {
    }

    public static Product 상품_등록됨(Product product) {
        ExtractableResponse<Response> 상품_등록_요청 = 상품_등록_요청(product);

        return 상품등록_검증(상품_등록_요청, product);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(Product product) {
        return RestAssured
            .given().log().all()
            .body(product)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 상품_목록조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(API_URL)
            .then().log().all()
            .extract();
    }


    public static List<Product> 상품_목록조회_검증(ExtractableResponse<Response> response,
        Product expected) {
        List<Product> 조회된_상품목록 = response.as(new TypeRef<List<Product>>() {
        });
        assertThat(조회된_상품목록).contains(expected);

        return 조회된_상품목록;
    }


    public static Product 상품등록_검증(ExtractableResponse<Response> response, Product expected) {
        Product 등록된_상품 = response.as(Product.class);
        assertThat(등록된_상품.getId()).isNotNull();
        assertThat(등록된_상품.getName()).isEqualTo(expected.getName());
        assertThat(등록된_상품.getPrice()).isEqualByComparingTo(expected.getPrice());

        return 등록된_상품;
    }
}
