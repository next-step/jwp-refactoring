package kitchenpos.menu.ui;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.common.fixtrue.MenuGroupFixture;
import kitchenpos.menu.ui.MenuGroupRestController;
import kitchenpos.ui.RestControllerTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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

        MenuGroup 두마리메뉴 = MenuGroupFixture.of(1L, "두마리메뉴");
        given(menuGroupService.create(any())).willReturn(두마리메뉴);

        // when
        ResultActions actions = mockMvc.perform(post(API_MENU_GROUP_ROOT)
                        .content(asJsonString(두마리메뉴))
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        actions
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(두마리메뉴.getId()))
                .andExpect(jsonPath("$.name").value(두마리메뉴.getName()));
    }

    @Test
    void 메뉴_그룹_조회() throws Exception {
        // given
        List<MenuGroup> menuGroups = new ArrayList<>();
        menuGroups.add(MenuGroupFixture.of(1L, "두마리메뉴"));
        menuGroups.add(MenuGroupFixture.of(2, "한마리메뉴"));

        given(menuGroupService.list()).willReturn(menuGroups);

        // when
        ResultActions actions = mockMvc.perform(get(API_MENU_GROUP_ROOT)
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());

        // then
        actions
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(menuGroups.get(0).getId()))
                .andExpect(jsonPath("$[0].name").value(menuGroups.get(0).getName()))
                .andExpect(jsonPath("$[1].id").value(menuGroups.get(1).getId()))
                .andExpect(jsonPath("$[1].name").value(menuGroups.get(1).getName()));
    }
}
