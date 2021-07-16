package kitchenpos.menugroup.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @Test
    void 메뉴_그룹을_생성한다() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천메뉴");
        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");

        when(menuGroupRepository.save(any(MenuGroup.class)))
                .thenReturn(menuGroup);
        MenuGroupResponse savedMenuGroup = menuGroupService.create(menuGroupRequest);

        assertThat(savedMenuGroup).extracting("name").isEqualTo("추천메뉴");
    }

    @Test
    void 모든_메뉴_그룹을_조회한다() {
        MenuGroup menuGroup1 = new MenuGroup(1L, "추천메뉴");
        MenuGroup menuGroup2 = new MenuGroup(2L, "추천메뉴2");
        when(menuGroupRepository.findAll())
                .thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroupResponse> menuGroups = menuGroupService.list();
        assertThat(menuGroups)
                .containsAll(Arrays.asList(new MenuGroupResponse(menuGroup1), new MenuGroupResponse(menuGroup2)));
    }
}
