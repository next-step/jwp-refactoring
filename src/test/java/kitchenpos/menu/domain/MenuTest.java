package kitchenpos.menu.domain;

import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
    private final MenuGroup menuGroup = MenuGroup.of(1L, "한마리메뉴");
    private final Product product = Product.of(1L, "후라이드", BigDecimal.valueOf(16000));
    private final List<MenuProduct> menuProducts = Collections.singletonList(MenuProduct.of(product, 1));

    @DisplayName("메뉴 가격이 음수일 경우 Exception 발생")
    @Test
    void validatePriceException1() {
        assertThatThrownBy(() -> Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(-1), menuGroup, menuProducts))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격이 없거나 음수입니다.");
    }

    @DisplayName("메뉴 가격이 없을 경우 Exception 발생")
    @Test
    void validatePriceException2() {
        assertThatThrownBy(() -> Menu.of(1L, "후라이드치킨", null, menuGroup, menuProducts))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("가격이 없거나 음수입니다.");
    }

    @DisplayName("메뉴 가격이 메뉴 상품의 총 합보다 클경우 Exception 발생")
    @Test
    void comparePriceException() {
        assertThatThrownBy(() -> Menu.of(1L, "후라이드치킨", BigDecimal.valueOf(17000), menuGroup, menuProducts))
                .isInstanceOf(InvalidPriceException.class)
                .hasMessage("메뉴의 가격은 메뉴 상품들의 총 합보다 작아야 합니다.");
    }
}
