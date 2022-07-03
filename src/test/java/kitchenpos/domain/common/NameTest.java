package kitchenpos.domain.common;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import kitchenpos.menus.menu.domain.Name;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class NameTest {

    @Test
    @DisplayName("이름은 빈 문자열 일 수 없다.")
    void validationTest() {
        assertThatThrownBy(
                () -> new Name("")
        ).isInstanceOf(IllegalArgumentException.class);
    }
}
