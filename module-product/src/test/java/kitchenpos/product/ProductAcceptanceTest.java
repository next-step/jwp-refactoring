package kitchenpos.product;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.ui.ProductRestControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptancePerMethodTest {

    @DisplayName("상품 관리")
    @Test
    void manage() {
        // When
        ExtractableResponse<Response> 등록_응답 = 상품_등록_요청("햄버거", 5000);
        // Then
        상품이_등록됨(등록_응답);

        // When
        ExtractableResponse<Response> 목록_조회_응답 = 상품_목록_조회_요청();
        // Then
        상품_목록_조회됨(목록_조회_응답);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return AcceptanceTest.get(ProductRestControllerTest.BASE_URL);
    }

    private List<ProductResponse> 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<ProductResponse> 목록_조회_응답 = new ArrayList<>(response.jsonPath().getList(".", ProductResponse.class));
        assertThat(목록_조회_응답).hasSize(1);
        return 목록_조회_응답;
    }

    public static ExtractableResponse<Response> 상품_등록_요청(String name, Integer price) {
        Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price);

        return AcceptanceTest.post(params, ProductRestControllerTest.BASE_URL);
    }

    public static void 상품이_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

}
