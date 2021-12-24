package kitchenpos.menu.domain;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

import kitchenpos.common.exception.Message;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;

class MenuGroupTest {

    @ParameterizedTest
    @NullAndEmptySource
    void 메뉴그룹의_이름이_빈값인경우_예외(String input) {

        assertThatThrownBy(() -> {
            MenuGroup.of(input);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(Message.MENU_GROUP_NAME_IS_NOT_NULL.getMessage());
    }

    @Test
    void 메뉴그룹_정상생성() {

        MenuGroup 세트메뉴 = MenuGroup.of("세트메뉴");

        Assertions.assertThat(세트메뉴).isNotNull();
    }
}