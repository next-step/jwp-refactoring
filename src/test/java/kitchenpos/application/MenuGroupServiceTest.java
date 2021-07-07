package kitchenpos.application;

import kitchenpos.menugroup.domain.MenuGroupDao;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.application.MenuGroupService;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = new MenuGroup(5L, "기타안주메뉴");
    }

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() {
        given(menuGroupDao.save(any())).willReturn(menuGroup);

        MenuGroup created = menuGroupService.create(menuGroup);

        assertThat(created.getName()).isEqualTo(menuGroup.getName());
    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void list() {
        MenuGroup anotherGroup = new MenuGroup(6L, "디저트");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(menuGroup, anotherGroup));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(
                () -> assertThat(menuGroups).isNotEmpty(),
                () -> assertThat(menuGroups).containsExactly(menuGroup, anotherGroup)
        );
    }
}
