package kitchenpos.menuGroup.domain;

import kitchenpos.menuGroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupTest {

    @DisplayName("생성")
    @Test
    void create() {
        MenuGroup menuGroup = new MenuGroup(1L,"추천메뉴");

        assertThat(menuGroup).isNotNull();
    }
}
