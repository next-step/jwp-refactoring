package api.menu.application;

import api.DataBaseCleanSupport;
import api.menu.dto.MenuGroupRequest;
import api.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("메뉴 그룹 관리")
class MenuGroupServiceTest extends DataBaseCleanSupport {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 추가한다.")
    @Test
    void create() {
        //given
        MenuGroupRequest 착한세트 = MenuGroupRequest.of("착한세트");

        //when
        MenuGroupResponse actual = menuGroupService.create(착한세트);

        //then
        assertThat(actual.getId()).isNotNull();
        assertThat(actual.getName()).isEqualTo(착한세트.getName());
    }

    @DisplayName("메뉴 그룹을 모두 조회한다.")
    @Test
    void list() {
        //when
        List<MenuGroupResponse> actual = menuGroupService.findMenuGroupResponses();

        //then
        assertThat(actual.size()).isNotZero();
    }
}
