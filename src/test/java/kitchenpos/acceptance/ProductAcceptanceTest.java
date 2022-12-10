package kitchenpos.acceptance;

import static kitchenpos.acceptance.ProductRestAssured.상품_등록되어_있음;
import static kitchenpos.acceptance.ProductRestAssured.상품_목록_조회_요청;
import static kitchenpos.acceptance.ProductRestAssured.상품_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 관련 인수 테스트")
class ProductAcceptanceTest extends AcceptanceTest {

    private ProductRequest 버팔로윙;
    private ProductRequest 치킨텐더;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        버팔로윙 = ProductRequest.of("버팔로윙", BigDecimal.valueOf(6_500));
        치킨텐더 = ProductRequest.of("치킨텐더", BigDecimal.valueOf(5_900));
    }

    /**
     * When 상품 생성 요청
     * Then 상품 생성됨
     */
    @DisplayName("상품을 생성한다.")
    @Test
    void create() {
        ExtractableResponse<Response> response = 상품_생성_요청(버팔로윙);

        상품_생성됨(response);
    }

    /**
     * When 가격이 없는 상품 생성 요청
     * Then 상품 생성 실패함
     */
    @DisplayName("가격이 없는 상품은 생성할 수 없다.")
    @Test
    void createFail() {
        ProductRequest 가격없는_상품 = ProductRequest.of("가격없는_상품", null);

        ExtractableResponse<Response> response = 상품_생성_요청(가격없는_상품);

        상품_생성_실패함(response);
    }

    /**
     * When 가격이 음수인 상품 생성 요청
     * Then 상품 생성 실패함
     */
    @DisplayName("가격이 음수인 상품은 생성할 수 없다.")
    @Test
    void createFail2() {
        ProductRequest 가격이_음수인_상품 = ProductRequest.of("가격이_음수인_상품", BigDecimal.valueOf(-1));

        ExtractableResponse<Response> response = 상품_생성_요청(가격이_음수인_상품);

        상품_생성_실패함(response);
    }

    /**
     * Given 상품 여러개 등록됨
     * When 상품 목록 조회 요청
     * Then 상품 목록 조회됨
     * Then 상품 목록에 등록된 상품이 포함됨
     */
    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() {
        ExtractableResponse<Response> createResponse1 = 상품_등록되어_있음(버팔로윙);
        ExtractableResponse<Response> createResponse2 = 상품_등록되어_있음(치킨텐더);

        ExtractableResponse<Response> listResponse = 상품_목록_조회_요청();

        상품_목록_조회됨(listResponse);
        상품_목록에_등록된_상품이_포함됨(listResponse, Arrays.asList(createResponse1, createResponse2));
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 상품_생성_실패함(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 상품_목록에_등록된_상품이_포함됨(ExtractableResponse<Response> listResponse,
                                    List<ExtractableResponse<Response>> createResponses) {
        List<Product> products = listResponse.jsonPath().getList(".", Product.class);
        List<Product> createdProducts = createResponses.stream()
                .map(it -> it.as(Product.class))
                .collect(Collectors.toList());

        assertAll(
                () -> assertThat(products).hasSize(2),
                () -> assertThat(products).containsAll(createdProducts)
        );
    }
}
