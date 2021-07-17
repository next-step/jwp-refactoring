package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;

@DisplayName("메뉴그룹 서비스")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void create_menuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup("A");
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(new MenuGroup("A"));

        // when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(new MenuGroupRequest("A"));

        // then
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getMenuGroupName().toString());
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회한다.")
    void find_menuGroupList() {
        // given
        List<MenuGroupResponse> menuGroupResponses = Arrays.asList(MenuGroupResponse.of(1L, "A"), MenuGroupResponse.of(2L, "B"));
        List<MenuGroup> menuGroups = Arrays.asList(new MenuGroup("A"), new MenuGroup("B"));
        given(menuGroupRepository.findAll()).willReturn(menuGroups);

        // when
        List<MenuGroupResponse> resultResponses = menuGroupService.findAllMenuGroups();

        // then
        assertThat(resultResponses.size()).isEqualTo(menuGroupResponses.size());
    }
}
