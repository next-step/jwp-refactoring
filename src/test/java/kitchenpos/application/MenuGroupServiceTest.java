package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
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
    MenuGroupDao menuGroupDao;

    @Autowired
    MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹을 생성해보자")
    @Test
    public void createMenuGroup() throws Exception {
        //given
        String menuGroupName = "테스트메뉴그룹";

        MenuGroupRequest menuGroupRequest = new MenuGroupRequest();
        menuGroupRequest.setName(menuGroupName);

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

        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(menuGroupName);
        MenuGroup savedMenuGroup = menuGroupDao.save(menuGroup);

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
