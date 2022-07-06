package kitchenpos.menu.domain;

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuProductsTest {

    Quantity quantity;
    MenuProduct menuProduct;
    List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        quantity = new Quantity(10);
        menuProduct = new MenuProduct(1L, quantity.value());
        menuProducts = Collections.singletonList(menuProduct);
    }

    @Test
    @DisplayName("MenuProducts 인스턴스를 생성한다")
    void of() {
        // when
        MenuProducts result = MenuProducts.of(menuProducts);

        // then
        assertThat(result.getMenuProducts()).isEqualTo(menuProducts);
    }

    @Test
    @DisplayName("메뉴제품에 메뉴 엔티티를 넣어준다")
    void setMenu() {
        // given
        MenuProducts menuProducts = MenuProducts.of(this.menuProducts);
        Menu menu = new Menu("라면메뉴", new Price(9000), 1L, MenuProducts.of(this.menuProducts));

        // when
        menuProducts.setMenu(menu);

        // then
        assertThat(menuProducts.getMenuProducts().get(0).getMenu()).isEqualTo(menu);
    }
}
