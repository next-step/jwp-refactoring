package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.utils.Http;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관리 기능")
public class ProductAcceptanceTest extends AcceptanceTest {
    @DisplayName("상품을 관리한다")
    @Test
    void testManagement() {
        // given
        ProductRequest 볶음짜장면_요청 = new ProductRequest("볶음짜장면", 8000);

        // when
        ExtractableResponse<Response> createResponse = 상품_생성_요청(볶음짜장면_요청);
        // then
        ProductResponse 볶음짜장면 = 상품_생성됨(createResponse);

        // when
        ExtractableResponse<Response> listResponse = 모든_상품_조회_요청();
        // then
        모든_상품_조회_응답(listResponse);
        생성한_상품이_상품_목록에_포함됨(볶음짜장면, listResponse);
    }

    /**
     * 요청 관련
     */
    private static ExtractableResponse<Response> 모든_상품_조회_요청() {
        return Http.get("/api/products");
    }

    private static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return Http.post("/api/products", productRequest);
    }

    /**
     * 응답 관련
     */
    private static void 생성한_상품이_상품_목록에_포함됨(ProductResponse product, ExtractableResponse<Response> listResponse) {
        List<ProductResponse> products = listResponse.jsonPath().getList(".", ProductResponse.class);
        assertThat(products).containsExactly(product);
    }

    private static void 모든_상품_조회_응답(ExtractableResponse<Response> listResponse) {
        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static ProductResponse 상품_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        return createResponse.as(ProductResponse.class);
    }

    /**
     * 테스트 픽스처 관련
     */
    public static ProductResponse 상품_등록되어_있음(String name, long price) {
        return 상품_생성_요청(new ProductRequest(name, price)).as(ProductResponse.class);
    }
}
