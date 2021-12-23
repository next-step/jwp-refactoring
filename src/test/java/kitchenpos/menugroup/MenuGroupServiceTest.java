package kitchenpos.menugroup;

import kitchenpos.AcceptanceTest;
import kitchenpos.application.MenuGroupService;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupServiceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Autowired
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void createMenuGroup() {
        // given
        final MenuGroup menuGroup = new MenuGroup("추천메뉴");

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo("추천메뉴")
        );
    }

    @Test
    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    void findMenuGroup() {
        // given
        menuGroupDao.save(new MenuGroup("추천메뉴1"));
        menuGroupDao.save(new MenuGroup("추천메뉴2"));

        // when
        List<MenuGroup> findByMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(findByMenuGroups.size()).isEqualTo(2),
                () -> assertThat(findByMenuGroups).extracting("name").contains("추천메뉴1","추천메뉴2")
        );
    }
}
