package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = createMenuGroup(null, "name");
        MenuGroup expected = createMenuGroup(1L, "name");
        Mockito.when(menuGroupDao.save(Mockito.any()))
            .thenReturn(expected);

        // when
        MenuGroup actual = menuGroupService.create(menuGroup);

        // then
        assertThat(actual).isSameAs(expected);
    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        // given
        MenuGroup menuGroup1 = createMenuGroup(1L, "name1");
        MenuGroup menuGroup2 = createMenuGroup(2L, "name2");
        List<MenuGroup> expected = Arrays.asList(menuGroup1, menuGroup2);
        Mockito.when(menuGroupDao.findAll())
            .thenReturn(expected);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).isSameAs(expected);
    }

    private MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);

        return menuGroup;
    }
}