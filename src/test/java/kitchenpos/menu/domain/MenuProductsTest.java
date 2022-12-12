package kitchenpos.menu.domain;

import kitchenpos.price.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("MenuProducts 클래스 테스트")
class MenuProductsTest {

    private final Menu menu = new Menu("강정치킨", BigDecimal.TEN, 1L);
    private final List<MenuProduct> menuProducts = Arrays.asList(new MenuProduct(new Product("강정치킨", BigDecimal.TEN), new Quantity(1L)));

    @DisplayName("1개의 MenuProduct를 MenuProducts에 추가한다.")
    @Test
    void create() {
        MenuProducts menuProducts = new MenuProducts();

        menuProducts.addAll(this.menu, this.menuProducts);

        assertAll(
                () -> assertThat(menuProducts.get()).hasSize(1),
                () -> assertThat(menuProducts.get().get(0).getMenu()).isEqualTo(menu)
        );
    }
}
