package kichenpos.menu.application;

import kichenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Transactional
@SpringBootTest
class MenuGroupServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴그룹을_생성한다() {
        // given
        MenuGroup menuGroup = createMenuGroup();

        // when
        MenuGroup result = menuGroupService.create(menuGroup);

        // then
        assertThat(result.getId()).isNotNull();
    }

    @Test
    void 메뉴그룹_목록을_조회한다() {
        // given
        MenuGroup saved = menuGroupService.create(createMenuGroup());

        // when
        List<MenuGroup> result = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(result).hasSizeGreaterThan(0),
                () -> assertThat(result).contains(saved)
        );
    }

    private MenuGroup createMenuGroup() {
        return new MenuGroup("세마리 메뉴");
    }
}
