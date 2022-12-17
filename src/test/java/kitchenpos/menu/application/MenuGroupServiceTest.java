package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupFactory;
import kitchenpos.menu.domain.MenuGroupRepository;
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
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    public void create() {
        //given
        MenuGroup 추천메뉴 = MenuGroupFactory.create(1L, "추천메뉴");
        given(menuGroupRepository.save(any())).willReturn(추천메뉴);

        //when
        menuGroupService.create(추천메뉴);

        //then
        then(menuGroupRepository).should().save(any());
    }

    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    @Test
    void list() {
        //given
        MenuGroup 추천메뉴 = MenuGroupFactory.create(1L, "추천메뉴");
        MenuGroup 오늘의메뉴 = MenuGroupFactory.create(2L, "오늘의메뉴");
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(추천메뉴, 오늘의메뉴));

        //when
        List<MenuGroupResponse> list = menuGroupService.list();

        //then
        List<Long> responseIds = list.stream()
                .map(MenuGroupResponse::getId)
                .collect(Collectors.toList());
        assertThat(responseIds).contains(추천메뉴.getId(), 오늘의메뉴.getId());
    }
}
