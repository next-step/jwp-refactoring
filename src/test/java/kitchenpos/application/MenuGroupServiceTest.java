package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.*;

class MenuGroupServiceTest {

    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;

    public static MenuGroup menuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }

    @BeforeEach
    void setUp() {
        menuGroupDao = mock(MenuGroupDao.class);
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    public void create() {
        //given
        MenuGroup menuGroup = menuGroup(1L, "group");
        when(menuGroupDao.save(any(MenuGroup.class))).thenReturn(menuGroup);

        //when
        MenuGroup actual = menuGroupService.create(menuGroup);

        //then
        verify(menuGroupDao).save(menuGroup);
        assertThat(actual).isEqualTo(menuGroup);
    }

    @Test
    public void findAll() {
        //given
        MenuGroup menuGroup = menuGroup(1L, "group");
        MenuGroup menuGroup2 = menuGroup(2L, "group2");
        when(menuGroupDao.findAll()).thenReturn(Lists.newArrayList(menuGroup, menuGroup2));

        //when
        List<MenuGroup> actual = menuGroupService.list();

        //then
        verify(menuGroupDao).findAll();
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(menuGroup, menuGroup2)
                );
    }

}
