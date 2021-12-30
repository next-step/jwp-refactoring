package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

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
        MenuGroupRequest 반마리메뉴 = new MenuGroupRequest("반마리메뉴");
        // when
        // 메뉴 그룹을 등록한다.
        MenuGroupResponse savedMenuGroup = menuGroupService.create(반마리메뉴);
        // then
        // 메뉴 그룹이 정상적으로 등록된다.
        assertThat(savedMenuGroup.getName()).isEqualTo(반마리메뉴.getName());

        // when
        // 메뉴 그룹 리스트를 조회한다.
        List<MenuGroupResponse> savedMenuGroups = menuGroupService.list();
        // then
        // 메뉴 그룹 리스트가 정상적으로 조회된다.
        assertThat(savedMenuGroups).hasSize(5);
        assertThat(savedMenuGroups).contains(savedMenuGroup);
    }
}
