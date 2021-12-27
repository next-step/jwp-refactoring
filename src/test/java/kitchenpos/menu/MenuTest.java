package kitchenpos.menu;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;
import kitchenpos.menu.exception.MenuPriceMoreThanMenuProductPriceSumException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴")
class MenuTest {

    @Test
    @DisplayName("메뉴의 금액이 상품의 총 금액보다 크다면 예외가 발생한다.")
    void menuPriceMoreThanProductPriceSum() {
        // given
        final MenuGroup menuGroup = new MenuGroup("추천메뉴");
        final Product product = new Product("후라이드", BigDecimal.valueOf(8000));
        final MenuProduct menuProduct = new MenuProduct(product, 2L);


        // when
        assertThatThrownBy(() -> {
            new Menu("후라이드 2마리", BigDecimal.valueOf(18000), menuGroup, Arrays.asList(menuProduct));
        }).isInstanceOf(MenuPriceMoreThanMenuProductPriceSumException.class);
    }
}
