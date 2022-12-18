package kitchenpos.menu;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Quantity;
import kitchenpos.menu.exception.InvalidQuantityException;

class QuantityTest {

    @DisplayName("[수량] 널값이거나 음수일 수 없다")
    @Test
    void test1() {
        assertThatThrownBy(() -> new Quantity(null))
            .isInstanceOf(InvalidQuantityException.class);
        assertThatThrownBy(() -> new Quantity(-10L))
            .isInstanceOf(InvalidQuantityException.class);
        assertDoesNotThrow(() -> new Quantity(10L));
    }
}
