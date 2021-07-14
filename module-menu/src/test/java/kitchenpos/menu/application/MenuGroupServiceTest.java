package kitchenpos.menu.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴그룹 서비스 관련 테스트")
public class MenuGroupServiceTest {

    private MenuGroupService menuGroupService;

    private Long menuGroupId1 = 1L;
    private Long menuGroupId2 = 1L;
    private String menuGroupName1 = "메뉴그룹1";
    private String menuGroupName2 = "메뉴그룹2";

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @DisplayName("메뉴 그룹을 만들 수 있다")
    @Test
    void create() {
        MenuGroup menuGroup1 = MenuGroup.of(menuGroupId1, menuGroupName1);
        MenuGroupRequest menuGroupRequest = MenuGroupRequest.of(menuGroupName1);

        Mockito.when(menuGroupRepository.save(ArgumentMatchers.any())).thenReturn(menuGroup1);

        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        assertThat(menuGroupResponse.getId()).isEqualTo(menuGroupId1);
        assertThat(menuGroupResponse.getName()).isEqualTo(menuGroupName1);
    }

    @DisplayName("메뉴 그룹 전체를 조회할 수 있다")
    @Test
    void findAll() {
        MenuGroup menuGroup1 = MenuGroup.of(menuGroupId1, menuGroupName1);
        MenuGroup menuGroup2 = MenuGroup.of(menuGroupId2, menuGroupName2);

        Mockito.when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();

        Assertions.assertThat(menuGroupResponses.stream()
                .map(menuGroupResponse -> menuGroupResponse.getName())
                .collect(Collectors.toList()))
                .contains(menuGroup1.getName(), menuGroup2.getName());
    }

}
