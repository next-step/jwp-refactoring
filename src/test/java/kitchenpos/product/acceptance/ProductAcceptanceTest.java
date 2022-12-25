package kitchenpos.product.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.DynamicTest.dynamicTest;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 관련 기능 테스트")
    @TestFactory
    Stream<DynamicNode> product() {
        return Stream.of(
            dynamicTest("상품을 등록한다.", () -> {
                ExtractableResponse<Response> response = 상품_생성_요청("뿌링클치킨", BigDecimal.valueOf(15_000L));

                상품_생성됨(response);
            }),
            dynamicTest("가격이 0미만인 상품을 등록한다.", () -> {
                ExtractableResponse<Response> response = 상품_생성_요청("파닭치킨", BigDecimal.valueOf(-1L));

                상품_생성_실패됨(response);
            }),
            dynamicTest("이름이 없는 상품을 등록한다.", () -> {
                ExtractableResponse<Response> response = 상품_생성_요청(null, BigDecimal.valueOf(15_000L));

                상품_생성_실패됨(response);
            }),
            dynamicTest("상품 목록을 조회한다.", () -> {
                ExtractableResponse<Response> response = 상품_목록_조회_요청();

                상품_목록_응답됨(response);
                상품_목록_확인됨(response, "뿌링클치킨");
            })
        );
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, BigDecimal price) {
        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("name", name);
        requestBody.put("price", price);

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(requestBody)
            .when().post("/api/products")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssured
            .given().log().all()
            .when().get("/api/products")
            .then().log().all()
            .extract();
    }

    public static void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 상품_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    public static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_확인됨(ExtractableResponse<Response> response, String... names) {
        List<ProductResponse> productResponses = response.jsonPath().getList(".", ProductResponse.class);
        List<String> productNames = productResponses
            .stream()
            .map(ProductResponse::getName)
            .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }
}
