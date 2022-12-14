package kitchenpos.acceptence;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class ProductRestControllerTest extends AcceptanceSupport {
    private Product 스테이크;
    private Product 감튀;

    @BeforeEach
    public void setUp() {
        super.setUp();
        스테이크 = new Product(1L, "스테이크", BigDecimal.valueOf(1_000));
        감튀 = new Product(2L, "감튀", BigDecimal.valueOf(2_000));
    }

    @Test
    void 상품을_등록_할_수_있다() {
        // when
        ExtractableResponse<Response> response = 상품을_등록한다(스테이크);

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.CREATED);
    }

    @Test
    void 상품_목록을_조회_할_수_있다() {
        // given
        스테이크 = 상품을_등록한다(this.스테이크).as(Product.class);
        감튀 = 상품을_등록한다(this.감튀).as(Product.class);

        // when
        ExtractableResponse<Response> response = 상품_리스트를_조회해온다();

        // then
        상태값을_비교한다(response.statusCode(), HttpStatus.OK);
        상품_리스트를_비교한다(response, Arrays.asList(스테이크.getId(), 감튀.getId()));

    }

    public ExtractableResponse<Response> 상품을_등록한다(Product product) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(product)
                .when().post("/api/products")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 상품_리스트를_조회해온다() {
        return RestAssured
                .given().log().all()
                .when().get("/api/products")
                .then().log().all()
                .extract();
    }

    private void 상태값을_비교한다(int statusCode, HttpStatus created) {
        assertThat(statusCode).isEqualTo(created.value());
    }

    private void 상품_리스트를_비교한다(ExtractableResponse<Response> response, List<Long> createId) {
        List<Product> result = response.jsonPath().getList(".", Product.class);
        List<Long> responseId = result.stream().map(Product::getId).collect(Collectors.toList());
        assertThat(responseId).containsAll(createId);
    }
}
