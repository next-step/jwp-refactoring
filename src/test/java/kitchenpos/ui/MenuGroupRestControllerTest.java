package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupRestControllerTest extends BaseControllerTest {

    @DisplayName("메뉴 그룹 등록")
    @Test
    void testCreateMenuGroup() throws Exception {
        // given
        String name = "추천메뉴";
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);

        // when & then
        mockMvc.perform(post("/api/menu-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroup)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(name));
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void testGetMenuGroups() throws Exception {
        mockMvc.perform(get("/api/menu-groups")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)));
    }
}
