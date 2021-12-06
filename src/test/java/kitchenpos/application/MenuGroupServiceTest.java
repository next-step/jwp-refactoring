package kitchenpos.application;

import kitchenpos.dao.InMemoryMenuGroupDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static kitchenpos.fixture.MenuGroupFixture.메뉴_그룹;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

/**
 * - 메뉴 그룹을 등록할 수 있다
 * - 메뉴 그룹 목록을 조회할 수 있다
 */
class MenuGroupServiceTest {

    private static final String 메뉴_그룹_이름 = "추천메뉴";
    private static final MenuGroup 메뉴_그룹 = 메뉴_그룹(메뉴_그룹_이름);

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

    @Test
    void create_메뉴_그룹_목록을_조회할_수_있다() {
        menuGroupService.create(메뉴_그룹);
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertAll(
                () -> assertThat(menuGroups.size()).isEqualTo(1),
                () -> assertThat(menuGroups.get(0).getName()).isEqualTo(메뉴_그룹_이름)
        );
    }

}