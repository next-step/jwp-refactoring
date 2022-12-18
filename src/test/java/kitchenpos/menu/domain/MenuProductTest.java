package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuProductTest {

    @DisplayName("메뉴상품을 추가할 수 있다")
    @Test
    public void returnMenuProducts() {
        MenuProduct menuProduct1 = MenuProduct.builder().seq(1l).build();
        MenuProduct menuProduct2 = MenuProduct.builder().seq(2l).build();
        MenuProducts menuProducts = new MenuProducts();

        menuProducts.addMenuProduct(menuProduct1);
        menuProducts.addMenuProduct(menuProduct2);

        assertThat(menuProducts.getMenuProducts()).contains(menuProduct1, menuProduct2);
    }
}

