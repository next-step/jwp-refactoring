package kitchenpos.menugroup;

import kitchenpos.common.ServiceTest;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.assertj.core.api.Assertions.*;


class MenuGroupServiceTest extends ServiceTest {

    @Autowired
    private MenuGroupService service;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        MenuGroupRequest request = new MenuGroupRequest("신메뉴");
        MenuGroupResponse response = service.create(request);

        assertAll(
                () -> assertThat(response.getId()).isNotNull(),
                () -> assertThat(response.getName()).isEqualTo("신메뉴")
        );
    }
}
