package kitchenpos.domain;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.ui.exception.IllegalMenuPriceException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuTest {

    @DisplayName("입력받은 메뉴의 가격이 음수인지 확인")
    @Test
    void 입력받은_메뉴의_가격이_음수인지_확인() {
        Menu inputMenu = new Menu("후라이드세트", BigDecimal.valueOf(-1000L), 1L);

        assertThatThrownBy(inputMenu::validatePrice).isInstanceOf(IllegalMenuPriceException.class);
    }

    @DisplayName("입력받은 메뉴의 가격이 상품의 합보다 큰지 확인")
    @Test
    void 입력받은_메뉴의_가격이_상품의_합보다_큰지_확인() {
        Menu inputMenu = new Menu("후라이드세트", BigDecimal.valueOf(30000L), 1L);

        assertThatThrownBy(() -> inputMenu.compareMenuPriceToProductsSum(BigDecimal.valueOf(20000L)))
                .isInstanceOf(IllegalMenuPriceException.class);
    }
}
