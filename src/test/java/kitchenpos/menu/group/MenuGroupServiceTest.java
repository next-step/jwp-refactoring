package kitchenpos.menu.group;


import kitchenpos.menu.group.application.MenuGroupService;
import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.menu.group.domain.MenuGroupRepository;
import kitchenpos.menu.group.dto.MenuGroupRequest;
import kitchenpos.menu.group.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {

        //given
        final String menuGroupName = "중화요리";
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest();
        ReflectionTestUtils.setField(menuGroupRequest, "name", menuGroupName);
        MenuGroup menuGroup = menuGroupRequest.toEntity();

        when(menuGroupRepository.save(any())).thenReturn(menuGroup);

        //when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(savedMenuGroup).isNotNull();
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void getMenuGroups() {

        //given
        List<MenuGroup> menuGroups = new ArrayList<>();

        MenuGroup menuGroupA = MenuGroup.create("중화요리");
        MenuGroup menuGroupB = MenuGroup.create("파스타");

        menuGroups.add(menuGroupA);
        menuGroups.add(menuGroupB);

        when(menuGroupRepository.findAll()).thenReturn(menuGroups);

        //when
        List<MenuGroupResponse> findMenuGroups = menuGroupService.list();

        //then
        assertThat(findMenuGroups).isNotEmpty();
        assertThat(findMenuGroups.size()).isEqualTo(2);
        assertThat(findMenuGroups).extracting(MenuGroupResponse::getName).containsExactly("중화요리", "파스타");
    }

}
