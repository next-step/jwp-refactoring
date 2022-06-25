package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.ServiceTest;
import kitchenpos.application.helper.ServiceTestHelper;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MenuGroupServiceTest extends ServiceTest {
    @Autowired
    private ServiceTestHelper serviceTestHelper;

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    void 메뉴그룹생성() {
        String name = "메뉴그룹1";
        MenuGroup savedMenuGroup = serviceTestHelper.메뉴그룹_생성됨(name);
        assertThat(savedMenuGroup.getName()).isEqualTo(name);
    }

    @Test
    void 메뉴그룹조회() {
        serviceTestHelper.메뉴그룹_생성됨("메뉴그룹1");
        serviceTestHelper.메뉴그룹_생성됨("메뉴그룹2");
        List<MenuGroup> menuGroups = menuGroupService.list();
        assertThat(menuGroups).hasSize(2);
    }
}
