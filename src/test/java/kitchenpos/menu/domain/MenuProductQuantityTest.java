package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("메뉴 상품의 수량을 관리하는 클래스 테스트")
class MenuProductQuantityTest {

    @DisplayName("수량에 음수 값을 입력할 수 없다.")
    @Test
    void 수량에_음수_값을_입력할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new MenuProductQuantity(-1))
                .withMessage(ErrorMessage.MENU_PRODUCT_INVALID_QUANTITY.getMessage());
    }
}
