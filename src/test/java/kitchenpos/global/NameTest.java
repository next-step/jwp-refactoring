package kitchenpos.global;

import kitchenpos.menu.domain.MenuProducts;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@DisplayName("Name Domain Test")
class NameTest {
    @Test
    void nameException() {
        assertThatThrownBy(() -> {
            new Name("");
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nameNullException() {
        assertThatThrownBy(() -> {
            new Name(null);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void nameWhiteSpaceException() {
        assertThatThrownBy(() -> {
            new Name(" ");
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
