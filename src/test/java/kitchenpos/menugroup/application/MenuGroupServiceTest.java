package kitchenpos.menugroup.application;

import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroupRequest menuGroupRequest;
    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroupRequest = new MenuGroupRequest("기타안주메뉴");
        menuGroup = new MenuGroup(1L, "기타안주메뉴");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        given(menuGroupRepository.save(any())).willReturn(menuGroup);

        MenuGroupResponse created = menuGroupService.create(menuGroupRequest);

        assertThat(created.getName()).isEqualTo(menuGroup.getName());

        verify(menuGroupRepository, times(1)).save(any());
    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void list() {
        MenuGroup anotherGroup = new MenuGroup(6L, "디저트");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup, anotherGroup));

        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).isNotEmpty(),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo(MenuGroupResponse.from(menuGroup).getName()),
                () -> assertThat(menuGroups.get(1).getName()).isEqualTo(MenuGroupResponse.from(anotherGroup).getName())
        );

        verify(menuGroupRepository, times(1)).findAll();
    }
}
