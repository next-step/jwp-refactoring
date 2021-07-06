package kitchenpos.menu.controller;

import kitchenpos.common.ControllerTest;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuGroupControllerTest extends ControllerTest {

    @Test
    @DisplayName("메뉴 그룹을 생성 한다")
    public void createMenuGroup() throws Exception {
        // given
        String name = "피자";
        MenuGroupRequest menuGroupRequest = new MenuGroupRequest(name);

        // when
        // then
        메뉴_그룹_생성_요청(menuGroupRequest)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(name))
        ;
    }

    @Test
    @DisplayName("메뉴 그룹 리스트를 가져온다")
    public void selectMenuGroupList() throws Exception {
        // when
        // then
        메뉴_그룹_리스트_요청()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(4)))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("두마리메뉴"))
        ;
    }

    private ResultActions 메뉴_그룹_생성_요청(MenuGroupRequest menuGroupRequest) throws Exception {
        return mockMvc.perform(post("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroupRequest)))
                .andDo(print());
    }

    private ResultActions 메뉴_그룹_리스트_요청() throws Exception {
        return mockMvc.perform(get("/api/menu-groups")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
