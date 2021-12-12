package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;

import kitchenpos.common.domain.Quantity;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 상품")
class MenuProductTest {

    @Test
    @DisplayName("생성")
    void instance() {
        assertThatNoException()
            .isThrownBy(() -> MenuProduct.of(1L, Quantity.from(1L)));
    }

    @Test
    @DisplayName("수량은 필수")
    void instance_nullQuantity_thrownIllegalArgumentException() {
        assertThatIllegalArgumentException()
            .isThrownBy(() -> MenuProduct.of(1L, null))
            .withMessage("수량은 필수입니다.");
    }
}
