package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class ProductTest {

    @Test
    void multiplyPrice() {
        Product product = new Product(null, new Price(3));

        assertThat(product.multiplyPrice(10)).isEqualTo(new Price(30));
    }
}