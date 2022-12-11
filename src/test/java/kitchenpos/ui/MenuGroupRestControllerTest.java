package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class MenuGroupRestControllerTest extends BaseTest {
    @Test
    void 생성() throws Exception {
        String content = objectMapper.writeValueAsString(new MenuGroup("한마리메뉴"));

        생성_요청(content);
    }

    @Test
    void 조회() throws Exception {
        조회_요청();
    }

    private void 생성_요청(String content) throws Exception {
        mockMvc.perform(post("/api/menu-groups")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    private void 조회_요청() throws Exception {
        mockMvc.perform(get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
