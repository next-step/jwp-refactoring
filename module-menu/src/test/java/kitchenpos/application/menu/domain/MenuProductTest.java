package kitchenpos.application.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.math.BigDecimal;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품 테스트")
class MenuProductTest {

    @DisplayName("메뉴를 구성하는 메뉴 상품들에 상품이 존재하지 않으면 등록할 수 없다.")
    @Test
    void validate() {
        // given
        MenuGroup menuGroup = new MenuGroup("menuGroup");
        Menu menu = new Menu("name", BigDecimal.valueOf(1000), menuGroup);
        // when && then
        assertThatThrownBy(() -> new MenuProduct(menu, null, 1L))
            .isInstanceOf(IllegalArgumentException.class);
    }

}
