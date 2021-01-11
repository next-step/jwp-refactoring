package kitchenpos.product;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 관리한다.")
    @Test
    void manage() {
        // when
        Map<String, String> params = new HashMap<>();
        params.put("name", "강정치킨");
        params.put("price", "17000");
        ExtractableResponse<Response> createResponse = 상품_생성_요청(params);

        // then
        상품_생성됨(createResponse);

        // when
        ExtractableResponse<Response> findResponse = 상품_목록_조회_요청();

        // then
        상품_목록_응답됨(findResponse);
        상품_목록_포함됨(findResponse, Arrays.asList(createResponse));
    }


    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_생성_요청(final Map<String, String> params) {
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static void 상품_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_목록_응답됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(final ExtractableResponse<Response> response,
                                 final List<ExtractableResponse<Response>> createResponses) {
        List<Long> expectedProductIds = createResponses.stream()
                .map(create -> Long.parseLong(create.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> actualIds = response.jsonPath().getList("id", Long.class);
        assertThat(actualIds).containsAll(expectedProductIds);
    }
}
