package kitchenpos.menu.acceptance;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static kitchenpos.domain.MenuGroupTest.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@DisplayName("메뉴 그룹 통합테스트")
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹 관리")
    public void menuGroupManage() {
        // given
        // 메뉴 그룹 생성
        MenuGroup 반마리메뉴 = new MenuGroup("반마리메뉴");
        // when
        // 메뉴 그룹을 등록한다.
        MenuGroup savedMenuGroup = menuGroupService.create(반마리메뉴);
        // then
        // 메뉴 그룹이 정상적으로 등록된다.
        assertThat(savedMenuGroup).isEqualTo(반마리메뉴);

        // when
        // 메뉴 그룹을 조회한다.
        List<MenuGroup> savedMenuGroups = menuGroupService.list();
        // then
        // 메뉴 그룹이 정상적으로 조회된다.
        assertThat(savedMenuGroups).hasSize(5);
        assertThat(savedMenuGroups).containsExactly(두마리메뉴, 한마리메뉴, 순살파닭두마리메뉴, 신메뉴, 반마리메뉴);
    }
}
