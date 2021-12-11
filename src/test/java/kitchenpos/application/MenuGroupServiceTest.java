package kitchenpos.application;

import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuGroupRepository;
import kitchenpos.dto.MenuGroupDto;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 치킨_메뉴그룹;

    @BeforeEach
    void setUp() {
        치킨_메뉴그룹 = MenuGroup.of("치킨");
        //치킨_메뉴그룹.setId(1L);
    }

    @DisplayName("메뉴그룹이 저장된다.")
    @Test
    void create_menuGroup() {
        // given
        when(menuGroupRepository.save(this.치킨_메뉴그룹)).thenReturn(this.치킨_메뉴그룹);

        // when
        MenuGroupDto savedMenuGroup = menuGroupService.create(MenuGroupDto.of(this.치킨_메뉴그룹));

        // then
        Assertions.assertThat(savedMenuGroup).isEqualTo(MenuGroupDto.of(this.치킨_메뉴그룹));
    }

    @DisplayName("메뉴그룹들이 조회된다.")
    @Test
    void search_menuGroup() {
        // given
        MenuGroup 사이드_메뉴그룹 = MenuGroup.of("사이드");
        //사이드_메뉴그룹.setId(2L);
        

        when(menuGroupRepository.findAll()).thenReturn(List.of(this.치킨_메뉴그룹, 사이드_메뉴그룹));

        // when
        List<MenuGroupDto> searchedMenuGroups = menuGroupService.list();

        // then
        Assertions.assertThat(searchedMenuGroups).isEqualTo(List.of(this.치킨_메뉴그룹, 사이드_메뉴그룹));
    }
}
