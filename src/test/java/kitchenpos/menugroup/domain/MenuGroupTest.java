package kitchenpos.menugroup.domain;

import kitchenpos.common.ErrorCode;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuGroupTest {

    @DisplayName("이름이 null이거나 빈 문자열이면 메뉴 그룹 객체를 생성할 수 없다.")
    @ParameterizedTest
    @NullAndEmptySource
    void makeExceptionWhenCreateMenuGroup(String name) {
        assertThatThrownBy(() -> {
            new MenuGroup(name);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ErrorCode.INVALID_FORMAT_MENU_GROUP_NAME.getErrorMessage());
    }
}
