package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import kitchenpos.Exception.InvalidMenuPriceException;
import kitchenpos.common.Price;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuName;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Quantity;
import kitchenpos.product.domain.Product;
import kitchenpos.product.domain.ProductName;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class MenuTest {
    private Menu menu;

    @BeforeEach
    void setUp() {
        menu = new Menu(MenuName.from("빅맥"), Price.from(5000), 1L);
    }

    @Test
    void 메뉴_생성_상품_가격_총_합_예외() {
        // given
        MenuProduct 토마토 = new MenuProduct(new Product(ProductName.from("토마토"), Price.from(1000)), Quantity.from(1));
        MenuProduct 양상추 = new MenuProduct(new Product(ProductName.from("토마토"), Price.from(1000)), Quantity.from(2));

        // when, then
        assertThatThrownBy(
                () -> menu.addMenuProducts(Arrays.asList(토마토, 양상추))
        ).isInstanceOf(InvalidMenuPriceException.class);
    }

    @Test
    void 메뉴_상품_추가() {
        // given
        MenuProduct 토마토 = new MenuProduct(new Product(ProductName.from("토마토"), Price.from(5000)), Quantity.from(1));
        MenuProduct 양상추 = new MenuProduct(new Product(ProductName.from("토마토"), Price.from(1000)), Quantity.from(2));

        // when
        menu.addMenuProducts(Arrays.asList(토마토, 양상추));
        // then
        assertThat(menu.getMenuProducts().sumOfPrice()).isEqualTo(Price.from(7000));
    }
}
