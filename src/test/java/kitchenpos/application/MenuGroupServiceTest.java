package kitchenpos.application;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class MenuGroupServiceTest extends BaseTest{
    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 등록")
    @Test
    void create() {
        List<MenuGroup> menuGroups = menuGroupService.list();
        MenuGroup expected = menuGroups.get(0);

        MenuGroup actual = menuGroupService.create(expected);

        assertThat(actual.getName()).isEqualTo(expected.getName());
    }
}