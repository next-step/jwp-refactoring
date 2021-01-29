package kitchenpos.menugroup;

import kitchenpos.BaseServiceTest;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.assertj.core.api.Assertions.assertThat;
import static kitchenpos.utils.TestHelper.등록되어_있지_않은_menuGroup_id;

class MenuGroupServiceTest extends BaseServiceTest {
    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    public void createMenuGroup() {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(등록되어_있지_않은_menuGroup_id, "핫메뉴");

        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        assertThat(menuGroupResponse.getId()).isEqualTo(menuGroupRequest.getId());
    }
}
