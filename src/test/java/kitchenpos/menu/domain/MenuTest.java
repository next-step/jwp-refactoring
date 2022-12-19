package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.error.ErrorEnum;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.Test;

public class MenuTest {
    @Test
    void 메뉴의_가격이_전체_메뉴_상품의_가격의_합보다_클_수_없다() {
        // given
        MenuGroup menuGroup = new MenuGroup(new Name("한식"));
        Product product = new Product(new Name("불고기"), new Price(BigDecimal.valueOf(10_000)));
        MenuProducts menuProducts = new MenuProducts(Arrays.asList(new MenuProduct(new Quantity(1L), product)));
        Menu menu = new Menu(new Name("불고기정식"), new Price(BigDecimal.valueOf(200_000)), menuGroup);

        // then
        assertThatThrownBy(() -> menu.setMenuProducts(menuProducts))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(ErrorEnum.MENU_PRICE_OVER_TOTAL_PRICE.message());
    }
}
