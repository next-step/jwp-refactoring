package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith({MockitoExtension.class})
class MenuGroupServiceTest {

    private static final String MENU_GROUP_NAME = "메뉴그룹";
    private final MenuGroup menuGroup = new MenuGroup();

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroup.setId(1L);
        menuGroup.setName(MENU_GROUP_NAME);
    }

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void create() {
        when(menuGroupDao.save(any(MenuGroup.class)))
            .thenReturn(menuGroup);

        MenuGroup saved = menuGroupService.create(menuGroup);

        assertThat(saved.getName()).isEqualTo(MENU_GROUP_NAME);
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void list() {
        when(menuGroupDao.findAll())
            .thenReturn(Arrays.asList(menuGroup));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertAll(() -> {
            assertThat(menuGroups.size()).isEqualTo(1);
            assertThat(menuGroups).extracting(MenuGroup::getName).containsExactly(MENU_GROUP_NAME);
        });
    }

    @Disabled
    @Test
    @DisplayName("메뉴 그룹 이름 없는 경우 실패")
    void createValidateName() {
        // TODO: 2021/12/17 이름 정보 필수이므로 비지니스 로직 추가 필요. 현재는 실패하는 테스트임.
        menuGroup.setName(null);
        assertThatThrownBy(() -> menuGroupService.create(menuGroup))
            .isInstanceOf(IllegalArgumentException.class);
    }
}