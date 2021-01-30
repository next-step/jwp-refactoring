package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup(1L,"메뉴그룹");
    }

    @DisplayName("메뉴 그룹을 생성")
    @Test
    void createMenuGroup() {
        // given
        when(menuGroupDao.save(menuGroup)).thenReturn(menuGroup);

        // when
        MenuGroup createdMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(createdMenuGroup.getId()).isEqualTo(menuGroup.getId());
        assertThat(createdMenuGroup.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹의 목록을 조회")
    @Test
    void selectMenuGroup() {
        // given
        when(menuGroupDao.findAll()).thenReturn(Collections.singletonList(menuGroup));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups.get(0).getId()).isEqualTo(menuGroup.getId());
        assertThat(menuGroups.get(0).getName()).isEqualTo(menuGroup.getName());
    }
}
