package kitchenpos.product.acceptance;

import static kitchenpos.product.acceptance.ProductTestFixture.상품_목록_조회_요청함;
import static kitchenpos.product.acceptance.ProductTestFixture.상품_생성_요청함;
import static kitchenpos.product.acceptance.ProductTestFixture.상품_생성됨;
import static kitchenpos.product.acceptance.ProductTestFixture.상품_조회_요청_응답됨;
import static kitchenpos.product.acceptance.ProductTestFixture.상품_조회_포함됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class ProductAcceptanceTest extends AcceptanceTest {

    private String 강정치킨;
    private BigDecimal 강정치킨가격;
    private String 양념치킨;
    private BigDecimal 양념치킨가격;

    @BeforeEach
    public void setUp() {
        super.setUp();
        강정치킨 = "강정치킨";
        강정치킨가격 = BigDecimal.valueOf(17000);
        양념치킨 = "양념치킨";
        양념치킨가격 = BigDecimal.valueOf(19000);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        //when
        ExtractableResponse<Response> response = 상품_생성_요청함(강정치킨, 강정치킨가격);

        //then
        상품_생성됨(response);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void list() {
        //given
        ExtractableResponse<Response> 강정치킨_response = 상품_생성_요청함(강정치킨, 강정치킨가격);
        ExtractableResponse<Response> 양념치킨_response = 상품_생성_요청함(양념치킨, 양념치킨가격);

        //when
        ExtractableResponse<Response> response = 상품_목록_조회_요청함();

        //then
        상품_조회_요청_응답됨(response);
        상품_조회_포함됨(response, Arrays.asList(강정치킨_response, 양념치킨_response));
    }

}
