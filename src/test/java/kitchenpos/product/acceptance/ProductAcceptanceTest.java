package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.product.testfixtures.acceptance.ProductAcceptanceFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;


public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 등록")
    @Test
    void create() {
        //given
        ProductRequest 양념치킨_정보 = ProductAcceptanceFixtures.상품_정의("양념치킨", BigDecimal.valueOf(10000));

        //when
        ResponseEntity<ProductResponse> 양념치킨_생성_결과 = ProductAcceptanceFixtures.상품_생성_요청(
            양념치킨_정보);

        //then
        상품_생성_정상_확인(양념치킨_생성_결과);
        상품_생성_세부내용_확인(양념치킨_생성_결과, 양념치킨_정보);
    }

    @Transactional
    @DisplayName("상품 조회")
    @Test
    void list() {
        //given
        ProductResponse 양념치킨 = ProductAcceptanceFixtures.상품_생성_요청(
            ProductAcceptanceFixtures.상품_정의("양념치킨", BigDecimal.valueOf(10000.00))).getBody();
        ProductResponse 후라이드 = ProductAcceptanceFixtures.상품_생성_요청(
            ProductAcceptanceFixtures.상품_정의("후라이드", BigDecimal.valueOf(9000.00))).getBody();

        //when
        ResponseEntity<List<ProductResponse>> 조회_결과 = ProductAcceptanceFixtures.상품_전체_조회_요청();

        //then
        조회_정상(조회_결과);
        조회목록_정상(조회_결과, Arrays.asList(양념치킨, 후라이드));
    }

    private void 조회_정상(ResponseEntity<List<ProductResponse>> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    private void 상품_생성_정상_확인(ResponseEntity<ProductResponse> response) {
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    private void 상품_생성_세부내용_확인(ResponseEntity<ProductResponse> response,
        ProductRequest productRequest) {
        ProductResponse createdProductResponse = response.getBody();
        assertThat(createdProductResponse.getName()).isEqualTo(productRequest.getName());
    }

    private void 조회목록_정상(ResponseEntity<List<ProductResponse>> response,
        List<ProductResponse> expectedProductResponse) {
        List<ProductResponse> productResponses = response.getBody();
        assertThat(productResponses).containsAll(expectedProductResponse);
    }
}
