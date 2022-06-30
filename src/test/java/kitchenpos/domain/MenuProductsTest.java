package kitchenpos.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
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
