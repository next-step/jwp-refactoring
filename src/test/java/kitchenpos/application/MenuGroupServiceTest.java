package kitchenpos.application;

import static org.mockito.Mockito.when;

import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.Fixture;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹이 저장된다.")
    @Test
    void create_menuGroup() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("치킨");

        when(menuGroupDao.save(menuGroup)).thenReturn(Fixture.치킨_메뉴그룹);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        Assertions.assertThat(savedMenuGroup).isEqualTo(Fixture.치킨_메뉴그룹);
    }

    @DisplayName("메뉴그룹들이 조회된다.")
    @Test
    void search_menuGroup() {
        // given
        when(menuGroupDao.findAll()).thenReturn(List.of(Fixture.치킨_메뉴그룹, Fixture.사이드_메뉴그룹));

        // when
        List<MenuGroup> searchedMenuGroups = menuGroupService.list();

        // then
        Assertions.assertThat(searchedMenuGroups).isEqualTo(List.of(Fixture.치킨_메뉴그룹, Fixture.사이드_메뉴그룹));
    }
}
