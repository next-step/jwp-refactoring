package kitchenpos.product.acceptance;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상픔 관련 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {
    private Product 후라이드;
    private Product 순살치킨;

    @BeforeEach
    public void setUp() {
        super.setUp();
        후라이드 = new Product(1L, "후라이드", BigDecimal.valueOf(16_000));
        순살치킨 = new Product(1L, "순살치킨", BigDecimal.valueOf(17_000));
    }

    @Test
    void 상품을_등록_할_수_있다() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(후라이드);

        // then
        상품_생성됨(response);
    }

    @Test
    void 상품_목록을_조회_할_수_있다() {
        // given
        후라이드 = 상품_생성_요청(후라이드).as(Product.class);
        순살치킨 = 상품_생성_요청(순살치킨).as(Product.class);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response, Arrays.asList(후라이드.getId(), 순살치킨.getId()));
    }

    public static ExtractableResponse<Response> 상품_생성_요청(Product product) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 상품_목록_응답됨(ExtractableResponse<Response> response, List<Long> productIds) {
        List<Long> ids = response.jsonPath().getList(".", Product.class)
                        .stream()
                        .map(Product::getId)
                        .collect(Collectors.toList());

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(ids).containsAll(productIds)
        );
    }
}
