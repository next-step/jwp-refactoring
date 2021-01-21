package kitchenpos.product.application;

import kitchenpos.product.ProductService;
import kitchenpos.product.dto.ProductRequest;
import kitchenpos.product.dto.ProductResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 비즈니스 로직을 처리하는 서비스 테스트")
class ProductServiceTest extends BaseTest {
    private static final String PRODUCT_NAME = "후라이드";
    private static final long PRICE = 19_000L;

    @Autowired
    private ProductService productService;

    private ProductRequest productRequest;

    @BeforeEach
    void setUp() {
        productRequest = new ProductRequest(PRODUCT_NAME, PRICE);
    }

    @DisplayName("상품을 생성한다.")
    @Test
    void 상품_생성() {
        final ProductResponse savedProduct = productService.create(productRequest);

        assertThat(savedProduct.getId()).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo(PRODUCT_NAME);
        assertThat(savedProduct.getPrice()).isEqualTo(PRICE);
    }

    @DisplayName("상품 가격이 음수인 경우 상품을 생성할 수 없다.")
    @Test
    void 가격이_음수인_경우_상품_생성() {
        productRequest.setPrice(-1);
        assertThatThrownBy(() -> productService.create(productRequest))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("상품을 조회한다.")
    @Test
    void 상품_조회() {
        productService.create(productRequest);

        final List<ProductResponse> responseProducts = productService.list();

        assertThat(responseProducts.get(0).getId()).isNotNull();
        assertThat(responseProducts.size()).isGreaterThan(0);
    }
}
