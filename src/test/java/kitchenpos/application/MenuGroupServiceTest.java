package kitchenpos.application;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.menu.MenuGroupService;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.MenuGroupDto;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹이 저장된다.")
    @Test
    void create_menuGroup() {
        // given
        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");
        when(menuGroupRepository.save(any(MenuGroup.class))).thenReturn(치킨_메뉴그룹);

        // when
        MenuGroupDto savedMenuGroup = menuGroupService.create(MenuGroupDto.of("치킨"));

        // then
        Assertions.assertThat(savedMenuGroup).isEqualTo(MenuGroupDto.of("치킨"));
    }

    @DisplayName("메뉴그룹들이 조회된다.")
    @Test
    void search_menuGroup() {
        // given
        MenuGroup 치킨_메뉴그룹 = MenuGroup.of("치킨");
        MenuGroup 사이드_메뉴그룹 = MenuGroup.of("사이드");
        
        when(menuGroupRepository.findAll()).thenReturn(List.of(치킨_메뉴그룹, 사이드_메뉴그룹));

        // when
        List<MenuGroupDto> searchedMenuGroups = menuGroupService.list();

        // then
        Assertions.assertThat(searchedMenuGroups).hasSize(2)
                                                    .contains(MenuGroupDto.of(치킨_메뉴그룹), MenuGroupDto.of(사이드_메뉴그룹));
    }
}
