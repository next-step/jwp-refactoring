package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.junit.jupiter.api.Assertions.assertThrows;

import kitchenpos.menu.exception.EmptyNameException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

class MenuGroupTest {

    @DisplayName("MenuGroup을 생성할 수 있다. (Name)")
    @ParameterizedTest
    @ValueSource(strings = {"A", "가", "메뉴그룹"})
    void create01(String name) {
        // when & then
        assertThatNoException().isThrownBy(() -> MenuGroup.from(name));
    }

    @DisplayName("MenuGroup 생성 시 Name이 없으면 예외가 발생한다.")
    @ParameterizedTest
    @NullAndEmptySource
    void create02(String name) {
        // when & then
        assertThrows(EmptyNameException.class, () -> MenuGroup.from(name));
    }
}