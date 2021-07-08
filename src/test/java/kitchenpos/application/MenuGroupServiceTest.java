package kitchenpos.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    MenuGroupRepository menuGroupRepository;

    @Autowired
    MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹을 생성해보자")
    @Test
    public void createMenuGroup() throws Exception {
        //given
        String menuGroupName = "테스트메뉴그룹";

        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(menuGroupName);

        //when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroupRequest);

        //then
        assertNotNull(savedMenuGroup.getId());
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroupName);
    }

    @DisplayName("메뉴그룹의 목록을 출력해보자자")
    @Test
    public void listMenuGroup() throws Exception {
        //given
        String menuGroupName = "테스트메뉴그룹";

        MenuGroup menuGroup = new MenuGroup(menuGroupName);
        MenuGroup savedMenuGroup = menuGroupRepository.save(menuGroup);

        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        List<Long> findMenuGroupIds = menuGroups.stream()
                .map(findMenuGroup -> findMenuGroup.getId())
                .collect(Collectors.toList());
        //then
        assertNotNull(menuGroups);
        assertTrue(findMenuGroupIds.contains(savedMenuGroup.getId()));
    }
}
