package kitchenpos.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MenuProductTest {

    @Test
    void getAmount() {
        MenuProduct menuProduct = new MenuProduct(null, new Product(null, new Price(2)), 10);

        assertThat(menuProduct.getAmount()).isEqualTo(new Price(20));
    }
}