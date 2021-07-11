package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;

import org.junit.jupiter.api.Test;

class MenuProductsTest {

    @Test
    void totalPrice() {
        // given
        Menu menu = new Menu();
        final MenuProduct menuProduct1 = new MenuProduct(menu, new Product("name", new BigDecimal(100)), 2);
        final MenuProduct menuProduct2 = new MenuProduct(menu, new Product("name2", new BigDecimal(100)), 3);
        final MenuProducts menuProducts = new MenuProducts(Arrays.asList(menuProduct1, menuProduct2));

        // when
        final BigDecimal actual = menuProducts.totalPrice();

        // then
        assertThat(actual).isEqualTo(BigDecimal.valueOf(500));
    }
}
