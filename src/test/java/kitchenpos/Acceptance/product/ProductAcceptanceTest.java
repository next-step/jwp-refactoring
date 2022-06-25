package kitchenpos.Acceptance.product;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.Acceptance.utils.RestAssuredRequest;
import kitchenpos.product.domain.Product;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {
    private static final String PATH = "/api/products";

    @DisplayName("0원 미만인 상품을 생성하면 예외가 발생해야 한다")
    @Test
    void createProductByMinusPriceTest() {
        // when
        ExtractableResponse<Response> 상품_생성_결과 = 상품_생성_요청("상품", -1);

        // then
        상품_생성_실패됨(상품_생성_결과);
    }

    @DisplayName("정상 상태의 상품을 생성하면 정상 저장 되어야 한다")
    @Test
    void createProductTest() {
        // given
        String 상품_이름 = "상품";
        int 상품_가격 = 1_000;
        ExtractableResponse<Response> 상품_생성_결과 = 상품_생성_요청(상품_이름, 상품_가격);

        // then
        상품_생성됨(상품_생성_결과, 상품_이름, 상품_가격);
    }

    @DisplayName("상품 목록을 조회하면 정상 조회되어야 한다")
    @Test
    void findAllProductTest() {
        // given
        List<String> 상품_이름들 = Arrays.asList("상품 1", "상품 2", "상품 3", "상품 4", "상품 5");
        상품_이름들.forEach(name -> 상품_생성_요청(name, 1_000));

        // when
        ExtractableResponse<Response> 상품_조회_결과 = 상품_목록_조회_요청();

        // then
        상품_목록_조회됨(상품_조회_결과, 상품_이름들);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(String name, int price) {
        Map<String, Object> body = new HashMap<>();

        body.put("name", name);
        body.put("price", price);

        return RestAssuredRequest.postRequest(PATH, Collections.emptyMap(), body);
    }

    public static ExtractableResponse<Response> 상품_목록_조회_요청() {
        return RestAssuredRequest.getRequest(PATH, Collections.emptyMap());
    }

    void 상품_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
        // 추후 상태 코드 정의시 변경 필요
    }

    void 상품_생성됨(ExtractableResponse<Response> response, String expectedName, int expectedPrice) {
        Product product = response.as(Product.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo(expectedName);
        assertThat(product.getPrice().compareTo(new BigDecimal(expectedPrice))).isEqualTo(0);
    }

    void 상품_목록_조회됨(ExtractableResponse<Response> response, List<String> expectedNames) {
        List<String> names = response.body().jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(names).containsAll(expectedNames);
    }
}
