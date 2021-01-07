package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import kitchenpos.ui.dto.MenuGroupRequest;
import kitchenpos.ui.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroupTest() {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("testMenuGroup");

        // when
        MenuGroup saved = menuGroupService.create(menuGroupRequest);

        // then
        assertThat(saved.getId()).isNotNull();
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void getMenuGroupsTest() {
        // given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("testMenuGroup");
        menuGroupService.create(menuGroupRequest);

        // when
        List<MenuGroupResponse> foundMenuGroups = menuGroupService.list();
        List<String> names = foundMenuGroups.stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());

        // then
        assertThat(names).contains(menuGroupRequest.getName());
    }
}