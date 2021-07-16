package kitchenpos.product.acceptance;

import kitchenpos.common.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;

@DisplayName("상품 관리 기능")
public class ProductAcceptanceTest extends AcceptanceTest {
    @DisplayName("상품을 관리한다.")
    @Test
    public void productManager() throws Exception {
        ProductRequest 상품 = new ProductRequest("후라이드", BigDecimal.valueOf(15_000));
        ExtractableResponse<Response> postResponse = 상품_등록_요청(상품);
        상품_등록됨(postResponse);

        ExtractableResponse<Response> getResponse = 상품_목록조회_요청(postResponse);
        상품_목록조회됨(getResponse);
    }

    public static ProductResponse 상품_등록_되어있음(ProductRequest 상품) {
        ExtractableResponse<Response> response = 상품_등록_요청(상품);
        상품_등록됨(response);
        return response.as(ProductResponse.class);
    }

    private void 상품_목록조회됨(ExtractableResponse<Response> getResponse) {
        assertHttpStatus(getResponse, OK);
    }

    private ExtractableResponse<Response> 상품_목록조회_요청(ExtractableResponse<Response> postResponse) {
        return get("/api/products");
    }

    private static void 상품_등록됨(ExtractableResponse<Response> postResponse) {
        assertHttpStatus(postResponse, CREATED);
    }

    private static ExtractableResponse<Response> 상품_등록_요청(ProductRequest 상품) {
        return post("/api/products", 상품);
    }
}
