package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void 메뉴_그룹을_생성한다() {
        MenuGroup menuGroup = new MenuGroup(1L, "추천메뉴");

        when(menuGroupDao.save(any(MenuGroup.class)))
                .thenReturn(menuGroup);
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup).isNotNull();
    }

    @Test
    void 모든_메뉴_그룹을_조회한다() {
        MenuGroup menuGroup1 = new MenuGroup();
        MenuGroup menuGroup2 = new MenuGroup();
        when(menuGroupDao.findAll())
                .thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).containsAll(Arrays.asList(menuGroup1, menuGroup2));
    }
}
