package kitchenpos.product.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.acceptance.step.HttpUtil;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;

public class ProductAcceptanceStep {

    private static final String API_URL = "/api/products";

    private ProductAcceptanceStep() {
    }

    public static Long 상품_등록됨(ProductRequest request) {
        ExtractableResponse<Response> 상품_등록_요청 = 상품_등록_요청(request);

        return 상품등록_검증(상품_등록_요청, request);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest request) {
        return HttpUtil.post(API_URL, request);
    }

    public static ExtractableResponse<Response> 상품_목록조회_요청() {
        return HttpUtil.get(API_URL);
    }


    public static List<ProductResponse> 상품_목록조회_검증(ExtractableResponse<Response> response,
        Long expected) {
        List<ProductResponse> 조회된_상품목록 = response.as(new TypeRef<List<ProductResponse>>() {
        });

        assertThat(조회된_상품목록).extracting("id").contains(expected);

        return 조회된_상품목록;
    }


    public static Long 상품등록_검증(ExtractableResponse<Response> response,
        ProductRequest expected) {
        ProductResponse 등록된_상품 = response.as(ProductResponse.class);

        assertAll(
            () -> assertThat(등록된_상품.getId()).isNotNull(),
            () -> assertThat(등록된_상품.getName()).isEqualTo(expected.getName()),
            () -> assertThat(등록된_상품.getPrice()).isEqualByComparingTo(expected.getPrice())
        );

        return 등록된_상품.getId();
    }

    public static ProductRequest 양념치킨() {
        return new ProductRequest("양념치킨", BigDecimal.valueOf(16000));
    }
}
