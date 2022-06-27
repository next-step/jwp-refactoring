package kitchenpos.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("상품")
public class ProductAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * - 상품관리
     * given 등록할 상품을 생성한다.
     * when 상품을 등록한다.
     * then 상품이 등록됨
     * when 상품목록을 조회한다.
     * then 등록한 상품이 조회 된다.
     * */
    @Test
    @DisplayName("상품을 등록 관리")
    void manageProduct() {
        //givne
        Product 생맥주 = new Product("생맥주", BigDecimal.valueOf(2000));

        //when
        ExtractableResponse<Response> createProduct = 상품_등록을_요청(생맥주);
        //then
        상품이_등록됨(생맥주, createProduct);

        //when
        final ExtractableResponse<Response> searchProducts = 상품_목록을_조회();
        //then
        등록한_상품이_조회됨(생맥주, searchProducts);
    }


    private ExtractableResponse<Response> 상품_목록을_조회() {
        return RestAssured.given().log().all()
                .when().get("/api/products")
                .then()
                .log().all()
                .extract();
    }


    private ExtractableResponse<Response> 상품_등록을_요청(Product product) {
        return RestAssured.given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then()
                .log().all()
                .extract();
    }

    private void 상품이_등록됨(Product product, ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header(HttpHeaders.LOCATION)).isNotEmpty();
        assertThat(response.jsonPath().getString("name")).isEqualTo(product.getName());
    }

    private void 등록한_상품이_조회됨(Product product, ExtractableResponse<Response> searchProducts) {
        assertThat(searchProducts.jsonPath().getList(".", Product.class))
                .extracting("name")
                .contains(product.getName());

    }

}
