package kitchenpos.product;


import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static kitchenpos.product.fixtures.ProductFixtures.메뉴등록요청;
import static kitchenpos.product.fixtures.ProductFixtures.양념치킨요청;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * packageName : kitchenpos.acceptance
 * fileName : ProductAcceptanceTest
 * author : haedoang
 * date : 2021-12-21
 * description :
 */
@DisplayName("상품 인수테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @Test
    @DisplayName("상품을 등록한다.")
    public void create() throws Exception {
        //when
        final ExtractableResponse<Response> response = 상품_등록_요청함(양념치킨요청());

        //then
        상품_등록됨(response);
    }

    @Test
    @DisplayName("상품 금액이 0보다 작은 경우 등록할 수 없다.")
    public void createFail() throws Exception {
        //given
        final ProductRequest 금액이_0보다_작은경우 = ProductRequest.of("양념치킨", new BigDecimal(-1));

        //when
        final ExtractableResponse<Response> response = 상품_등록_요청함(금액이_0보다_작은경우);

        //then
        상품_등록_실패됨(response);
    }

    @Test
    @DisplayName("상품 금액이 null인 경우 등록할 수 없다.")
    public void createFailNull() {
        //given
        final ProductRequest 금액이_널값인_경우 = ProductRequest.of("양념치킨", null);

        //when
        final ExtractableResponse<Response> response = 상품_등록_요청함(금액이_널값인_경우);

        //then
        상품_등록_실패됨(response);
    }

    @Test
    @DisplayName("상품을 조회한다.")
    public void list() throws Exception {
        //given
        상품_등록되어_있음(메뉴등록요청("양념치킨", new BigDecimal(18000)));
        상품_등록되어_있음(메뉴등록요청("후라이드치킨", new BigDecimal(16000)));

        //when
        final ExtractableResponse<Response> response = 상품_리스트_조회_요청함();

        // then
        상품_리스트_조회됨(response);
    }

    private ExtractableResponse<Response> 상품_리스트_조회_요청함() {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when()
                .get("/api/products")
                .then().log().all()
                .extract();
    }

    public static ProductResponse 상품_등록되어_있음(ProductRequest request) {
        final ExtractableResponse<Response> response = 상품_등록_요청함(request);
        return response.jsonPath().getObject("", ProductResponse.class);
    }

    public static ExtractableResponse<Response> 상품_등록_요청함(ProductRequest request) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when()
                .post("/api/products")
                .then().log().all()
                .extract();
    }

    private void 상품_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotNull();
    }

    private void 상품_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    private void 상품_리스트_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(
                response.jsonPath()
                        .getList("", ProductResponse.class)
                        .size()
        ).isGreaterThanOrEqualTo(0);
    }
}
