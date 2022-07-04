package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupRequest;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class MenuGroupServiceTest {

    @Autowired
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    public void createMenuGroup() {
        //given
        MenuGroupRequest menuGroup = MenuGroupRequest.from("메뉴그룹");
        //when
        MenuGroupResponse result = menuGroupService.create(menuGroup);
        //then
        assertThat(result).isNotNull();
    }

    @DisplayName("메뉴 그룹 리스트를 가져온다.")
    @Test
    public void getMenuGroups(){
        //given
        menuGroupService.create(MenuGroupRequest.from("메뉴그룹"));
        //when
        List<MenuGroupResponse> results = menuGroupService.list();
        //then
        assertThat(results).hasSizeGreaterThan(1);
    }
}
