package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Collections;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹에_메뉴를_등록할_수_있다() {
        MenuGroup menuGroup = new MenuGroup(1L, "name");
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);

        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        assertThat(savedMenuGroup).isEqualTo(menuGroup);
    }

    @Test
    void 그룹이_등록된_메뉴들을_조회할_수_있다() {
        MenuGroup menuGroup = new MenuGroup(1L, "name");
        when(menuGroupDao.findAll()).thenReturn(Collections.singletonList(menuGroup));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).hasSize(1);
        assertThat(menuGroups).contains(menuGroup);
    }
}