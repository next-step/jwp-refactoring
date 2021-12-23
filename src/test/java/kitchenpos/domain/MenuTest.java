package kitchenpos.domain;

import kitchenpos.exception.IllegalMenuPriceException;
import kitchenpos.exception.NegativePriceException;
import kitchenpos.exception.NoMenuGroupException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {

    @DisplayName("메뉴 그룹이 있어야한다")
    @Test
    void noMenuGroupTest() {
        assertThatThrownBy(() -> new Menu("후라이드치킨", Price.from(16_000), null, new MenuProducts())).isInstanceOf(NoMenuGroupException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품의 총합보다 작아야 한다")
    @Test
    void sumPriceTest() {
        Product 후라이드치킨 = new Product(1L, "후라이드치킨", Price.from(16_000));
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(new MenuProduct(1L, null, 후라이드치킨, 1)));
        assertThatThrownBy(() -> new Menu("후라이드치킨", Price.from(19_000), new MenuGroup(), menuProducts)).isInstanceOf(IllegalMenuPriceException.class);
    }

    @DisplayName("메뉴의 가격이 0원 이상이어야 한다")
    @Test
    void negativePriceTest() {
        // then
        Product 후라이드치킨 = new Product(1L, "후라이드치킨", Price.from(16_000));
        MenuProducts menuProducts = new MenuProducts(Collections.singletonList(new MenuProduct(1L, null, 후라이드치킨, 1)));
        assertThatThrownBy(() -> new Menu("후라이드치킨", Price.from(-1_000), new MenuGroup(), menuProducts)).isInstanceOf(NegativePriceException.class);
    }
}