package kitchenpos.ui;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class MenuGroupRestControllerTest extends ControllerTest {

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    public void create() throws Exception {
        mockMvc.perform(post(MENU_GROUP_URI))
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