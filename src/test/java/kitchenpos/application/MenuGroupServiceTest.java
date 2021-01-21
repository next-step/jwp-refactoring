package kitchenpos.application;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        //given
        ArgumentCaptor<MenuGroup> argumentCaptor = ArgumentCaptor.forClass(MenuGroup.class);
        MenuGroup menuGroup = new MenuGroup("일반메뉴");
        ReflectionTestUtils.setField(menuGroup, "id", 1L);

        given(menuGroupRepository.save(any()))
                .willReturn(menuGroup);

        //when
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("일반메뉴");
        MenuGroupResponse createMenuGroup = menuGroupService.create(menuGroupRequest);

        //then
        verify(menuGroupRepository).save(argumentCaptor.capture());
        assertThat(argumentCaptor.getValue().getId()).isNull();
        assertThat(argumentCaptor.getValue().getName()).isEqualTo("일반메뉴");

        assertThat(createMenuGroup.getId()).isNotNull();
        assertThat(createMenuGroup.getName()).isEqualTo("일반메뉴");
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        MenuGroup menuGroup1 = new MenuGroup("일반메뉴");
        MenuGroup menuGroup2 = new MenuGroup("스페셜메뉴");
        ReflectionTestUtils.setField(menuGroup1, "id", 1L);
        ReflectionTestUtils.setField(menuGroup2, "id", 2L);

        given(menuGroupRepository.findAll())
                .willReturn(Arrays.asList(menuGroup1, menuGroup2));
        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups.size()).isEqualTo(2);
        assertThat(menuGroups.get(0).getName()).isEqualTo("일반메뉴");
        assertThat(menuGroups.get(1).getName()).isEqualTo("스페셜메뉴");
    }
}
