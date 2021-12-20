package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith({MockitoExtension.class})
class MenuGroupServiceTest {

    private static final String MENU_GROUP_NAME = "메뉴그룹";
    private final MenuGroup menuGroup = MenuGroup.from(MENU_GROUP_NAME);

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;


    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void create() {
        when(menuGroupRepository.save(any(MenuGroup.class)))
            .thenReturn(menuGroup);

        MenuGroupResponse response = menuGroupService.create(new MenuGroupRequest(MENU_GROUP_NAME));

        assertThat(response.getName()).isEqualTo(MENU_GROUP_NAME);
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void list() {
        when(menuGroupRepository.findAll())
            .thenReturn(Arrays.asList(menuGroup));

        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertAll(
            () -> assertThat(menuGroups.size()).isEqualTo(1),
            () -> assertThat(menuGroups).extracting(MenuGroupResponse::getName).containsExactly(MENU_GROUP_NAME)
        );
    }

}