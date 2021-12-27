package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.menugroup.testfixtures.MenuGroupTestFixtures;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        String name = "추천메뉴";
        MenuGroup menuGroup = new MenuGroup(name);
        MenuGroupTestFixtures.메뉴그룹_생성_결과_모킹(menuGroupRepository, menuGroup);

        //when
        MenuGroupResponse savedMenuGroup = menuGroupService.create(new MenuGroupRequest(name));

        //then
        assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    @Test
    void list() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(
            new MenuGroup(1L, "추천메뉴"),
            new MenuGroup(2L, "베스트메뉴"));
        MenuGroupTestFixtures.메뉴그룹_전체조회_모킹(menuGroupRepository, menuGroups);

        //when
        List<MenuGroupResponse> findMenuGroups = menuGroupService.list();

        //then
        assertThat(findMenuGroups.size()).isEqualTo(menuGroups.size());
        메뉴그룹_목록_검증(findMenuGroups, menuGroups);
    }

    private void 메뉴그룹_목록_검증(List<MenuGroupResponse> findMenuGroups, List<MenuGroup> menuGroups) {
        List<Long> findProductIds = findMenuGroups.stream()
            .map(MenuGroupResponse::getId)
            .collect(Collectors.toList());
        List<Long> expectProductIds = menuGroups.stream()
            .map(MenuGroup::getId)
            .collect(Collectors.toList());
        assertThat(findProductIds).containsAll(expectProductIds);
    }
}
