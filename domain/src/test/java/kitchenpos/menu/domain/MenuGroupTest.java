package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹을 생성 한다")
    public void createMenuGroup() {
        //given
        String name = "피자";

        //when
        MenuGroup createMenuGroup = new MenuGroup(name);

        //then
        assertThat(createMenuGroup).isEqualTo(new MenuGroup(name));
    }
}
