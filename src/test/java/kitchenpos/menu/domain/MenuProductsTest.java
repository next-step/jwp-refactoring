package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import kitchenpos.common.Price;

import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuProductsTest {
    private Product 양상추;

    @BeforeEach
    void setUp() {
        양상추 = new Product(ProductName.from("양상추"), Price.from(1000));
    }

    @Test
    void 메뉴_추가() {
        // given
        MenuProducts menuProducts = MenuProducts.from(Arrays.asList(new MenuProduct(양상추.getId(), Quantity.from(5))));
        Menu 빅맥 = new Menu(MenuName.from("빅맥"), Price.from(1000), 1L);

        // when
        menuProducts.addMenu(빅맥);

        // then
        assertThat(menuProducts.getMenuProducts().get(0).getMenu().getName()).isEqualTo(빅맥.getName());
    }
}
