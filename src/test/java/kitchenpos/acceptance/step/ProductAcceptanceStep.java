package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.List;
import kitchenpos.domain.Product;
import org.springframework.http.MediaType;

public class ProductAcceptanceStep {

    private static final String PRODUCT_API_URL = "/api/products";

    private ProductAcceptanceStep() {
    }

    public static Product 상품_등록됨(Product 등록_파라미터) {
        ExtractableResponse<Response> 상품_등록_요청 = 상품_등록_요청(등록_파라미터);

        return 상품등록_검증(상품_등록_요청, 등록_파라미터);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(Product 등록_파라미터) {
        return RestAssured
            .given().log().all()
            .body(등록_파라미터)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(PRODUCT_API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 상품_목록조회_요청() {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().get(PRODUCT_API_URL)
            .then().log().all()
            .extract();
    }


    public static List<Product> 상품_목록조회_검증(Product 등록된_상품,
        ExtractableResponse<Response> 상품_목록조회_결과) {
        List<Product> 조회된_상품목록 = 상품_목록조회_결과.as(new TypeRef<List<Product>>() {
        });
        assertThat(조회된_상품목록).contains(등록된_상품);

        return 조회된_상품목록;
    }


    public static Product 상품등록_검증(ExtractableResponse<Response> 상품등록_결과, Product 예상_상품) {
        Product 등록된_상품 = 상품등록_결과.as(Product.class);
        assertThat(등록된_상품.getId()).isNotNull();
        assertThat(등록된_상품.getName()).isEqualTo(예상_상품.getName());
        assertThat(등록된_상품.getPrice()).isEqualByComparingTo(예상_상품.getPrice());

        return 등록된_상품;
    }
}
