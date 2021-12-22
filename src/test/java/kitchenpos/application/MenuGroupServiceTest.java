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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@DisplayName("메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 베스트메뉴;
    private MenuGroup 세트메뉴;

    @BeforeEach
    void setUp() {
        베스트메뉴 = createMenuGroup(1L, "베스트메뉴");
        세트메뉴 = createMenuGroup(2L, "세트메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void create() {
        when(menuGroupDao.save(any())).thenReturn(베스트메뉴);

        MenuGroup menuGroup = menuGroupService.create(베스트메뉴);

        verify(menuGroupDao, times(1)).save(any(MenuGroup.class));
        assertThat(menuGroup.getName()).isEqualTo(베스트메뉴.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void list() {
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(베스트메뉴, 세트메뉴));

        List<MenuGroup> menuGroups = menuGroupService.list();

        verify(menuGroupDao, times(1)).findAll();
        assertThat(menuGroups).hasSize(2);
    }

    private MenuGroup createMenuGroup(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}