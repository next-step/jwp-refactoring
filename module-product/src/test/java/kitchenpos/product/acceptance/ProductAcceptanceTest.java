package kitchenpos.product.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.product.dto.ProductResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.product.generator.ProductGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("0원 미만인 상품을 생성하면 예외가 발생해야 한다")
    @Test
    void createProductByMinusPriceTest() {
        // when
        ExtractableResponse<Response> 상품_생성_결과 = 상품_생성_API_요청("상품", -1);

        // then
        상품_생성_실패됨(상품_생성_결과);
    }

    @DisplayName("정상 상태의 상품을 생성하면 정상 저장 되어야 한다")
    @Test
    void createProductTest() {
        // given
        String 상품_이름 = "상품";
        int 상품_가격 = 1_000;
        ExtractableResponse<Response> 상품_생성_결과 = 상품_생성_API_요청(상품_이름, 상품_가격);

        // then
        상품_생성됨(상품_생성_결과, 상품_이름, 상품_가격);
    }

    @DisplayName("상품 목록을 조회하면 정상 조회되어야 한다")
    @Test
    void findAllProductTest() {
        // given
        List<String> 상품_이름들 = Arrays.asList("상품 1", "상품 2", "상품 3", "상품 4", "상품 5");
        상품_이름들.forEach(name -> 상품_생성_API_요청(name, 1_000));

        // when
        ExtractableResponse<Response> 상품_조회_결과 = 상품_목록_API_요청();

        // then
        상품_목록_조회됨(상품_조회_결과, 상품_이름들);
    }

    @DisplayName("없는 상품을 조회하면 예외가 발생해야 한다")
    @Test
    void findProductByNotSavedTest() {
        // when
        ExtractableResponse<Response> 상품_조회_결과 = 상품_조회_API_요청(-1L);

        // then
        상품_조회_실패됨(상품_조회_결과);
    }

    @DisplayName("정상 상품을 조회하면 정상 조회되어야 한다")
    @Test
    void findProductTest() {
        // given
        String 상품_이름 = "상품";
        int 상품_가격 = 1_000;
        Long 상품_아이디 = 상품_생성_API_요청(상품_이름, 상품_가격).body().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> 상품_조회_결과 = 상품_조회_API_요청(상품_아이디);

        // then
        상품_조회_성공됨(상품_조회_결과, 상품_아이디, 상품_이름, 상품_가격);
    }

    void 상품_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 상품_생성됨(ExtractableResponse<Response> response, String expectedName, int expectedPrice) {
        ProductResponse product = response.as(ProductResponse.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(product.getId()).isNotNull();
        assertThat(product.getName()).isEqualTo(expectedName);
        assertThat(product.getPrice()).isEqualTo(new BigDecimal(expectedPrice));
    }

    void 상품_목록_조회됨(ExtractableResponse<Response> response, List<String> expectedNames) {
        List<String> names = response.body().jsonPath().getList("name", String.class);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_OK);
        assertThat(names).containsAll(expectedNames);
    }

    void 상품_조회_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    private void 상품_조회_성공됨(ExtractableResponse<Response> response, Long id, String name, int price) {
        ProductResponse product = response.as(ProductResponse.class);

        assertThat(product.getId()).isNotNull();
        assertThat(product.getId()).isEqualTo(id);
        assertThat(product.getName()).isEqualTo(name);
        assertThat(product.getPrice().compareTo(new BigDecimal(price))).isEqualTo(0);
    }
}
