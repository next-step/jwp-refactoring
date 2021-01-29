package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@Transactional
class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;


    @Test
    @DisplayName("menu group 생성")
    void menu_create_test() {
        //given
        MenuGroupRequest menuGroupRequest = MENU_GROUP_REQUEST_생성("양식");
        //when
        MenuGroupResponse createdMenuGroup = MENU_GROUP_생성_테스트(menuGroupRequest);

        //then
        Assertions.assertAll(() -> {
            assertThat(createdMenuGroup.getId()).isNotNull();
            assertThat(createdMenuGroup.getName()).isEqualTo(menuGroupRequest.getName());
        });
    }

    @Test
    @DisplayName("menu group 리스트 조회")
    void menu_show_test() {
        //given
        MenuGroupResponse menuGroup1 = MENU_GROUP_생성_테스트(MENU_GROUP_REQUEST_생성("양식"));
        MenuGroupResponse menuGroup2 = MENU_GROUP_생성_테스트(MENU_GROUP_REQUEST_생성("중식"));

        //when
        List<MenuGroupResponse> list = menuGroupService.list();

        //then
        assertThat(list)
            .extracting(MenuGroupResponse::getName)
            .containsExactly(menuGroup1.getName(), menuGroup2.getName());

    }

    private MenuGroupResponse MENU_GROUP_생성_테스트(MenuGroupRequest menuGroupRequest) {
        return menuGroupService.create(menuGroupRequest);
    }

    private MenuGroupRequest MENU_GROUP_REQUEST_생성(String name) {
        return new MenuGroupRequest(name);
    }

}
