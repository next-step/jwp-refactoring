package kitchenpos.domain.menu;

import static org.assertj.core.api.Assertions.assertThat;

import kitchenpos.domain.product.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {
    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        Product product = Product.of("허니콤보", 19_000L);
        MenuProduct menuProduct = MenuProduct.createMenuProduct(product.getId(), 1L);
        MenuProducts menuProducts = MenuProducts.createMenuProducts(Lists.list(menuProduct));
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");

        Menu menu = Menu.createMenu("허니콤보", 19_000L, menuGroup.getId(), menuProducts);

        assertThat(menu).isNotNull();
        assertThat(menu).isEqualTo(menuProduct.getMenu());
    }
}
