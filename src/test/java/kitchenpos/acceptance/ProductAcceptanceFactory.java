package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductAcceptanceFactory {

    public static ExtractableResponse<Response> 상품_등록_요청(String name, int price) {
        ProductRequest product = ProductRequest.from(name,price);

        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();
    }
    public static ExtractableResponse<Response> 상품_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/products")
                .then().log().all()
                .extract();
    }



    public static void 상품_등록_성공(ExtractableResponse<Response> 상품등록_결과, String 상품명) {
        Product productResponse = 상품등록_결과.as(Product.class);
        assertAll(
                () -> assertThat(상품등록_결과.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(productResponse.getName()).isEqualTo(상품명)
        );
    }

    public static void 상품_등록_실패(ExtractableResponse<Response> 상품등록_결과) {
        assertThat(상품등록_결과.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 상품_조회_성공(ExtractableResponse<Response> 상품조회_결과, List<Product> 예상조회결과) {
        List<Product> productList = 상품조회_결과.as(List.class);
        assertAll(
                () -> assertThat(상품조회_결과.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(productList.size()).isEqualTo(예상조회결과.size())
        );
    }
}
