package kitchenpos.menu.application;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.repository.MenuGroupRepository;
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

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void createMenuGroup() {
        //given
        MenuGroupRequest request = new MenuGroupRequest("양식");
        given(menuGroupRepository.save(MenuGroup.from(request.getName()))).willReturn(MenuGroup.from("양식"));

        //when
        MenuGroupResponse saveMenuGroup = menuGroupService.create(request);

        //then
        assertThat(saveMenuGroup.getName()).isEqualTo(request.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회 한다.")
    void retriedMenuGroups() {
        //given
        MenuGroup 양식 = MenuGroup.from("양식");
        MenuGroup 일식 = MenuGroup.from("일식");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(양식, 일식));

        //when
        List<MenuGroupResponse> menuGroups = menuGroupService.list();

        //then
        assertAll("메뉴 그룹 목록을 조회 한다.",
                () -> assertThat(menuGroups).hasSize(2),
                () -> assertThat(menuGroups).extracting("name").contains("양식", "일식")
        );

    }

}
