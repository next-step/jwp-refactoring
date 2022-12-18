package kitchenpos.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRestAssured {
    public static Product from(String productName, int productPrice) {
        Product product = new Product();
        product.setName(productName);
        product.setPrice(new BigDecimal(productPrice));
        return product;
    }

    public static void 상품_등록_되어_있음(Product product) {
        상품_생성_요청(product);
    }

    public static void 상품_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }


    public static void 상품_생성_안됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static ExtractableResponse<Response> 상품_생성_요청(Product params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_조회_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_조회_목록_포함됨(ExtractableResponse<Response> response, List<String> expectProductList) {
        List<String> retrieveProductList = response.jsonPath().getList(".", Product.class).stream()
                .map(Product::getName)
                .collect(Collectors.toList());

        assertThat(retrieveProductList).containsAll(expectProductList);
    }
}
