package kitchenpos.application;

import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
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

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        //given

        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("일반메뉴");

        // TODO: 임시로 any() 로 돌려놓음.
        given(menuGroupRepository.save(any()))
                .willReturn(new MenuGroup(1L, "일반메뉴"));

        //when
        MenuGroupResponse createMenuGroup = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(createMenuGroup.getId()).isNotNull();
        assertThat(createMenuGroup.getName()).isEqualTo("일반메뉴");
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(menuGroupRepository.findAll())
                .willReturn(
                        Arrays.asList(
                                new MenuGroup(1L, "일반메뉴"),
                                new MenuGroup(1L, "스페셜메뉴")
                        )
                );
        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups.size()).isEqualTo(2);
        assertThat(menuGroups.get(0).getName()).isEqualTo("일반메뉴");
        assertThat(menuGroups.get(1).getName()).isEqualTo("스페셜메뉴");
    }
}
