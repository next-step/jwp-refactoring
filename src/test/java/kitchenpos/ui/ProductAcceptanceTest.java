package kitchenpos.ui;

import static kitchenpos.utils.AcceptanceTestUtil.get;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

class ProductAcceptanceTest extends AcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    @DisplayName("상품을 생성한다")
    @Test
    void createProduct() {
        // when
        ExtractableResponse<Response> 상품생성_응답 = 상품_생성(Product.of("양념치킨", 18000));

        // then
        상품_생성됨(상품생성_응답);
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void readProducts() {
        // when
        ExtractableResponse<Response> 상품목록_조회_응답 = 상품목록_조회();

        // then
        상품목록_조회됨(상품목록_조회_응답);
    }

    private ExtractableResponse<Response> 상품_생성(Product product) {
        return post("/api/products", product);
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        Product product = response.as(Product.class);
        assertThat(product.getName()).isEqualTo("양념치킨");
        assertThat(product.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(18000));
    }

    private ExtractableResponse<Response> 상품목록_조회() {
        return get("/api/products");
    }

    private void 상품목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<Product> products = Lists.newArrayList(response.as(Product[].class));
        assertThat(products).hasSize(6);
        assertThat(products).extracting(Product::getName)
            .contains("후라이드", "양념치킨", "반반치킨", "통구이", "간장치킨", "순살치킨");
    }

}