package kitchenpos.ui;

import static kitchenpos.utils.AcceptanceTestUtil.get;
import static kitchenpos.utils.AcceptanceTestUtil.post;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.math.BigDecimal;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.product.domain.Name;
import kitchenpos.product.domain.Product;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
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
        ExtractableResponse<Response> 상품생성_응답 = 상품_생성(new ProductRequest("양념치킨", 18000));

        // then
        상품_생성됨(상품생성_응답);
    }

    @DisplayName("상품 목록을 조회한다")
    @Test
    void readProducts() {
        // given
        상품_생성(new ProductRequest("양념치킨", 18000));
        상품_생성(new ProductRequest("후라이드치킨", 17000));

        // when
        ExtractableResponse<Response> 상품목록_조회_응답 = 상품목록_조회();

        // then
        상품목록_조회됨(상품목록_조회_응답);
    }

    private ExtractableResponse<Response> 상품_생성(ProductRequest product) {
        return post("/api/products", product);
    }

    private void 상품_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
        Product product = response.as(Product.class);
        assertThat(product.getName()).isEqualTo(Name.of("양념치킨"));
        assertThat(product.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(18000));
    }

    private ExtractableResponse<Response> 상품목록_조회() {
        return get("/api/products");
    }

    private void 상품목록_조회됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
        List<ProductResponse> products = Lists.newArrayList(response.as(ProductResponse[].class));
        assertThat(products).hasSize(2);
        assertThat(products).extracting(ProductResponse::getName)
            .contains("후라이드치킨", "양념치킨");
    }

}