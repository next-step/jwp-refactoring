package kitchenpos.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menugroup.port.MenuGroupPort;
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
    private MenuGroupPort menuGroupPort;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그륩을 등록한다")
    void createMenuGroup() {
        MenuGroup 피자 = new MenuGroup("피자");

        given(menuGroupPort.save(any())).willReturn(피자);

        MenuGroupResponse result = menuGroupService.create(new MenuGroupRequest("피자"));

        assertThat(result.getName()).isEqualTo("피자");
    }

    @Test
    @DisplayName("메뉴 그륩 리스트를 받아온다")
    void getMenuGroupList() {
        MenuGroup 피자 = new MenuGroup("피자");
        MenuGroup 치킨 = new MenuGroup("치킨");

        given(menuGroupPort.findAll()).willReturn(Arrays.asList(피자, 치킨));

        List<MenuGroup> result = menuGroupService.list();

        assertThat(result).hasSize(2);
        assertThat(result).contains(치킨, 피자);
    }
}
