package kitchenpos.application;

import kitchenpos.AcceptanceTest;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

public class MenuGroupServiceTest extends AcceptanceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void createMenuGroup() {
        //given
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천메뉴");

        //when
        MenuGroupResponse result = menuGroupService.create(menuGroupRequest);

        //then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getName()).isEqualTo("추천메뉴");
    }

    @DisplayName("메뉴 그룹의 목록을 조회할 수 있다.")
    @Test
    void findAllMenuGroups() {
        //when
        List<MenuGroupResponse> results = menuGroupService.list();

        //then
        assertThat(results).isNotEmpty();
        assertThat(results.stream()
                .map(MenuGroupResponse::getName)
                .collect(Collectors.toList())).containsAll(Arrays.asList("두마리메뉴", "한마리메뉴", "순살파닭두마리메뉴", "신메뉴"));
    }
}
