package kitchenpos.menu.service;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);

        //when
        MenuGroupResponse menuGroupResponse = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(menuGroupResponse.getName()).isEqualTo(name);
    }

    @Test
    @DisplayName("메뉴 그룹 리스트를 가져온다")
    public void selectMenuGroupList() {
        //when
        List<MenuGroupResponse> menuGroupResponses = menuGroupService.list();

        //then
        for (MenuGroupResponse menuGroupResponse : menuGroupResponses) {
            assertThat(menuGroupResponse.getId()).isNotNull();
            assertThat(menuGroupResponse.getName()).isNotNull();
        }
    }
}
