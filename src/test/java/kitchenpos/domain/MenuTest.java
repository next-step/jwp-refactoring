package kitchenpos.domain;

import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
    @DisplayName("메뉴의 가격이 전체 메뉴상품 가격의 합보다 클 수 없다.")
    @Test
    void menuPriceNotOverThanTotalPriceException() {
        // given
        MenuGroup menuGroup = new MenuGroup(new Name("한식"));
        Product product = new Product(new Name("불고기"), new Price(BigDecimal.valueOf(10_000)));
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(new Quantity(1L), product)));
        Menu menu = new Menu(new Name("불고기정식"), new Price(BigDecimal.valueOf(200_000)), menuGroup);

        // when & then
        assertThatThrownBy(() -> menu.setMenuProducts(menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorCode.MENU_PRICE_SHOULD_NOT_OVER_TOTAL_PRICE.getMessage());
    }
}
