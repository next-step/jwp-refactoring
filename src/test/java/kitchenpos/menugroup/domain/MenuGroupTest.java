package kitchenpos.menugroup.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹 생성에 성공한다")
    void createMenuGroupTest() {
        // when
        MenuGroup menuGroup = new MenuGroup("후라이드치킨");

        // then
        assertThat(menuGroup).isEqualTo(new MenuGroup("후라이드치킨"));
    }
}
