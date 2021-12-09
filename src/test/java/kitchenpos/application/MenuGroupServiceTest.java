package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.fixture.MenuGroupFixture;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("MenuGroup 을 등록한다.")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("고기 메뉴그룹");

        when(menuGroupDao.save(menuGroup)).thenReturn(MenuGroupFixture.고기_메뉴그룹);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(savedMenuGroup).isEqualTo(MenuGroupFixture.고기_메뉴그룹);
    }

    @DisplayName("MenuGroup 목록을 조회한다.")
    @Test
    void findList() {
        // given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(MenuGroupFixture.고기_메뉴그룹, MenuGroupFixture.야채_메뉴그룹));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).containsExactly(MenuGroupFixture.고기_메뉴그룹, MenuGroupFixture.야채_메뉴그룹);
    }
}