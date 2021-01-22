package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.dto.MenuGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static kitchenpos.common.Fixtures.menuGroupRequest;
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
    void testManageMenuGroup() throws Exception {
        메뉴_그룹_등록();

        메뉴_그룹_추가됨_목록_조회();
    }

    void 메뉴_그룹_등록() throws Exception {
        // given
        MenuGroupRequest menuGroupRequest = menuGroupRequest();

        // when & then
        mockMvc.perform(post("/api/menu-groups")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuGroupRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(menuGroupRequest.getName()));
    }

    void 메뉴_그룹_추가됨_목록_조회() throws Exception {
        mockMvc.perform(get("/api/menu-groups")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(5)));
    }
}
