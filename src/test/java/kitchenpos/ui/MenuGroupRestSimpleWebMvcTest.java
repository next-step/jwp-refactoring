package kitchenpos.ui;

import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class MenuGroupRestSimpleWebMvcTest extends RestControllerTest {

    public static final String MENU_GROUPS_URL = "/api/menu-groups";

    @Test
    void create() throws Exception {
        //given
        MenuGroup 일반메뉴요청 = new MenuGroup(null, "일반메뉴");

        //when
        //then
        mockMvc.perform(
                post(MENU_GROUPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(일반메뉴요청))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrlPattern("/api/menu-groups/*"))
                .andExpect(jsonPath("$.name", is("일반메뉴")));
    }


    @Test
    void list() throws Exception {
        //given
        //when
        //then
        mockMvc.perform(
                get("/api/menu-groups")
                .characterEncoding("utf-8")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(4)))
                .andExpect(jsonPath("$[0]['name']", is("두마리메뉴")))
                .andExpect(jsonPath("$[1]['name']", is("한마리메뉴")))
                .andExpect(jsonPath("$[2]['name']", is("순살파닭두마리메뉴")))
                .andExpect(jsonPath("$[3]['name']", is("신메뉴")));
    }
}