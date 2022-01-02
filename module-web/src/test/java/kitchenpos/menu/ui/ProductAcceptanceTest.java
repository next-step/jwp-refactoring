package kitchenpos.menu.ui;

import static kitchenpos.common.AcceptanceFixture.*;
import static kitchenpos.fixture.ProductAcceptanceFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.math.*;
import java.util.*;
import java.util.stream.*;

import org.junit.jupiter.api.*;

import io.restassured.response.*;
import kitchenpos.common.*;
import kitchenpos.menu.dto.*;

@DisplayName("상품 관련(인수 테스트)")
class ProductAcceptanceTest extends AcceptanceTest {
    private ProductRequest 순살치킨;
    private ProductResponse 순살치킨_등록됨;
    private ProductResponse 간장치킨_등록됨;

    @DisplayName("상품 조회 하기")
    @Test
    void findTest() {
        순살치킨_등록됨 = 상품_생성_요청(ProductRequest.of("순살치킨", BigDecimal.valueOf(17000))).as(ProductResponse.class);
        간장치킨_등록됨 = 상품_생성_요청(ProductRequest.of("간장치킨", BigDecimal.valueOf(17000))).as(ProductResponse.class);

        ExtractableResponse<Response> 상품_조회_요청됨 = 상품_조회_요청();

        OK_응답_잘_받았음(상품_조회_요청됨);
        상품_조회_확인(상품_조회_요청됨, 순살치킨_등록됨, 간장치킨_등록됨);
    }

    @DisplayName("상품 생성 하기")
    @Test
    void createTest() {
        순살치킨 = ProductRequest.of("순살치킨", BigDecimal.valueOf(17000));

        ExtractableResponse<Response> 상품_생성_요청됨 = 상품_생성_요청(순살치킨);

        CREATE_응답_잘_받음(상품_생성_요청됨);
        상품_생성_확인(상품_생성_요청됨, 순살치킨);
    }

    @DisplayName("상품 이름 없으면 생성 실패함")
    @Test
    void failTest1() {
        순살치킨 = ProductRequest.of("", BigDecimal.valueOf(17000));
        BAD_REQUEST_응답_잘_받았음(상품_생성_요청(순살치킨));
    }

    @DisplayName("상품 가격 유효하지 않으면 생성 실패함")
    @Test
    void failTest2() {
        순살치킨 = ProductRequest.of("순살치킨", BigDecimal.valueOf(-17000));
        BAD_REQUEST_응답_잘_받았음(상품_생성_요청(순살치킨));
    }

    @DisplayName("상품 금액이 없으면 실패함")
    @Test
    void failTest3() {
        순살치킨 = ProductRequest.of("순살치킨", null);
        BAD_REQUEST_응답_잘_받았음(상품_생성_요청(순살치킨));
    }

    private void 상품_생성_확인(ExtractableResponse<Response> actual, ProductRequest productRequest) {
        ProductResponse response = actual.as(ProductResponse.class);
        assertThat(response.getName()).isEqualTo(productRequest.getName());
    }

    private void 상품_조회_확인(ExtractableResponse<Response> actual, ProductResponse... expected) {
        List<Long> expectedIds = Arrays.stream(expected)
            .map(ProductResponse::getId)
            .collect(Collectors.toList());

        List<Long> response = actual.jsonPath()
            .getList(".", ProductResponse.class)
            .stream()
            .map(ProductResponse::getId)
            .collect(Collectors.toList());

        assertThat(response).containsAll(expectedIds);
    }
}
