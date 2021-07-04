package kitchenpos.menu.service;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성 한다")
    public void createMenuGroup() {
        //given
        String name = "피자";
        MenuGroup menuGroup = new MenuGroup(name);

        //when
        MenuGroup createMenuGroup = menuGroupService.create(menuGroup);

        //then
        assertThat(createMenuGroup.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("메뉴 그룹 리스트를 가져온다")
    public void selectMenuGroupList() {
        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        for (MenuGroup menuGroup : menuGroups) {
            assertThat(menuGroup.getId()).isNotNull();
            assertThat(menuGroup.getName()).isNotNull();
        }
    }
}
