package kitchenpos.tobe.menu.domain;

import kitchenpos.tobe.product.domain.Product;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @Test
    void create() {
        MenuProduct menuProduct = MenuProduct.builder()
                .id(1L)
                .product(new Product("강정치킨", BigDecimal.valueOf(16000)))
                .menu(new Menu())
                .quantity(5L)
                .build();

        assertThat(menuProduct).isNotNull();
    }
}
