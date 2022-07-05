package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @DisplayName("메뉴그룹을 생성한다.")
    @Test
    void create() {
        //given
        String name = "파스타메뉴";

        //when
        MenuGroup menuGroup = new MenuGroup(name);

        //then
        assertThat(menuGroup).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo(name);
    }

    @DisplayName("이름이 없으면 메뉴그룹을 생성할 수 없다.")
    @Test
    void create_invalidName() {
        //when & then
        assertThatThrownBy(() -> new MenuGroup(null))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("메뉴그룹의 이름을 입력해주세요.");
    }

}
