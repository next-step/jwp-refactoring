package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 반환 객체 테스트")
class MenuGroupResponseTest {

    @Test
    void 메뉴_그룹_entity_를_이용하여_메뉴_그룹_반환_객체_생성() {
        MenuGroup menuGroup = new MenuGroup(1L, "추천 메뉴");
        MenuGroupResponse menuGroupResponse = MenuGroupResponse.of(menuGroup);
        assertThat(menuGroupResponse).isEqualTo(new MenuGroupResponse(1L, "추천 메뉴"));
    }
}
