package kitchenpos.menu.group;

import kitchenpos.menu.group.domain.MenuGroup;
import kitchenpos.menu.group.dto.MenuGroupRequest;
import kitchenpos.menu.group.dto.MenuGroupResponse;
import kitchenpos.menu.group.ui.MenuGroupController;
import kitchenpos.utils.ControllerTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;

import javax.annotation.PostConstruct;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class MenuGroupControllerTest extends ControllerTest {

    @PostConstruct
    public void setUp() {
        this.mockMvc = MockMvcBuilders.standaloneSetup(new MenuGroupController(menuGroupService)).build();
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() throws Exception {

        //given
        final String menuGroupName = "중화요리";
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest();
        ReflectionTestUtils.setField(menuGroupRequest, "name", menuGroupName);
        MenuGroupResponse menuGroupResponse = MenuGroupResponse.of(menuGroupRequest.toEntity());
        when(menuGroupService.create(any())).thenReturn(menuGroupResponse);

        //when
        ResultActions resultActions = post("/api/menu-groups", menuGroupRequest);

        //then
        resultActions.andExpect(status().isCreated());
    }

    @DisplayName("메뉴 그룹 리스트를 조회한다.")
    @Test
    void getMenuGroups() throws Exception {

        //given
        MenuGroup 중화요리 = MenuGroup.create("중화요리");
        MenuGroup 일식 = MenuGroup.create("일식");
        List<MenuGroupResponse> 메뉴리스트 = MenuGroupResponse.ofList(Arrays.asList(중화요리, 일식));
        when(menuGroupService.list()).thenReturn(메뉴리스트);

        //when
        ResultActions resultActions = get("/api/menu-groups", new LinkedMultiValueMap<>());

        //then
        resultActions.andExpect(status().isOk());
        resultActions.andExpect(jsonPath("$").isArray());
        resultActions.andExpect(jsonPath("$[0]['name']").isString());
        resultActions.andExpect(jsonPath("$[1]['name']").isString());
    }
}
