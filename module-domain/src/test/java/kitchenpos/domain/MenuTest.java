package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.exception.IllegalMenuPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.exception.NoMenuGroupException;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.NegativePriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
    Product 후라이드치킨 = new Product("후라이드치킨", Price.from(16_000));

    @DisplayName("메뉴 그룹이 있어야한다")
    @Test
    void noMenuGroupTest() {
        assertThatThrownBy(() -> new Menu("후라이드치킨", Price.from(16_000), null, new MenuProducts()))
                .isInstanceOf(NoMenuGroupException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품의 총합보다 작아야 한다")
    @Test
    void sumPriceTest() {
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(new MenuProduct(후라이드치킨, 1)));
        assertThatThrownBy(() -> new Menu("후라이드치킨", Price.from(19_000), new MenuGroup(), menuProducts))
                .isInstanceOf(IllegalMenuPriceException.class);
    }

    @DisplayName("메뉴의 가격이 0원 이상이어야 한다")
    @Test
    void negativePriceTest() {
        // then
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(new MenuProduct(후라이드치킨, 1)));
        assertThatThrownBy(() -> new Menu("후라이드치킨", Price.from(-1_000), new MenuGroup(), menuProducts))
                .isInstanceOf(NegativePriceException.class);
    }
}
