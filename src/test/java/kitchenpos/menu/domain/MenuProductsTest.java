package kitchenpos.menu.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    @Test
    void add() {
        MenuProducts menuProducts = new MenuProducts();
        assertThat(menuProducts).isNotNull();
    }

}
