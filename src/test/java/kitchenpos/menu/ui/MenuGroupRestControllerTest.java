package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.dto.MenuGroupRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc
@SpringBootTest
class MenuGroupRestControllerTest {
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void post() throws Exception {
        // given
        String json = objectMapper.writeValueAsString(new MenuGroupRequest("메뉴그룹"));

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.post("/api/menu-groups")
                        .contentType(APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());
    }


    @Test
    void get() throws Exception {

        // when & then
        mockMvc.perform(MockMvcRequestBuilders.get("/api/menu-groups"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(4))
                .andDo(print());
    }
}
