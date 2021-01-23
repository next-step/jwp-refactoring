package kitchenpos.acceptance.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.AcceptanceTest;
import kitchenpos.domain.valueobject.Price;
import kitchenpos.dto.product.ProductRequest;
import kitchenpos.dto.product.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {
    private final static String DEFAULT_PRODUCT_NAME = "후라이드 치킨";

    @Test
    @DisplayName("시나리오1: 상품을 등록하고 목록을 조회할 수 있다.")
    public void scenarioTest() throws Exception {
        // when 상품 등록 요청
        ExtractableResponse<Response> 상품_등록 = 상품_등록_요청(DEFAULT_PRODUCT_NAME, 16000);
        // then 상품 등록됨
        상품_등록됨(상품_등록);

        // when 상품 목록 조회 요청
        ExtractableResponse<Response> 상품_목록_조회 = 상품_목록_조회_요청();
        // then 등록한 상품이 반영된 상품 목록이 조회됨
        상품_목록_조회됨(상품_목록_조회);
   }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, int price) {
        ProductRequest productRequest = new ProductRequest(name, Price.of(price));

        // when
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(productRequest)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<ProductResponse> productResponses = response.jsonPath().getList(".", ProductResponse.class);
        ProductResponse addedProductResponse = productResponses.get(productResponses.size() - 1);
        assertAll(
                () -> assertThat(addedProductResponse.getId()).isNotNull(),
                () -> assertThat(addedProductResponse.getName()).isEqualTo(DEFAULT_PRODUCT_NAME),
                () -> assertThat(addedProductResponse.getPrice()).isEqualTo(BigDecimal.valueOf(16_000.0))
        );
    }
}
