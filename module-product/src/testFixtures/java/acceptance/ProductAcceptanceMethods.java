package acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.stream.Collectors;

import static acceptance.RestAssuredMethods.*;
import static acceptance.RestAssuredMethods.post;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceMethods {
    public static ExtractableResponse<Response> 상품_등록_요청(ProductRequest params) {
        return post("/api/products", params);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return get("/api/products");
    }

    public static void 상품_등록됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 상품_목록_응답됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
    }

    public static void 상품_목록_포함됨(ExtractableResponse<Response> response, List<ExtractableResponse<Response>> createdResponses) {
        List<Long> expectedProductIds = createdResponses.stream()
                .map(it -> Long.parseLong(it.header("Location").split("/")[3]))
                .collect(Collectors.toList());

        List<Long> resultProductIds = response.jsonPath().getList(".", ProductResponse.class).stream()
                .map(ProductResponse::getId)
                .collect(Collectors.toList());

        assertThat(resultProductIds).containsAll(expectedProductIds);
    }

    public static void 상품_등록_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    public static ExtractableResponse<Response> 상품_등록되어_있음(String name, int price) {
        return 상품_등록_요청(ProductRequest.of(name, price));
    }
}
