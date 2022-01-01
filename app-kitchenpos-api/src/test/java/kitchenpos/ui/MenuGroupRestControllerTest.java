package kitchenpos.ui;

import kitchenpos.application.MenuGroupService;
import kitchenpos.domain.MenuGroup;
import kitchenpos.dto.MenuGroupRequest;
import kitchenpos.dto.MenuGroupResponse;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest extends RestControllerTest {

    private static final String API_MENU_GROUP_ROOT = "/api/menu-groups";

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MenuGroupService menuGroupService;

    @Test
    void 메뉴_그룹_생성() throws Exception {
        // given

        MenuGroupRequest 두마리메뉴_요청 = MenuGroupRequest.from("두마리메뉴");
        MenuGroupResponse 두마리메뉴_응답 = MenuGroupResponse.from(MenuGroup.from(두마리메뉴_요청.getName()));
        BDDMockito.given(menuGroupService.create(ArgumentMatchers.any())).willReturn(두마리메뉴_응답);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.post(API_MENU_GROUP_ROOT)
                        .content(asJsonString(두마리메뉴_요청))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        // then
        actions
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id").value(두마리메뉴_응답.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name").value(두마리메뉴_응답.getName()));
    }

    @Test
    void 메뉴_그룹_조회() throws Exception {
        // given
        List<MenuGroupResponse> menuGroups = new ArrayList<>();
        menuGroups.add(MenuGroupResponse.from(MenuGroup.from("한마리메뉴")));
        menuGroups.add(MenuGroupResponse.from(MenuGroup.from("두마리메뉴")));

        BDDMockito.given(menuGroupService.list()).willReturn(menuGroups);

        // when
        ResultActions actions = mockMvc.perform(MockMvcRequestBuilders.get(API_MENU_GROUP_ROOT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(MockMvcResultHandlers.print());

        // then
        actions
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id").value(menuGroups.get(0).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name").value(menuGroups.get(0).getName()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].id").value(menuGroups.get(1).getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].name").value(menuGroups.get(1).getName()));
    }
}
