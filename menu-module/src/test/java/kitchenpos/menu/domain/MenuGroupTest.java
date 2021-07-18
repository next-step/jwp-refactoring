package kitchenpos.menu.domain;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 entity  테스트")
class MenuGroupTest {

    @Test
    void 메뉴_그룹_entity_생성() {
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        assertThat(menuGroup).isEqualTo(new MenuGroup("추천메뉴"));
    }
}
