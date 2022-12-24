package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class ProductRestControllerTest extends AcceptanceSupport {
    private ProductRequest 스테이크;
    private ProductRequest 감튀;

    @BeforeEach
    public void setUp() {
        super.setUp();
        스테이크 = new ProductRequest("스테이크", BigDecimal.valueOf(1_000));
        감튀 = new ProductRequest("감튀", BigDecimal.valueOf(2_000));
    }

    @Test
    void 상품을_등록_할_수_있다() {
        // when
        ExtractableResponse<Response> response = 상품을_등록한다(스테이크);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    void 상품_목록을_조회_할_수_있다() {
        // given
        ProductResponse responseA = 상품을_등록한다(스테이크).as(ProductResponse.class);
        ProductResponse responseB = 상품을_등록한다(감튀).as(ProductResponse.class);

        // when
        ExtractableResponse<Response> response = 상품_리스트를_조회해온다();

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        상품_리스트를_비교한다(response, Arrays.asList(responseA.getId(), responseB.getId()));

    }

    public static ExtractableResponse<Response> 상품을_등록한다(ProductRequest product) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_리스트를_조회해온다() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    private void 상태값을_비교한다(int statusCode, HttpStatus created) {
        assertThat(statusCode).isEqualTo(created.value());
    }

    private void 상품_리스트를_비교한다(ExtractableResponse<Response> response, List<Long> getId) {
        List<ProductResponse> result = response.jsonPath().getList(".", ProductResponse.class);
        List<Long> responseId = result.stream().map(ProductResponse::getId).collect(Collectors.toList());
        assertThat(responseId).containsAll(getId);
    }
}
