package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹 생성")
    void createMenuGroup() {
        //given
        String 추천메뉴 = "추천메뉴";

        //when
        MenuGroup 메뉴그룹 = new MenuGroup(추천메뉴);

        //then
        assertThat(메뉴그룹.getName()).isEqualTo(추천메뉴);
    }

    @Test
    @DisplayName("메뉴 그룹 이름이 없으면 오류 발생")
    void menuGroupNoNameException() {
        //given
        String 빈메뉴명 = null;

        //when & then
        assertThatThrownBy(() -> new MenuGroup(빈메뉴명))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 그룹의 이름을 입력해 주세요");
    }
}