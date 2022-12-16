package kitchenpos.menu.application;

import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.domain.Name;
import kitchenpos.menu.application.MenuGroupService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@DisplayName("메뉴 그룹 테스트")
@SpringBootTest
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        final String menuName = "추천 메뉴";
        final MenuGroup menuGroup = 메뉴_그룹(menuName);
        //when, then:
        assertThat(menuGroupService.create(menuGroup)).isEqualTo(메뉴_그룹(menuName));
    }

    /**
     * source :
     * INSERT INTO menu_group (id, name) VALUES (1, '두마리메뉴');
     * INSERT INTO menu_group (id, name) VALUES (2, '한마리메뉴');
     * INSERT INTO menu_group (id, name) VALUES (3, '순살파닭두마리메뉴');
     * INSERT INTO menu_group (id, name) VALUES (4, '신메뉴');
     */
    @DisplayName("조회 성공")
    @Test
    void 조회_성공() {
        assertThat(menuGroupService.list()).hasSize(4);
    }

    public static MenuGroup 메뉴_그룹(String name) {
        return MenuGroup.from(Name.from(name));
    }
}
