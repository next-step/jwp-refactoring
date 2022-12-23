package kitchenpos;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품 관련 기능")
public class ProductAcceptanceTest extends AcceptanceTest {

    /**
     * Feature: 상품 관련 기능
     *  Scenario: 상품 추가 조회
     *      when: 상품을 생성
     *      then: 상품이 생성됨
     *      when: 상품의 이름 없이 생성
     *      then: 상품이 실패됨           
     *      when: 상품의 가격 없이 생성
     *      then: 상품이 실패됨      
     *      when: 상품의 가격을 음수로 생성한다
     *      then: 상품이 실패됨
     *      when: 상품을 조회한다
     *      then: 상품이 조회됨
     */

    @DisplayName("상품 추가 조회")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> 페페로니 = 상품_생성_요청("페페로니피자", BigDecimal.valueOf(15_000L));
        // then
        상품_생성됨(페페로니);

        // when
        ExtractableResponse<Response> 이름없음 = 상품_생성_요청(null, BigDecimal.valueOf(15_000L));
        // then
        상품_생성_실패됨(이름없음);

        // when
        ExtractableResponse<Response> 가격없음 = 상품_생성_요청("치즈피자", null);
        // then
        상품_생성_실패됨(가격없음);
        
        // when
        ExtractableResponse<Response> 가격음수 = 상품_생성_요청("치즈피자", BigDecimal.valueOf(-1L));
        // then
        상품_생성_실패됨(가격음수);

        // when
        ExtractableResponse<Response> response = 상품_목록_조회_요청();
        // then
        상품_목록_확인됨(response, "페페로니피자");
        
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

    public static void 상품_목록_확인됨(ExtractableResponse<Response> response, String... names) {
        List<Product> products = response.jsonPath().getList(".", Product.class);
        List<String> productNames = products
                .stream()
                .map(Product::getName)
                .collect(Collectors.toList());
        assertThat(productNames).containsExactly(names);
    }
}
