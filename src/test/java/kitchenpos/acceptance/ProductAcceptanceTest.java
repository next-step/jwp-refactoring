package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.ui.dto.ProductCreateRequest;
import kitchenpos.menu.ui.dto.ProductCreateResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("상품 관리")
public class ProductAcceptanceTest extends AcceptanceTest {
    public static final ProductCreateRequest 파닭 = new ProductCreateRequest("파닭", BigDecimal.valueOf(10_000));

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @TestFactory
    Stream<DynamicTest> 상품_관리_시나리오() {
        return Stream.of(
                dynamicTest("상품을 등록한다.", this::상품을_등록한다),
                dynamicTest("상품 목록을 조회한다.", this::상품_목록을_조회한다)
        );
    }

    private void 상품을_등록한다() {
        // when
        ExtractableResponse<Response> 상품_등록_응답 = 상품_등록_요청(파닭);

        // then
        상품_등록됨(상품_등록_응답);
    }

    private void 상품_목록을_조회한다() {
        // when
        ExtractableResponse<Response> 상품_목록_조회_응답 = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(상품_목록_조회_응답);
    }

    public static ExtractableResponse<Response> 상품_등록_요청(ProductCreateRequest request) {
        return post("/api/products", request);
    }

    public static void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static ProductCreateResponse 상품_등록됨(ProductCreateRequest request) {
        return 상품_등록_요청(request).as(ProductCreateResponse.class);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return get("/api/products");
    }

    public static void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(response.as(ArrayList.class)).hasSize(1);
    }
}
