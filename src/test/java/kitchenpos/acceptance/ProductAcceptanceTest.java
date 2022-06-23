package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.application.ProductServiceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    Product product1;
    Product product2;

    @BeforeEach
    public void init() {
        super.init();

        product1 = ProductServiceTest.상품_생성(1L, "샐러드", 100);
        product2 = ProductServiceTest.상품_생성(2L, "스테이크", 200);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(product1);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void 상품_목록_조회() {
        // given
        ExtractableResponse<Response> createResponse1 = 상품_생성_요청(product1);
        ExtractableResponse<Response> createResponse2 = 상품_생성_요청(product2);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
        상품_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    public static ExtractableResponse<Response> 상품_생성되어_있음(String name, int price) {
        Product product = ProductServiceTest.상품_생성(null, name, price);
        return 상품_생성_요청(product);
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

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", Product.class).stream()
                .map(Product::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
