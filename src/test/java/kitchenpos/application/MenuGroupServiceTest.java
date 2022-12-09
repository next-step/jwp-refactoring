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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

//    - 메뉴 그룹을 등록할 수 있다.
//- 메뉴 그룹의 목록을 조회할 수 있다.

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void create_menu() {
        // given
        MenuGroup 추천메뉴 = create(1L, "추천메뉴");
        when(menuGroupDao.save(any())).thenReturn(추천메뉴);

        // when
        MenuGroup 추천메뉴_등록 = menuGroupService.create(추천메뉴);

        // then
        assertThat(추천메뉴_등록).isEqualTo(추천메뉴);
    }

    @Test
    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    void find_menus() {
        // given
        MenuGroup 추천메뉴 = create(1L, "추천메뉴");
        MenuGroup 요일메뉴 = create(1L, "요일메뉴");

        // when
        when(menuGroupService.list()).thenReturn(Arrays.asList(추천메뉴, 요일메뉴));
        List<MenuGroup> 메뉴_그룹 = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(메뉴_그룹).hasSize(2),
                () -> assertThat(메뉴_그룹).containsExactly(추천메뉴, 요일메뉴)
        );
    }

    private static MenuGroup create(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
