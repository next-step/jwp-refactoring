package kitchenpos.menugroup.domain;

import kitchenpos.menugroup.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("메뉴 그룹 도메인 테스트")
class MenuGroupTest {

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    public void create() throws Exception {
        // given
        String name = "두마리메뉴";

        // when
        MenuGroup menuGroup = new MenuGroup(name);

        // then
        Assertions.assertThat(menuGroup.getName()).isEqualTo(name);
    }
}
