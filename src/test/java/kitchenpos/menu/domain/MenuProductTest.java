package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.menu.domain.MenuProduct.*;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품")
class MenuProductTest {

    @DisplayName("메뉴 상품을 생성한다.")
    @Test
    void create() {
        assertThatNoException().isThrownBy(() -> new MenuProduct(1L, 1L, 1L, 1L));
    }

    @DisplayName("메뉴 상품을 생성한다. / 메뉴를 필수로 갖는다.")
    @Test
    void create_fail_menu() {
        assertThatThrownBy(() -> new MenuProduct(1L, null, 1L, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(MENU_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴 상품을 생성한다. / 상품을 필수로 갖는다.")
    @Test
    void create_fail_product() {
        assertThatThrownBy(() -> new MenuProduct(1L, 1L, null, 1L))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRODUCT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴 상품을 생성한다. / 갯수를 필수로 갖는다.")
    @Test
    void create_fail_quantity() {
        assertThatThrownBy(() -> new MenuProduct(1L, 1L, 1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(QUANTITY_NULL_EXCEPTION_MESSAGE);
    }
}
