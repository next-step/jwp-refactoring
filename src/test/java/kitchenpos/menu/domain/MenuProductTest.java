package kitchenpos.menu.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductTest {

    @Test
    void create() {
        MenuProduct menuProduct = MenuProduct.builder()
                .id(1L)
                .productId(1L)
                .menu(new Menu())
                .quantity(5L)
                .build();

        assertThat(menuProduct).isNotNull();
    }
}
