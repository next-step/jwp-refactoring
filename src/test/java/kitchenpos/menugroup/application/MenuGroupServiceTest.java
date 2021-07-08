package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.utils.domain.MenuGroupObjects;

@DisplayName("메뉴그룹 서비스")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroupObjects menuGroupObjects;
    private MenuGroup menuGroup1;

    @BeforeEach
    void setUp() {
        menuGroupObjects = new MenuGroupObjects();
        menuGroup1 = menuGroupObjects.getMenuGroup1();
    }

    @Test
    @DisplayName("메뉴그룹을 생성한다.")
    void create_menuGroup() {
        // given
        MenuGroup insertMenuGroup = new MenuGroup();
        menuGroup1.setName(menuGroup1.getName());

        // mocking
        when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(menuGroupObjects.getMenuGroup1());

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup1);

        // then
        assertThat(savedMenuGroup).isSameAs(menuGroup1);
    }

    @Test
    @DisplayName("메뉴그룹 목록을 조회한다.")
    void find_menuGroupList() {
        // given
        List<MenuGroup> menuGroups = menuGroupObjects.getMenuGroups();

        // mocking
        when(menuGroupService.list()).thenReturn(menuGroups);

        // when
        List<MenuGroup> resultMenuGroups = menuGroupService.list();

        // then
        assertThat(resultMenuGroups).isSameAs(menuGroups);
    }
}
