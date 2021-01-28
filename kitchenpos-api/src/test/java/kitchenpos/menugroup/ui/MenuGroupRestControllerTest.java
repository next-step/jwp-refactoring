package kitchenpos.menugroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menugroup.application.MenuGroupService;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void create() throws Exception {
        MenuGroupResponse menuGroup1 = new MenuGroupResponse(1L, "menuGroup1");
        MenuGroupResponse menuGroup2 = new MenuGroupResponse(2L, "menuGroup2");
        when(menuGroupService.list()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        mockMvc.perform(get("/api/menu-groups").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("menuGroup1", "menuGroup2")));
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() throws Exception {
        MenuGroupResponse menuGroup1 = new MenuGroupResponse(1L, "menuGroup1");
        MenuGroupResponse menuGroup2 = new MenuGroupResponse(2L, "menuGroup2");

        when(menuGroupService.list()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        mockMvc.perform(get("/api/menu-groups"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("menuGroup1", "menuGroup2")));
    }

}
