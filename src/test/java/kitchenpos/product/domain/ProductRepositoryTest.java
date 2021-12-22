package kitchenpos.product.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    ProductDao productDao;

    @Test
    void save() {
        Product product = new Product("치킨", BigDecimal.valueOf(21000));
        Product saveProduct = productDao.save(product);
        Product findProduct = productDao.findById(saveProduct.getId())
            .orElseThrow(() -> new IllegalStateException());
        assertThat(saveProduct).isEqualTo(findProduct);
    }
}
