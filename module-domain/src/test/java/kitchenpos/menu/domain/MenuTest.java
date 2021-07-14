package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.ArrayList;
import kitchenpos.menu.exception.MenuPriceEmptyException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 테스트")
public class MenuTest {

    @DisplayName("메뉴의 가격은 필수 입력항목이다.")
    @Test
    void price_null() {
        assertThatThrownBy(() -> new Menu("메뉴명", null, null, new ArrayList<>()))
            .isInstanceOf(MenuPriceEmptyException.class);
    }
}
