package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void findAllByIds() {
        // given
        final Product product1 = productRepository.save(new Product("name", new BigDecimal(100)));
        final Product product2 = productRepository.save(new Product("name2", new BigDecimal(200)));

        // when
        final List<Product> products = productRepository.findAllByIdIn(
            Arrays.asList(product1.getId(), product2.getId()));

        // then
        assertThat(products).containsExactly(product1, product2);
    }
}
