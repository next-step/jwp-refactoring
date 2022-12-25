package kitchenpos.product;

import static kitchenpos.product.ProductFixture.상품_등록;
import static kitchenpos.product.ProductFixture.상품_목록_조회;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.dto.ProductResponse;
import org.assertj.core.util.BigDecimalComparator;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class ProductAcceptanceTest extends AcceptanceTest {

    /*
    Feature: 상품을 관리 한다.

      Scenario: 상품 관리
        When 상품 등록
        Then 상품 등록됨
        When 상품 목록 조회
        Then 상품 목록 조회됨
        When 가격 없이 상품 등록
        Then 에러 발생
        When 가격이 음수인 상태로 상품 등록
        Then 에러 발생
     */

    @Test
    void 상품_관리() {
        //when
        ExtractableResponse<Response> createResponse = 상품_등록("강정치킨", new BigDecimal(17_000));

        //then
        assertThat(createResponse.statusCode()).isEqualTo(HttpStatus.CREATED.value());

        //when
        List<ProductResponse> products = 상품_목록_조회().jsonPath().getList(".", ProductResponse.class);

        //then
        assertThat(products)
            .hasSize(1)
            .extracting("name", "price")
            .usingComparatorForType(BigDecimalComparator.BIG_DECIMAL_COMPARATOR, BigDecimal.class)
            .containsExactly(tuple("강정치킨", new BigDecimal("17000.0")));
    }

    @Test
    void 상품등록시_상품명을_입력안하면_에러발생() {
        ExtractableResponse<Response> response = 상품_등록(null, new BigDecimal(17_000));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 상품등록시_가격을_입력안하면_에러발생() {
        ExtractableResponse<Response> response = 상품_등록("강정치킨", null);

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    @Test
    void 상품등록시_가격이_0원이상이_아니면_에러발생() {
        ExtractableResponse<Response> response = 상품_등록("강정치킨", new BigDecimal(-1));

        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
