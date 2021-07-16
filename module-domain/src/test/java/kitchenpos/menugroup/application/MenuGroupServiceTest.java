package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createTest() {
        // given
        MenuGroup 추천메뉴 = new MenuGroup(1L, "추천메뉴");
        given(menuGroupRepository.save(any())).willReturn(추천메뉴);

        // when
        MenuGroupResponse createdMenuGroup = menuGroupService.create(new MenuGroupRequest("추천메뉴"));

        // then
        Assertions.assertThat(createdMenuGroup.getId()).isEqualTo(추천메뉴.getId());
        Assertions.assertThat(createdMenuGroup.getName()).isEqualTo(추천메뉴.getName());
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void listTest() {
        // given
        MenuGroup 추천메뉴 = new MenuGroup(1L, "추천메뉴");
        MenuGroup 인기메뉴 = new MenuGroup(2L, "인기메뉴");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(추천메뉴, 인기메뉴));

        // when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        // then
        Assertions.assertThat(menuGroups).hasSize(2);
        Assertions.assertThat(menuGroups.get(0).getId()).isEqualTo(추천메뉴.getId());
        Assertions.assertThat(menuGroups.get(0).getName()).isEqualTo(추천메뉴.getName());
        Assertions.assertThat(menuGroups.get(1).getId()).isEqualTo(인기메뉴.getId());
        Assertions.assertThat(menuGroups.get(1).getName()).isEqualTo(인기메뉴.getName());
    }
}
