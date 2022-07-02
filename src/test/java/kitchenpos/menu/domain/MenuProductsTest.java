package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.order.domain.Quantity;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    @Test
    void 메뉴_추가() {
        // given
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(new MenuProduct(
                new Product(ProductName.from("양상추"), Price.from(1000)), Quantity.from(5))
        ));
        Menu 빅맥 = new Menu(MenuName.from("빅맥"), Price.from(1000), 1L);

        // when
        menuProducts.addMenu(빅맥);

        // then
        assertThat(menuProducts.getMenuProducts().get(0).getMenu().getName()).isEqualTo(빅맥.getName());
    }

    @Test
    void 가격_합() {
        // given
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(
                new MenuProduct(new Product(ProductName.from("양상추"), Price.from(1000)), Quantity.from(5)),
                new MenuProduct(new Product(ProductName.from("토마토"), Price.from(5000)), Quantity.from(1))
        ));

        // when
        Price sum = menuProducts.sumOfPrice();

        // then
        assertThat(sum).isEqualTo(Price.from(10000));
    }
}
