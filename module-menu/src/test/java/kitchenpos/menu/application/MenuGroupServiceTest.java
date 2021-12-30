package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴그룹을 등록한다..")
    void saveMenuTest(){
        MenuGroup menuGroup = new MenuGroup("치킨세트");
        MenuGroupRequest menuGroupRequest = Mockito.mock(MenuGroupRequest.class);
        Mockito.when(menuGroupRequest.toEntity()).thenReturn(menuGroup);
        Mockito.when(menuGroupRepository.save(ArgumentMatchers.any(MenuGroup.class))).thenReturn(menuGroup);

        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);
        assertThat(menuGroupResponse.getName()).isEqualTo("치킨세트");
    }

    @Test
    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    void saveNotEnrolledMenuTest(){
        MenuGroup menuGroup = Mockito.mock(MenuGroup.class);
        Mockito.when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(menuGroup));

        List<MenuGroupResponse> menuGroups = menuGroupService.findAll();

        Assertions.assertThat(menuGroups.size()).isEqualTo(1);
        Mockito.verify(menuGroupRepository).findAll();
    }

}
