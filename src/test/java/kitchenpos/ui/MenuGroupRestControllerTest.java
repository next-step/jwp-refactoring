package kitchenpos.ui;

import kitchenpos.menugroup.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 그룹 Controller Test")
class MenuGroupRestControllerTest extends RestControllerTest {

    public static final String MENU_GROUPS_URL = "/api/menu-groups";

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() throws Exception {
        //given
        MenuGroup menuGroup = new MenuGroup(null, "일반메뉴");

        //when
        //then
        mockMvc.perform(
                post(MENU_GROUPS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(menuGroup))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrlPattern("/api/menu-groups/*"))
                .andExpect(jsonPath("$.name", is("일반메뉴")));
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
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
