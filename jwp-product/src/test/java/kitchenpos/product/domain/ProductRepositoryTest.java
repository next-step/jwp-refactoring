package kitchenpos.product.domain;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {
    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 등록한다.")
    @Test
    void saveProduct() {
        final Product product = Product.of("소곱창", 100000);

        final Product actual = productRepository.save(product);

        assertThat(actual).isEqualTo(product);
    }

    @DisplayName("등록한 상품을 조회한다.")
    @Test
    void findProduct() {
        final Product product = Product.of("소곱창", 100000);
        final Product persistProduct = productRepository.save(product);

        final Product actual = productRepository.findById(persistProduct.getId()).get();

        assertThat(actual).isEqualTo(persistProduct);
    }
}
