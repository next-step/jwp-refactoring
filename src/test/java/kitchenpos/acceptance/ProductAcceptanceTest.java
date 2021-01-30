package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 등록 및 조회")
    @Test
    void selectProduct() {
        // given
        ProductRequest product = new ProductRequest("상품", new BigDecimal(100));
        상품_생성_요청(product);

        // when
        ExtractableResponse<Response> selectResponse = 상품_목록_조회_요청();

        // then
        상품_응답_확인(selectResponse);
        상품_목록_포함(selectResponse, Collections.singletonList(product));
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

    public static void 상품_응답_확인(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함(ExtractableResponse<Response> findResponse, List<ProductRequest> products) {
        List<String> createProductIds = products.stream().map(ProductRequest::getName).collect(toList());

        List<String> findProductIds = findResponse.jsonPath().getList("name", String.class);
        assertThat(findProductIds).containsAll(createProductIds);
    }
}
