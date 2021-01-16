package kitchenpos.product.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품을 등록할 수 있다.")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> response = 상품_등록_요청됨("강정치킨", 17000);

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private ExtractableResponse<Response> 상품_등록_요청됨(String name, int price) {
        Map<String, String> params = new HashMap<>();
        params.put("name", name);
        params.put("price", price + "");
        return RestAssured.given().log().all().
                body(params).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().
                post("/api/products").
                then().
                log().all().
                extract();
    }

    @DisplayName("상품의 목록을 조회할 수 있다.")
    @Test
    void getProductFindAll() {
        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청됨();

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        assertThat(findProductNames(response))
                .containsExactly("후라이드치킨", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }

    private ExtractableResponse<Response> 상품_목록_조회_요청됨() {
        return RestAssured.given().log().all().
                when().
                get("/api/products").
                then().
                log().all().
                extract();
    }

    private List<String> findProductNames(ExtractableResponse<Response> response) {
        return response.jsonPath().getList(".", ProductResponse.class).stream()
                .map(ProductResponse::getName)
                .collect(Collectors.toList());
    }

    @DisplayName("상품 가격은 0원 이상이거나 빈값이 아니어야 한다.")
    @Test
    void failProductIllegalPrice() {
        ExtractableResponse<Response> response = 상품_등록_요청됨("강정치킨", -1);
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
