package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static kitchenpos.menu.exception.MenuExceptionConstants.INVALID_FORMAT_MENU_GROUP_NAME;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuGroupTest {

    @DisplayName("이름이 null이거나 빈 문자열이면 메뉴 그룹 객체를 생성할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void makeExceptionWhenCreateMenuGroup(String name) {
        assertThatThrownBy(() -> {
            new MenuGroup(name);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(INVALID_FORMAT_MENU_GROUP_NAME.getErrorMessage());
    }
}
