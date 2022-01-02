package menu.application;

import kitchenpos.menu.application.MenuGroupService;
import menu.domain.FakeMenuGroupRepository;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 그룹 테스트")
class MenuGroupServiceTest {
    private final MenuGroupRepository menuGroupRepository = new FakeMenuGroupRepository();
    private final MenuGroupService menuGroupService = new MenuGroupService(menuGroupRepository);

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        MenuGroupRequest menuGroup = MenuGroupRequest.of("추천메뉴");
        MenuGroupResponse result = menuGroupService.create(menuGroup);
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(1L),
                () -> assertThat(result.getName()).isEqualTo(menuGroup.getName())
        );
    }

    @DisplayName("모든 메뉴 그룹 조회")
    @Test
    void list() {
        MenuGroupRequest menuGroup1 = MenuGroupRequest.of("추천메뉴1");
        MenuGroupRequest menuGroup2 = MenuGroupRequest.of("추천메뉴2");

        menuGroupService.create(menuGroup1);
        menuGroupService.create(menuGroup2);

        List<MenuGroupResponse> list = menuGroupService.list();
        assertThat(list.size()).isEqualTo(2);
    }

}
