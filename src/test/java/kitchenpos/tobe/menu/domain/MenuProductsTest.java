package kitchenpos.tobe.menu.domain;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    @Test
    void add() {
        MenuProducts menuProducts = new MenuProducts();

        menuProducts.add(new MenuProduct());

        assertThat(menuProducts.getMenuProducts()).isNotEmpty();
    }

}
