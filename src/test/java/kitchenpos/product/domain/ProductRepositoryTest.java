package kitchenpos.product.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @BeforeEach
    void setUp() {

    }

    @Test
    void save() {
        Product persistProduct = productRepository.save(new Product(1L, "1번상품", BigDecimal.valueOf(10000)));

        assertThat(persistProduct.getId()).isEqualTo(1L);
        assertThat(persistProduct.getName()).isEqualTo("1번상품");
        assertThat(persistProduct.getPrice()).isEqualTo(BigDecimal.valueOf(10000));
    }

}
