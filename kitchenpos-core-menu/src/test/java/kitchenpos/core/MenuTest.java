package kitchenpos.core;

import kitchenpos.core.domain.Menu;
import kitchenpos.core.domain.MenuProduct;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class MenuTest {
    @DisplayName("가격은 0 이상이어야 한다.")
    @Test
    void name() {
        // given
        final List<MenuProduct> menuProducts = Collections.singletonList(MenuProduct.of(1L, 10));
        // when
        ThrowableAssert.ThrowingCallable createCall = () -> Menu.of("짬뽕", -190, 2L, menuProducts);
        // then
        assertThatThrownBy(createCall).isInstanceOf(IllegalArgumentException.class);
    }
}
