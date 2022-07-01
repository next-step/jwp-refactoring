package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.utils.ServiceTestHelper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {
    @Autowired
    private ServiceTestHelper serviceTestHelper;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴그룹 생성")
    void 메뉴그룹생성() {
        String name = "메뉴그룹1";
        MenuGroup savedMenuGroup = serviceTestHelper.메뉴그룹_생성됨(name);
        assertThat(savedMenuGroup.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("메뉴그룹 조회")
    void 메뉴그룹조회() {
        serviceTestHelper.메뉴그룹_생성됨("메뉴그룹1");
        serviceTestHelper.메뉴그룹_생성됨("메뉴그룹2");
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).hasSize(2);
    }
}
