package kitchenpos.menu.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuNameTest {
    @DisplayName("메뉴명은 NULL이거나 공백일 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithException(String name) {
        assertThatThrownBy(() -> new MenuName(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴명은 비어있거나 공백일 수 없습니다.");
    }
}
