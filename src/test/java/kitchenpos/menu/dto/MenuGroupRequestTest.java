package kitchenpos.menu.dto;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 요청 객체 테스트")
class MenuGroupRequestTest {

    @Test
    void 메뉴_그룹_요청_객체를_이용하여_메뉴_그룹_entity_생성() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천 메뉴");
        MenuGroup menuGroup = menuGroupRequest.toMenuGroup();
        assertThat(menuGroup).isEqualTo(new MenuGroup("추천 메뉴"));
    }
}
