package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹을 생선한다.")
    @Test
    void create() {
        // given
        MenuGroup menuGroup = MenuGroup.of(1L, "추천메뉴");
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);

        // when
        MenuGroup expected = menuGroupService.create(menuGroup);

        // then
        assertThat(menuGroup.getId()).isEqualTo(expected.getId());
    }

    @DisplayName("메뉴그룹 목록을 조회한다.")
    @Test
    void list() {

        // given
        MenuGroup menuGroup = MenuGroup.of(1L, "추천메뉴");
        MenuGroup menuGroup2 = MenuGroup.of(2L, "한마리메뉴");
        List<MenuGroup> actual = Arrays.asList(menuGroup, menuGroup2);
        when(menuGroupDao.findAll()).thenReturn(actual);

        // when
        List<MenuGroup> expected = menuGroupService.list();

        // then
        assertThat(actual.size()).isEqualTo(expected.size());
    }
}
