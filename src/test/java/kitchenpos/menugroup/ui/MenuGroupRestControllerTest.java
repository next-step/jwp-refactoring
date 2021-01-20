package kitchenpos.menugroup.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.service.MenuGroupService;
import kitchenpos.menu.ui.MenuGroupRestController;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuGroupRestController.class)
class MenuGroupRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuGroupService menuGroupService;


    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void createMenuGroups() throws Exception {
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest("추천메뉴");
        MenuGroupResponse 추천메뉴 = new MenuGroupResponse(1L, "추천메뉴");
        when(menuGroupService.create(any())).thenReturn(추천메뉴);

        mockMvc.perform(post("/api/menu-groups")
                .content(objectMapper.writeValueAsString(menuGroupRequest)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(redirectedUrl("/api/menu-groups/" + 추천메뉴.getId()))
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 그룹 목록을 가져올수 있다.")
    @Test
    void listMenuGroup() throws Exception {
        when(menuGroupService.list()).thenReturn(Arrays.asList(
                new MenuGroupResponse(1L,"두마리메뉴"),
                new MenuGroupResponse(2L,"한마리메뉴")));

        mockMvc.perform(get("/api/menu-groups").contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("두마리메뉴", "한마리메뉴")));
    }
}