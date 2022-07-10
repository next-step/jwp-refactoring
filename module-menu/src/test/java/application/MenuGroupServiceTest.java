package application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.application.MenuGroupService;
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

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        final String name = "치킨";
        MenuGroupRequest request = new MenuGroupRequest(name);
        MenuGroup 치킨 = new MenuGroup(name);
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(치킨);

        MenuGroupResponse response = menuGroupService.create(request);

        assertThat(response.getName()).isEqualTo(name);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        final String name = "치킨";
        MenuGroup 치킨 = new MenuGroup(name);
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(치킨));

        List<MenuGroupResponse> responses = menuGroupService.list();

        assertThat(responses.size()).isEqualTo(1);
        assertThat(responses.get(0).getName()).isEqualTo(name);
    }
}
