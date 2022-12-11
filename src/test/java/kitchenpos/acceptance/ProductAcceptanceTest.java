package kitchenpos.acceptance;

import static kitchenpos.acceptance.ProductRestAssured.상품_목록_조회_요청;
import static kitchenpos.acceptance.ProductRestAssured.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 관련 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    private Product 비빔국수;
    private Product 단무지;

    @BeforeEach
    public void setUp() {
        super.setUp();
        비빔국수 = new Product(1L, "비빔국수", BigDecimal.valueOf(5_000));
        단무지 = new Product(2L, "단무지", BigDecimal.valueOf(1_000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(비빔국수);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void findAllProduct() {
        // given
        비빔국수 = 상품_생성_요청(비빔국수).as(Product.class);
        단무지 = 상품_생성_요청(단무지).as(Product.class);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
        상품_목록_확인됨(response, Arrays.asList(비빔국수.getId(), 단무지.getId()));
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private static void 상품_목록_확인됨(ExtractableResponse<Response> response, List<Long> productIds) {
        List<Long> resultIds = response.jsonPath().getList(".", Product.class)
            .stream()
            .map(Product::getId)
            .collect(Collectors.toList());

        assertThat(resultIds).containsAll(productIds);
    }
}
