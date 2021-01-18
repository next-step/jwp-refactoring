package kitchenpos.ui;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MenuGroupRestControllerTest extends ControllerTest {

    @Override
    @BeforeEach
    void setUp() {
        super.setUp();
    }

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    public void create() throws Exception {
        String body = objectMapper.writeValueAsString(new MenuGroup("세마리메뉴"));

        mockMvc.perform(post(MENU_GROUP_URI)
                .content(body)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andDo(print())
                .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    public void list() throws Exception {
        mockMvc.perform(get(MENU_GROUP_URI))
                .andDo(print())
                .andExpect(status().isOk());
    }

}