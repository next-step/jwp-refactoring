package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.ProductRequest;
import kitchenpos.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.acceptance.ProductAcceptanceStep.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 관련 인수 테스트")
class ProductAcceptanceTest extends AcceptanceTest {
    private ProductRequest 허니콤보;
    private ProductRequest 레드윙;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        허니콤보 = ProductRequest.of("허니콤보", BigDecimal.valueOf(18000));
        레드윙 = ProductRequest.of( "레드윙", BigDecimal.valueOf(20000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        ExtractableResponse<Response> response = 상품_생성_요청(허니콤보);

        상품_생성됨(response);
    }

    @DisplayName("가격이 없는 상품은 생성할 수 없다.")
    @Test
    void createFail() {
        ProductRequest 가격없는_상품 = ProductRequest.of("가격없는_상품", null);

        ExtractableResponse<Response> response = 상품_생성_요청(가격없는_상품);

        상품_생성_실패함(response);
    }

    @DisplayName("가격이 음수인 상품은 생성할 수 없다.")
    @Test
    void createFail2() {
        ProductRequest 가격이_음수인_상품 = ProductRequest.of( "가격이_음수인_상품", BigDecimal.valueOf(-1));

        ExtractableResponse<Response> response = 상품_생성_요청(가격이_음수인_상품);

        상품_생성_실패함(response);
    }


    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse<Response> createResponse1 = 상품_등록되어_있음(허니콤보);
        ExtractableResponse<Response> createResponse2 = 상품_등록되어_있음(레드윙);

        ExtractableResponse<Response> listResponse = 상품_목록_조회_요청();

        상품_목록_조회됨(listResponse);
        상품_목록에_등록된_상품이_포함됨(listResponse, Arrays.asList(createResponse1, createResponse2));
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 상품_생성_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 상품_목록에_등록된_상품이_포함됨(ExtractableResponse<Response> listResponse,
                                    List<ExtractableResponse<Response>> createResponses) {
        List<ProductResponse> products = listResponse.jsonPath().getList(".", ProductResponse.class);
        List<ProductResponse> createdProducts = createResponses.stream()
                .map(it -> it.as(ProductResponse.class))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products).containsAll(createdProducts)
        );
    }
}
