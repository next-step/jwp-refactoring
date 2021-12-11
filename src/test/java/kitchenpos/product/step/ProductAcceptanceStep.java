package kitchenpos.product.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.product.ui.request.ProductRequest;
import kitchenpos.product.ui.response.ProductResponse;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceStep {

    public static ProductResponse 상품_등록_되어_있음(String name, BigDecimal price) {
        return 상품_등록_요청(name, price).as(ProductResponse.class);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, BigDecimal price) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(new ProductRequest(name, price))
            .when()
            .post("/api/products")
            .then().log().all()
            .extract();
    }

    public static void 상품_등록_됨(ExtractableResponse<Response> response,
        String expectedName, BigDecimal expectedPrice) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.as(ProductResponse.class))
                .satisfies(product -> {
                    assertThat(product.getId()).isNotNull();
                    assertThat(product.getName()).isEqualTo(expectedName);
                    assertThat(product.getPrice()).isEqualByComparingTo(expectedPrice);
                })
        );
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured.given().log().all()
            .when()
            .get("/api/products")
            .then().log().all()
            .extract();
    }

    public static void 상품_목록_조회_됨(
        ExtractableResponse<Response> response, ProductResponse expectedProduct) {
        List<ProductResponse> products = response.as(new TypeRef<List<ProductResponse>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
            () -> assertThat(products)
                .first()
                .extracting(ProductResponse::getId)
                .isEqualTo(expectedProduct.getId())
        );
    }
}
