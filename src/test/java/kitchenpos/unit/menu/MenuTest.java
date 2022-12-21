package kitchenpos.unit.menu;

import kitchenpos.menu.domain.*;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

class MenuTest {

    @Test
    @DisplayName("메뉴 가격은 모든 상품의 합 이하야야한다.")
    void sumResultLimit() {
        MenuGroup menuGroup = new MenuGroup("양식");
        Product product = new Product(new Price(BigDecimal.valueOf(10_000)), "파스타");
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(product, 2L)));
        Menu menu = new Menu("스테이크", new Price(BigDecimal.valueOf(500_000)), menuGroup.getId());

        assertThatThrownBy(
                () -> menu.addMenuProducts(menuProducts))
                .isInstanceOf(IllegalArgumentException.class);
    }
}
