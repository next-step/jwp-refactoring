package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("상품 인수 테스트")
public class ProductAcceptanceTest extends AcceptanceTest {

    @DisplayName("상품 생성에 성공한다.")
    @Test
    void createProduct() {
        //when
        final ExtractableResponse<Response> 결과 = 상품_생성_요청("후라이드", 16_000);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(결과.jsonPath().getString("name")).isEqualTo("후라이드");
    }

    @DisplayName("상품의 가격이 음수일 경우 상품 생성에 실패한다.")
    @Test
    void createProductFailedWhenPriceIsMinus() {
        //when
        final ExtractableResponse<Response> 결과 = 상품_생성_요청("후라이드", -15_000);

        //then
        assertThat(결과.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }

    @DisplayName("상품을 조회할 수 있다.")
    @Test
    void findProducts() {
        //given
        상품_생성_요청("후라이드", 16_000);
        상품_생성_요청("양념", 17_000);

        //when
        final ExtractableResponse<Response> 상품_목록 = 상품_목록_조회();

        //then
        assertThat(상품_목록.jsonPath().getList(".").size()).isEqualTo(2);
    }

    public static ExtractableResponse<Response> 상품_생성_요청(final String name, final int price) {
        final Product product = new Product(name, BigDecimal.valueOf(price));
        return RestAssured.given().log().all()
                .contentType(ContentType.JSON)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 상품_목록_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }
}
