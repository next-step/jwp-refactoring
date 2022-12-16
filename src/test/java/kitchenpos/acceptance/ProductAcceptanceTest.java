package kitchenpos.acceptance;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.Product;
import kitchenpos.fixture.ProductFixture;
import kitchenpos.rest.ProductRestAssured;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class ProductAcceptanceTest extends BaseAcceptanceTest {

    @Test
    void 신규_상품_정보가_주어진_경우_상품_등록_요청시_요청에_성공한다() {
        // given
        String 상품명 = "강정치킨";
        BigDecimal 상품가격 = BigDecimal.valueOf(12_000);

        // when
        ExtractableResponse<Response> response = ProductRestAssured.상품_등록_요청(상품명, 상품가격);

        // then
        신규_상품_등록됨(response, 상품명, 상품가격);
    }

    @Test
    void 상품_목록_조회_요청시_요청에_성공한다() {
        // given
        ProductRestAssured.상품_등록됨(ProductFixture.후라이드);

        // when
        ExtractableResponse<Response> response = ProductRestAssured.상품_목록_조회_요청();

        // then
        상품_목록_조회됨(response);
    }

    private void 신규_상품_등록됨(ExtractableResponse<Response> response, String name, BigDecimal price) {
        Product product = response.as(Product.class);
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(product.getName()).isEqualTo(name),
                () -> assertThat(product.getPrice().floatValue()).isEqualTo(price.floatValue())
        );
    }

    private void 상품_목록_조회됨(ExtractableResponse<Response> response) {
        List<Product> products = response.as(new TypeRef<List<Product>>() {});
        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
                () -> assertThat(products).hasSize(1)
        );
    }
}
