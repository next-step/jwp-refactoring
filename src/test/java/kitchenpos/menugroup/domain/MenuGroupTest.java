package kitchenpos.menugroup.domain;

import kitchenpos.common.Message;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class MenuGroupTest {

    @DisplayName("메뉴그룹 생성시, 이름을 입력하지 않으면 예외발생")
    @Test
    void 메뉴그룹_생성_이름없으면_예외발생() {

        String 메뉴그룹이름 = null;

        assertThatThrownBy(() -> new MenuGroup(메뉴그룹이름))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage(Message.ERROR_MENUGROUP_NAME_REQUIRED.showText());
    }
}
