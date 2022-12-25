package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("MenuProducts 테스트")
class MenuProductsTest {
    @DisplayName("1개의 MenuProduct를 MenuProducts에 추가한다.")
    @Test
    void create() {
        MenuProducts menuProducts = new MenuProducts();
        Menu menu = new Menu("테스트메뉴", BigDecimal.TEN, 1L);
        List<MenuProduct> menuProduct = Arrays.asList(new MenuProduct(1L, 1L));

        menuProducts.addAll(menu, menuProduct);

        assertAll(
            () -> assertThat(menuProducts.get()).hasSize(1),
            () -> assertThat(menuProducts.get().get(0).getMenu()).isEqualTo(menu)
        );
    }
}
