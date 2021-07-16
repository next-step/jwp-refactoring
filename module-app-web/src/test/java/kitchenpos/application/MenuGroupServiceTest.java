package kitchenpos.application;

import kitchenpos.menuGroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("메뉴그룹을 관리한다")
@SpringBootTest
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹를 등록할수 있다.")
    @Test
    void createTest() {
        // given
        String menuGroupName = "이름";
        MenuGroup menuGroup = new MenuGroup(menuGroupName);

        // when
        MenuGroup actualMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(actualMenuGroup).isNotNull();
        assertThat(actualMenuGroup.getName()).isEqualTo(menuGroupName);
    }

    @DisplayName("메뉴그룹들을 조회할수 있다.")
    @Test
    void selectTest() {
        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).isNotNull();
    }

}