package kitchenpos.application.menu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.menu.MenuGroupRequest;
import kitchenpos.dto.menu.MenuGroupResponse;
import org.assertj.core.util.Lists;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroup() {
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");
        MenuGroupRequest request = new MenuGroupRequest("한마리메뉴");
        when(menuGroupRepository.save(any())).thenReturn(menuGroup);

        MenuGroupResponse actual = menuGroupService.create(request);

        assertThat(actual.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void findAll() {
        MenuGroup menuGroup = MenuGroup.from("한마리메뉴");
        MenuGroup otherMenuGroup = MenuGroup.from("두마리메뉴");
        when(menuGroupRepository.findAll()).thenReturn(Lists.list(menuGroup, otherMenuGroup));

        List<MenuGroupResponse> actual = menuGroupService.list();
        List<String> actualNames = actual.stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList());

        assertThat(actualNames).containsExactly(menuGroup.getName(), otherMenuGroup.getName());
    }
}
