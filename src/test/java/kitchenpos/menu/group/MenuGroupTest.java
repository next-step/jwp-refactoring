package kitchenpos.menu.group;

import kitchenpos.menu.group.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupTest {

    @DisplayName("메뉴그룹 생성")
    @Test
    void createMenuGroup() {

        //given
        final String groupName = "중화요리";

        //when
        MenuGroup menuGroup = MenuGroup.create(groupName);

        //then
        assertThat(menuGroup).isNotNull();
    }

}
