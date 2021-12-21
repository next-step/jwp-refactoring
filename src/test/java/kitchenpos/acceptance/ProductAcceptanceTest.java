package kitchenpos.acceptance;

import static kitchenpos.utils.TestFactory.*;
import static org.assertj.core.api.Assertions.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.utils.StatusValidation;
import kitchenpos.utils.TestFactory;
import org.apache.http.HttpStatus;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    public static final String  PRODUCT_DEFAULT_URL = "/products";
    public ProductRequest 후라이드;
    public ProductRequest 양념치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드  = new ProductRequest(1L, "후라이드", 16000L);
        양념치킨  = new ProductRequest(2L, "양념치킨", 16000L);
    }

    @Test
    void 상품을_생성한다() {
        ExtractableResponse<Response> response = 상품_생성_요청(후라이드);

        상품_생성됨(response);
    }

    @Test
    void 상품을_조회한다() {
        상품_등록_됨(후라이드);
        상품_등록_됨(후라이드);

        ExtractableResponse<Response> response = 전체_상품_조회_요청();

        전체_상품_조회됨(response);
    }




    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest productRequest){
        return post(PRODUCT_DEFAULT_URL, productRequest);
    }

    public static ExtractableResponse<Response> 상품_등록_됨(ProductRequest productRequest){
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
