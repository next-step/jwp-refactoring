package kitchenpos.service.menugroup;

import kitchenpos.domain.menugroup.MenuGroup;
import kitchenpos.domain.menugroup.MenuGroupRepository;
import kitchenpos.service.menugroup.application.MenuGroupService;
import kitchenpos.service.menugroup.dto.MenuGroupRequest;
import kitchenpos.service.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성할 수 있다.")
    void create() {
        //given
        MenuGroupRequest request = new MenuGroupRequest("menuGroup");
        given(menuGroupRepository.save(any())).willReturn(request.toMenuGroup());

        //when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(request);

        //then
        assertThat(savedMenuGroup.getName()).isEqualTo("menuGroup");
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    void list() {
        //given
        MenuGroup menuGroup1 = new MenuGroup("menuGroup1");
        MenuGroup menuGroup2 = new MenuGroup("menuGroup2");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2));

        //when
        List<MenuGroupResponse> menuGroupList = menuGroupService.list();

        //then
        assertThat(menuGroupList.stream().map(MenuGroupResponse::getName)
                .collect(Collectors.toList())).containsExactlyInAnyOrder("menuGroup1", "menuGroup2");
    }
}
