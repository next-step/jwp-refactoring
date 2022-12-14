package kitchenpos.menu.domian;

import kitchenpos.JpaEntityTest;
import kitchenpos.menu.domain.Product;
import kitchenpos.menu.repository.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("상품 관련 도메인 테스트")
public class ProductTest extends JpaEntityTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품 생성 테스트")
    @Test
    void create() {
        // given
        final Product product = new Product("후라이드", new BigDecimal(10_000));

        // when
        final Product savedProduct = productRepository.save(product);

        // then
        assertThat(savedProduct).isNotNull();
    }

    @DisplayName("상품 생성 테스트")
    @Test
    void createProductExceptionNegativePrice() {
        // when / then
        assertThatThrownBy(() -> new Product("후라이드", new BigDecimal(-2_000)))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
