package kitchenpos.product;

import kitchenpos.Product;
import kitchenpos.ProductRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void add() {
        productRepository.save(new Product("닭강정", new BigDecimal(7000)));
        Optional<Product> product = productRepository.findById(1L);
        assertThat(product.get().getName()).isEqualTo("닭강정");
    }

}
