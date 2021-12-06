package kitchenpos.application;

import kitchenpos.dao.InMemoryMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * - 메뉴 그룹을 등록할 수 있다
 * - 메뉴 그룹 목록을 조회할 수 있다
 */
class MenuGroupServiceTest {

    private static final String 메뉴_그룹_이름 = "추천메뉴";
    private static final MenuGroup 메뉴_그룹 = 메뉴(메뉴_그룹_이름);

    private MenuGroupDao menuGroupDao;
    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupDao = new InMemoryMenuGroupDao();
        menuGroupService = new MenuGroupService(menuGroupDao);
    }

    @Test
    void create_메뉴_그룹을_등록할_수_있다() {
        MenuGroup savedMenuGroup = menuGroupService.create(메뉴_그룹);
        assertThat(savedMenuGroup.getName()).isEqualTo(메뉴_그룹_이름);
    }

    private static MenuGroup 메뉴(String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}