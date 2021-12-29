package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹 생성 테스트")
    public void createMenuGroupTest() {
        //when
        MenuGroup menuGroup = new MenuGroup("후라이드+양념");

        //then
        assertThat(menuGroup).isNotNull();
        assertThat(menuGroup.getName()).isNotNull();
    }
}
