package kitchenpos.ui;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@DisplayName("메뉴 Controller Test")
class MenuRestControllerTest extends RestControllerTest {

    public static final String MENUS_URL = "/api/menus";

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void create() throws Exception {
        //given
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(null, 1L, 1));
        menuProducts.add(new MenuProduct(null, 2L, 1));

        Menu menu = new Menu(null, "일반메뉴", new BigDecimal(13000), 2L, menuProducts);

        //when
        //then
        mockMvc.perform(
                post(MENUS_URL)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(toJsonString(menu))
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(redirectedUrlPattern(MENUS_URL + "/*"))
                .andExpect(jsonPath("$.name", is("일반메뉴")))
                .andExpect(jsonPath("$.menuGroupId", is(2)))
                .andExpect(jsonPath("$.menuProducts.length()", is(2)));
    }

    @DisplayName("메뉴 목록을 조회할 수 있다.")
    @Test
    void list() throws Exception {
        mockMvc.perform(get(MENUS_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()", is(6)))
                .andExpect(jsonPath("$[0]['id']", is(1)))
                .andExpect(jsonPath("$[0]['name']", is("후라이드치킨")))
                .andExpect(jsonPath("$[1]['id']", is(2)))
                .andExpect(jsonPath("$[1]['name']", is("양념치킨")))
                .andExpect(jsonPath("$[2]['id']", is(3)))
                .andExpect(jsonPath("$[2]['name']", is("반반치킨")))
                .andExpect(jsonPath("$[3]['id']", is(4)))
                .andExpect(jsonPath("$[3]['name']", is("통구이")))
                .andExpect(jsonPath("$[4]['id']", is(5)))
                .andExpect(jsonPath("$[4]['name']", is("간장치킨")))
                .andExpect(jsonPath("$[5]['id']", is(6)))
                .andExpect(jsonPath("$[5]['name']", is("순살치킨")));
    }
}