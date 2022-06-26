package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.math.BigDecimal;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

public class MenuTest {
    @Test
    @DisplayName("메뉴를 생성한다.")
    void createMenu() {
        Product product = Product.of("허니콤보", BigDecimal.valueOf(19_000L));
        MenuProduct menuProduct = MenuProduct.createMenuProduct(product, 1L);
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");

        Menu menu = Menu.createMenu("허니콤보", Price.from(BigDecimal.valueOf(19_000L)), menuGroup, Lists.list(menuProduct));

        assertThat(menu).isNotNull();
        assertThat(menu).isEqualTo(menuProduct.getMenu());
    }
}
