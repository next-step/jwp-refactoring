package kitchenpos.application.menu.domain;

import java.math.BigDecimal;
import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 테스트")
class MenuTest {

    @DisplayName("메뉴 생성 시 가격은 음수가 나올 수 없다.")
    @Test
    void menu_price_not_negative_number() {
        // given && when && then
        Assertions.assertThatThrownBy(() -> new Menu("name", BigDecimal.valueOf(-1000), null))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴의 가격이 메뉴 상품 가격의 합보다 크다면 등록할 수 없다")
    @Test
    void menu_price_less_then_menu_product_sum() {
        // given
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        Menu menu = new Menu("name", BigDecimal.valueOf(1000), menuGroup);
        MenuProduct 아메리카노 = new MenuProduct(menu, new Product("아메리카노", BigDecimal.valueOf(5_000)), 1);
        MenuProduct 바닐라라떼 = new MenuProduct(menu, new Product("바닐라라떼", BigDecimal.valueOf(3_000)), 2);

        // when && then
        Assertions.assertThatThrownBy(() -> new Menu("menu", BigDecimal.valueOf(9_000), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
