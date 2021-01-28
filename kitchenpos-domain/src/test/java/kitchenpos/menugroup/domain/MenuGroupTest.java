package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupTest {

    @DisplayName("메뉴그룹 객체를 생성한다.")
    @Test
    void constructor() {
        // when
        MenuGroup menuGroup = new MenuGroup("name");

        // then
        assertThat(menuGroup).isNotNull();
        assertThat(menuGroup.getName()).isEqualTo("name");
    }
}
