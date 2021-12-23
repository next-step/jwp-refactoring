package kitchenpos.menu.domain;

import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupTest {

    @DisplayName("생성")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup("추천메뉴");

        assertThat(menuGroup).isNotNull();
    }
}
