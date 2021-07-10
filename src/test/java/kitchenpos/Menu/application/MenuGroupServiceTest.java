package kitchenpos.Menu.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 관련 기능 테스트")
@ExtendWith(MockitoExtension.class)
public
class MenuGroupServiceTest {
    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;

    private MenuGroupRequest menuGroupRequest1;
    private MenuGroupRequest menuGroupRequest2;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroup1 = 메뉴_그룹_생성(1L, "반반시리즈");
        menuGroup2 = 메뉴_그룹_생성(2L, "허니시리즈");

        menuGroupRequest1 = new MenuGroupRequest("반반시리즈");
        menuGroupRequest2 = new MenuGroupRequest("허니시리즈");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        given(menuGroupRepository.save(any())).willReturn(menuGroup1);

        MenuGroupResponse createdMenuGroup = 메뉴_그룹_생성_요청(this.menuGroup1);

        메뉴_그룹_생성됨(createdMenuGroup, this.menuGroup1);
    }

    @Test
    void list() {
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroupResponse> createdMenuGroups = 메뉴_그룹_리스트_요청();

        메뉴_그룹_리스트_요청됨(createdMenuGroups, Arrays.asList(menuGroup1, menuGroup2));
    }

    public static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        return new MenuGroup(id, name);
    }

    private void 메뉴_그룹_생성됨(MenuGroupResponse createdMenuGroup, MenuGroup menuGroup1) {
        assertThat(createdMenuGroup.getId()).isEqualTo(menuGroup1.getId());
        assertThat(createdMenuGroup.getName()).isEqualTo(menuGroup1.getName());
    }

    private MenuGroupResponse 메뉴_그룹_생성_요청(MenuGroup menuGroup) {
        return menuGroupService.create(new MenuGroupRequest(menuGroup.getName()));
    }

    private List<MenuGroupResponse> 메뉴_그룹_리스트_요청() {
        return menuGroupService.list();
    }

    private void 메뉴_그룹_리스트_요청됨(List<MenuGroupResponse> createdMenuGroups, List<MenuGroup> menuGroups) {
        assertThat(createdMenuGroups.get(0).getName()).isEqualTo(menuGroups.get(0).getName());
        assertThat(createdMenuGroups.get(1).getName()).isEqualTo(menuGroups.get(1).getName());
    }
}