package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.dto.MenuGroupCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-08
 */
class MenuGroupRestControllerTest extends BaseControllerTest {

    @DisplayName("메뉴 그룹 생성")
    @Test
    public void menuGroupCreateTest() throws Exception {
        MenuGroupCreateRequest menuGroup = new MenuGroupCreateRequest("메뉴 그룹");

        mockMvc.perform(
                post("/api/menu-groups")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(menuGroup)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value("메뉴 그룹"));
    }


    @DisplayName("메뉴 그룹 조회")
    @Test
    public void menuGroupSelectTest() throws Exception {

        mockMvc.perform(get("/api/menu-groups")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

}
