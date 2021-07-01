package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class ProductTest {

    @Test
    void multiplyPrice() {
        Product product = new Product(new Name("Hello"), new Price(3));
        Quantity quantity = new Quantity(10L);

        assertThat(product.multiplyPrice(quantity)).isEqualTo(new Price(30));
    }
}