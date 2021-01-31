package kitchenpos.product;

import kitchenpos.product.Product;
import kitchenpos.product.ProductRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;

@DataJpaTest
public class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @DisplayName("상품을 생성한다.")
    @Test
    public void createProduct() {
        Product expected = new Product("케이크", new BigDecimal(1000));
        Product actual = productRepository.save(expected);
        assertThat(expected.getName()).isEqualTo(actual.getName());
        assertThat(expected.getPrice()).isEqualTo(actual.getPrice());
        assertThat(actual.getId()).isNotNull();
    }


}
