package kitchenpos.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.common.BaseContollerTest;
import kitchenpos.menu.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuGroupControllerTest extends BaseContollerTest {

    @Test
    @DisplayName("새로운 메뉴 그룹을 등록합니다.")
    void createMenuGroup() throws Exception {
        this.mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(
                        MenuGroupTestSupport.createMenuGroup("새로운메뉴")))
                )
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @Test
    @DisplayName("모든 메뉴 그룹을 조회합니다.")
    void findAllMenuGroup() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").exists())
                .andReturn();

        String responseMenuGroups = mvcResult.getResponse().getContentAsString();
        ArrayList<MenuGroup> menuGroups
                = this.objectMapper.readValue(responseMenuGroups, new TypeReference<ArrayList<MenuGroup>>() {});

        assertThat(menuGroups).hasSize(4);
    }
}
