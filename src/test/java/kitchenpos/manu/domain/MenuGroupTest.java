package kitchenpos.manu.domain;

import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupTest {
    @Test
    void 메뉴_그룹_생성() {
        MenuGroup menuGroup = new MenuGroup("분식메뉴");
        assertThat(menuGroup).isEqualTo(new MenuGroup("분식메뉴"));
    }
}
