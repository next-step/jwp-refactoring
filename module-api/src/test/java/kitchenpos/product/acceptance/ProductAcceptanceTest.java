package kitchenpos.product.acceptance;

import static kitchenpos.utils.TestFactory.get;
import static kitchenpos.utils.TestFactory.post;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.utils.StatusValidation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    private static final String PRODUCT_DEFAULT_URL = "/products";
    private ProductRequest 양념치킨;
    private ProductRequest 코카콜라;

    @BeforeEach
    public void setUp() {
        super.setUp();
        양념치킨 = new ProductRequest(1L, "양념치킨", 16000L);
        코카콜라 = new ProductRequest(2L, "코카콜라", 2000L);
    }

    @Test
    void 상품을_생성한다() {

        // when
        ExtractableResponse<Response> response = 상품_생성_요청(양념치킨);

        // then
        상품_생성됨(response);
    }

    @Test
    void 상품을_조회한다() {
        상품_등록_됨(양념치킨);
        상품_등록_됨(코카콜라);

        ExtractableResponse<Response> response = 전체_상품_조회_요청();

        전체_상품_조회됨(response);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest) {
        return post(PRODUCT_DEFAULT_URL, productRequest);
    }

    public static ExtractableResponse<Response> 상품_등록_됨(ProductRequest productRequest) {
        return 상품_생성_요청(productRequest);
    }

    public ExtractableResponse<Response> 전체_상품_조회_요청() {
        return get(PRODUCT_DEFAULT_URL);
    }

    public static void 전체_상품_조회됨(ExtractableResponse<Response> response) {
        StatusValidation.조회됨(response);
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        StatusValidation.생성됨(response);
    }
}
