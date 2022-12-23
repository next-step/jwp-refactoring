package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.acceptance.AcceptanceTest;
import product.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.acceptance.ProductAcceptanceStep.*;
import static kitchenpos.product.fixture.ProductTestFixture.상품;

@DisplayName("상품 관련 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    private ProductRequest 짜장면;
    private ProductRequest 단무지;

    @BeforeEach
    public void setUp() {
        super.setUp();
        짜장면 = 상품("짜장면", BigDecimal.valueOf(8000L));
        단무지 = 상품( "단무지", BigDecimal.valueOf(0L));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(짜장면);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        // given
        List<ExtractableResponse<Response>> 등록된_상품_목록 = Arrays.asList(등록된_상품(짜장면), 등록된_상품(단무지));

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
        상품_목록_포함됨(response, 등록된_상품_목록);
    }
}
