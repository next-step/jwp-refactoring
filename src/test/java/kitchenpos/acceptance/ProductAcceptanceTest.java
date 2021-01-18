package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 관리한다.")
    @Test
    void manageProduct() {
        // given
        Product 강정치킨 = new Product("강정치킨", BigDecimal.valueOf(17000));

        // when
        ExtractableResponse<Response> createResponse = 상품_생성_요청(강정치킨);

        // then
        상품_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(findResponse);
        상품_목록_포함됨(findResponse, Arrays.asList(createResponse));
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

    public static ExtractableResponse<Response> 상품_등록되어_있음(Product product) {
        return 상품_생성_요청(product);
    }

    private void 상품_생성됨(ExtractableResponse<Response> createResponse) {
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .accept(MediaType.APPLICATION_JSON_VALUE)
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    private void 상품_목록_응답됨(ExtractableResponse<Response> findResponse) {
        assertThat(findResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    private void 상품_목록_포함됨(ExtractableResponse<Response> findResponse, List<ExtractableResponse<Response>> createResponse) {
        List<Long> createProductIds = createResponse.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> findProductIds = findResponse.jsonPath().getList("id", Long.class);
        assertThat(findProductIds).containsAll(createProductIds);
    }
}
