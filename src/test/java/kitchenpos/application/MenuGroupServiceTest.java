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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴그룹")
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroup menuGroup1;
    private MenuGroup menuGroup2;
    private MenuGroup menuGroup3;

    @BeforeEach
    void setUp() {
        menuGroup1 = generateMenuGroup(1L, "menuGroup1");
        menuGroup2 = generateMenuGroup(2L, "menuGroup2");
        menuGroup3 = generateMenuGroup(3L, "menuGroup3");
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    void menuGroupTest1() {
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup1, menuGroup2, menuGroup3));

        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups.size()).isEqualTo(3);
    }

    @Test
    @DisplayName("새로운 메뉴 그룹을 추가할 수 있다.")
    void menuGroupTest2() {
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(menuGroup1);

        MenuGroup menuGroup = menuGroupService.create(menuGroup1);
        assertThat(menuGroup.getName()).isEqualTo(menuGroup1.getName());
    }

    public static MenuGroup generateMenuGroup(Long id, String name) {
        return MenuGroup.of(id, name);
    }

}