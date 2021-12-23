package kitchenpos.product;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("상품 관련 기능")
class ProductServiceTest extends AcceptanceTest {

    @Autowired
    private ProductService productService;

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void createProduct() {
        // given
        final Product product = new Product("후라이드", BigDecimal.valueOf(17000));

        // when
        Product savedProduct = productService.create(product);

        // then
        assertAll(
                () -> assertThat(savedProduct.getId()).isNotNull(),
                () -> assertThat(savedProduct.getName()).isEqualTo("후라이드"),
                () -> assertThat(savedProduct.getPrice()).isEqualByComparingTo(BigDecimal.valueOf(17000))
        );
    }

    @Test
    @DisplayName("상품 목록을 조회할 수 있다.")
    void findProduct() {
        // given
        productService.create(new Product("후라이드", BigDecimal.valueOf(17000)));

        // when
        List<Product> findByProducts = productService.list();

        // then
        assertThat(findByProducts.size()).isOne();
    }
}
