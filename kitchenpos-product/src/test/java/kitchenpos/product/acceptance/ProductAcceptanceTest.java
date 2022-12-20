package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import product.dto.ProductRequest;
import product.dto.ProductResponse;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.product.acceptance.ProductAcceptanceUtils.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 관련 인수 테스트")
class ProductAcceptanceTest extends AcceptanceTest {
    private ProductRequest honeyCombo;
    private ProductRequest redWing;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        honeyCombo = ProductRequest.of("honeyCombo", BigDecimal.valueOf(18000));
        redWing = ProductRequest.of( "redWing", BigDecimal.valueOf(20000));
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        ExtractableResponse<Response> response = 상품_생성_요청(honeyCombo);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    @DisplayName("가격이 없는 상품은 생성할 수 없다.")
    @Test
    void createFail() {
        ProductRequest noPriceProduct = ProductRequest.of("가격없는_상품", null);

        ExtractableResponse<Response> response = 상품_생성_요청(noPriceProduct);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("가격이 음수인 상품은 생성할 수 없다.")
    @Test
    void createFail2() {
        ProductRequest underzeroPriceProduct = ProductRequest.of( "가격이_음수인_상품", BigDecimal.valueOf(-1));

        ExtractableResponse<Response> response = 상품_생성_요청(underzeroPriceProduct);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }


    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse<Response> createResponse1 = 상품_등록되어_있음(honeyCombo);
        ExtractableResponse<Response> createResponse2 = 상품_등록되어_있음(redWing);

        ExtractableResponse<Response> listResponse = 상품_목록_조회_요청();

        assertThat(listResponse.statusCode()).isEqualTo(HttpStatus.OK.value());

        List<ProductResponse> products = listResponse.jsonPath().getList(".", ProductResponse.class);
        List<ProductResponse> createdProducts = Arrays.asList(createResponse1, createResponse2).stream()
                .map(it -> it.as(ProductResponse.class))
                .collect(Collectors.toList());

        assertAll(
                () -> Assertions.assertThat(products).hasSize(2),
                () -> Assertions.assertThat(products).containsAll(createdProducts)
        );
    }

}
