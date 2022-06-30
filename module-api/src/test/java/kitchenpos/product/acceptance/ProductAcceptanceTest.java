package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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

    ProductRequest productRequest1;
    ProductRequest productRequest2;
    ProductRequest productRequest3;

    @BeforeEach
    public void init() {
        super.init();

        // given
        productRequest1 = new ProductRequest("샐러드", 100);
        productRequest2 = new ProductRequest("스테이크", 100);
        productRequest3 = new ProductRequest("에이드", -5);
    }

    @DisplayName("상품 생성에 성공한다.")
    @Test
    void 생성() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(productRequest1);

        // then
        상품_생성됨(response);
    }

    @DisplayName("상품 가격이 0보다 작으면 생성에 실패한다.")
    @Test
    void 생성_예외_가격_오류() {
        // when
        ExtractableResponse<Response> response = 상품_생성_요청(productRequest3);

        // then
        상품_생성_실패됨(response);
    }

    @DisplayName("상품 목록 조회에 성공한다.")
    @Test
    void 목록_조회() {
        // given
        ExtractableResponse<Response> createResponse1 = 상품_생성_요청(productRequest1);
        ExtractableResponse<Response> createResponse2 = 상품_생성_요청(productRequest2);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(response);
        상품_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2));
    }

    public static ExtractableResponse<Response> 상품_생성되어_있음(String name, long price) {
        return 상품_생성_요청(new ProductRequest(name, price));
    }

    public static ExtractableResponse<Response> 상품_생성_요청(ProductRequest product) {
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

    public static void 상품_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedLineIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultLineIds = response.jsonPath().getList(".", ProductResponse.class).stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultLineIds).containsAll(expectedLineIds);
    }
}
