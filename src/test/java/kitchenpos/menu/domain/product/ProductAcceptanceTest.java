package kitchenpos.menu.domain.product;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.menu.dto.ProductRequest;
import kitchenpos.menu.dto.ProductResponse;
import kitchenpos.utils.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.fixture.ProductDomainFixture.상품_생성_요청;
import static kitchenpos.fixture.ProductDomainFixture.상품_조회_요청;
import static kitchenpos.utils.AcceptanceFixture.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("인수 테스트 - 상품 관리")
class ProductAcceptanceTest extends AcceptanceTest {
    private ProductRequest 치킨;
    private ProductRequest 콜라;

    private ProductResponse 치킨_등록됨;
    private ProductResponse 콜라_등록됨;


    private void 상품_생성됨(ExtractableResponse<Response> actual, ProductRequest productRequest) {
        ProductResponse response = actual.as(ProductResponse.class);
        assertThat(response.getName()).isEqualTo(productRequest.getName());
    }

    private void 상품_조회됨(ExtractableResponse<Response> actual, ProductResponse... expected) {
        List<Long> expectedIds = Arrays.stream(expected).map(ProductResponse::getId).collect(Collectors.toList());

        List<Long> response = actual.jsonPath().getList(".", ProductResponse.class)
                .stream().map(ProductResponse::getId).collect(Collectors.toList());

        assertThat(response).containsAll(expectedIds);
    }

    @Test
    @DisplayName("상품 조회")
    public void 상품_조회() {
        // given
        치킨_등록됨 = 상품_생성_요청(ProductRequest.of("치킨", BigDecimal.valueOf(15000))).as(ProductResponse.class);
        콜라_등록됨 = 상품_생성_요청(ProductRequest.of("콜라", BigDecimal.valueOf(1000))).as(ProductResponse.class);

        // when
        final ExtractableResponse<Response> actual = 상품_조회_요청();

        응답_OK(actual);
        상품_조회됨(actual, 치킨_등록됨, 콜라_등록됨);
    }

    @Nested
    @DisplayName("상품 생성")
    class CreateProduct {
        @Test
        @DisplayName("성공")
        public void 상품_생성() {
            // given
            치킨 = ProductRequest.of("치킨", BigDecimal.valueOf(15000));

            // when
            final ExtractableResponse<Response> actual = 상품_생성_요청(치킨);

            // then
            응답_CREATE(actual);
            상품_생성됨(actual, 치킨);
        }

        @Test
        @DisplayName("실패 - 상품명이 없음")
        public void 상품_생성_실패_상품명_없음() {
            // given
            치킨 = ProductRequest.of("", BigDecimal.valueOf(15000));

            // when
            final ExtractableResponse<Response> actual = 상품_생성_요청(치킨);

            // then
            응답_BAD_REQUEST(actual);
        }

        @Test
        @DisplayName("실패 - 상품의 가격이 0보다 작음")
        public void 상품_생성_실패_가격_음수() {
            // given
            치킨 = ProductRequest.of("치킨", BigDecimal.valueOf(-1));

            // when
            final ExtractableResponse<Response> actual = 상품_생성_요청(치킨);

            // then
            응답_BAD_REQUEST(actual);
        }

        @Test
        @DisplayName("실패 - 금액이 없음")
        public void 상품_생성_실패_금액_없음() {
            // given
            치킨 = ProductRequest.of("치킨", null);

            // when
            final ExtractableResponse<Response> actual = 상품_생성_요청(치킨);

            // then
            응답_BAD_REQUEST(actual);
        }
    }
}
