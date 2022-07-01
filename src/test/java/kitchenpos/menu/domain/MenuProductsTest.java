package kitchenpos.menu.domain;

import kitchenpos.embeddableEntity.Price;
import kitchenpos.embeddableEntity.Quantity;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class MenuProductsTest {

    Quantity quantity;
    Product product;
    MenuProduct menuProduct;
    List<MenuProduct> menuProducts;

    @BeforeEach
    void setUp() {
        quantity = new Quantity(10);
        product = new Product("진매", new Price(1000));
        menuProduct = MenuProduct.of(product, quantity.value());
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
    void totalPrice() {
        // given & when
        MenuProducts result = MenuProducts.of(menuProducts);

        // then
        assertThat(result.totalPrice()).isEqualTo(new Price(10000));
    }

    @Test
    void addMenu() {
        // given
        MenuProducts menuProducts = MenuProducts.of(this.menuProducts);
        Menu menu = new Menu("라면메뉴", new Price(9000), 1L, MenuProducts.of(this.menuProducts));

        // when
        menuProducts.setMenu(menu);

        // then
        assertThat(menuProducts.getMenuProducts().get(0).getMenu()).isEqualTo(menu);
    }
}
