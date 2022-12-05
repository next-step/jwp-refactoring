package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuGroupExceptionCode;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupTest {
    @Test
    void 동등성_테스트() {
        assertEquals(new MenuGroup("양식"), new MenuGroup("양식"));
    }

    @Test
    void 이름이_null이면_메뉴_그룹_객체를_생성할_수_없음() {
        assertThatThrownBy(() -> {
            new MenuGroup(null);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(MenuGroupExceptionCode.REQUIRED_NAME.getMessage());
    }
}
