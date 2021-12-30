package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import kitchenpos.common.vo.Price;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    void save() {
        Product product = new Product("치킨", Price.valueOf(BigDecimal.valueOf(21000)));
        Product saveProduct = productRepository.save(product);
        Product findProduct = productRepository.findById(saveProduct.getId())
            .orElseThrow(() -> new IllegalStateException());
        assertThat(saveProduct).isEqualTo(findProduct);
    }
}
