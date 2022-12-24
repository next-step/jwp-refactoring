package kitchenpos.menu.domain;

import kitchenpos.menu.domain.fixture.MenuProductFixture;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.menu.domain.MenuProduct.PRODUCT_NULL_EXCEPTION_MESSAGE;
import static kitchenpos.menu.domain.MenuProduct.QUANTITY_NULL_EXCEPTION_MESSAGE;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@DisplayName("메뉴 상품")
class MenuProductTest {

    @DisplayName("메뉴 상품을 생성한다.")
    @Test
    void create() {
        assertThatNoException().isThrownBy(() -> MenuProductFixture.menuProductA(1L));
    }

    @DisplayName("메뉴 상품을 생성한다. / 상품을 필수로 갖는다.")
    @Test
    void create_fail_product() {
        assertThatThrownBy(() -> new MenuProduct(null, new Quantity(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(PRODUCT_NULL_EXCEPTION_MESSAGE);
    }

    @DisplayName("메뉴 상품을 생성한다. / 갯수를 필수로 갖는다.")
    @Test
    void create_fail_quantity() {
        assertThatThrownBy(() -> new MenuProduct(1L, null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(QUANTITY_NULL_EXCEPTION_MESSAGE);
    }
}
