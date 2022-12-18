package kitchenpos.menugroup.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuGroupTest {
    @DisplayName("메뉴그룹명은 NULL이거나 공백일 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void createWithNameIsNullOrEmpty(String name) {
        assertThatThrownBy(() -> new MenuGroup(name))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("메뉴 그룹명은 비어있거나 공백일 수 없습니다.");
    }
}
