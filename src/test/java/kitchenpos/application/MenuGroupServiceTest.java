package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup(1L, "두마리메뉴");
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);
    }

    @DisplayName("메뉴 그룹을 생성한다")
    @Test
    void createTest() {
        // when
        MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);
        MenuGroup returnedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(returnedMenuGroup).isEqualTo(menuGroup);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void listTest() {
        // given
        when(menuGroupDao.findAll()).thenReturn(Collections.singletonList(menuGroup));

        // when
        MenuGroupService menuGroupService = new MenuGroupService(menuGroupDao);
        menuGroupService.create(menuGroup);
        List<MenuGroup> list = menuGroupService.list();

        assertThat(list).contains(menuGroup);
    }
}
