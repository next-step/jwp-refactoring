package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.ProductRestAssured.상품_목록_조회_요청;
import static kitchenpos.acceptance.ProductRestAssured.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 인수 테스트")
public class ProductAcceptanceTest extends AbstractAcceptanceTest {

    @DisplayName("상품을 생성")
    @Test
    void 상품_생성() {
        //when
        ExtractableResponse<Response> response = 상품_생성_요청(1L, "허니콤보", BigDecimal.valueOf(18000));
        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("상품 목록을 조회")
    @Test
    void 상품_목록_조회() {
        // given
        ExtractableResponse<Response> response1 = 상품_생성_요청(1L, "허니콤보", BigDecimal.valueOf(18000));
        ExtractableResponse<Response> response2 = 상품_생성_요청(2L, "레드윙", BigDecimal.valueOf(190000));
        // when
        ExtractableResponse<Response> result = 상품_목록_조회_요청();
        // then
        assertThat(result.statusCode()).isEqualTo(HttpStatus.OK.value());
        상품_목록_포함_확인(result, Arrays.asList(response1, response2));
    }


    private static void 상품_목록_포함_확인(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> responses) {
        List<Long> expectedProductIds = responses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultProductIds = response.jsonPath().getList(".", Product.class).stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(resultProductIds).containsAll(expectedProductIds);
    }
}
