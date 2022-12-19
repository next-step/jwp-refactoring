package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 가격 단위 테스트")
public class MenuPriceTest {

    @DisplayName("가격이 동일하면 메뉴 가격은 동일하다.")
    @Test
    void 가격이_동일하면_메뉴_가격은_동일하다() {
        assertEquals(
                new MenuPrice(10),
                new MenuPrice(10)
        );
    }

    @DisplayName("메뉴 가격은 null일 수 없다.")
    @Test
    void 메뉴_가격은_null일_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new MenuPrice(null))
                .withMessage(ErrorMessage.MENU_REQUIRED_PRICE.getMessage());
    }

    @DisplayName("메뉴 가격은 0보다 작을 수 없다.")
    @Test
    void 메뉴_가격은_0보다_작을_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> new MenuPrice(-1))
                .withMessage(ErrorMessage.MENU_INVALID_PRICE.getMessage());
    }
}
