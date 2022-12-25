package kitchenpos.menu.application;

import static kitchenpos.menu.MenuGroupFixture.createRequest;
import static kitchenpos.menu.MenuGroupFixture.추천메뉴;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
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
    @DisplayName("메뉴 그룹 생성")
    void createMenuGroup() {
        //given
        when(menuGroupRepository.save(any())).thenReturn(추천메뉴);

        //when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(createRequest(추천메뉴));

        //then
        assertThat(menuGroupResponse).isEqualTo(new MenuGroupResponse(1L, "추천메뉴"));
    }

    @Test
    @DisplayName("메뉴명이 없으면 그룹 생성중 에러 발생")
    void noNameException() {
        //given
        MenuGroupRequest 메뉴명없음 = new MenuGroupRequest(null);

        //when & then
        assertThatThrownBy(() -> menuGroupService.create(메뉴명없음))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("메뉴 그룹의 이름을 입력해 주세요");
    }

    @Test
    @DisplayName("메뉴 목록 조회")
    void getMenuGroup() {
        //given
        when(menuGroupRepository.findAll()).thenReturn(Collections.singletonList(추천메뉴));

        //when
        List<MenuGroupResponse> list = menuGroupService.list();

        //then
        assertThat(list)
            .hasSize(1)
            .containsExactly(new MenuGroupResponse(1L, "추천메뉴"));
    }
}