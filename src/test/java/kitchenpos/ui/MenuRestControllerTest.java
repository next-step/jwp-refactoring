package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * packageName : kitchenpos.ui
 * fileName : MenuRestControllerTest
 * author : haedoang
 * date : 2021-12-15
 * description :
 */
@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    private Menu menu;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        menu = new Menu();
        menu.setId(1L);
        menu.setName("양념치킨");
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(new MenuProduct(), new MenuProduct()));
    }


    @Test
    @DisplayName("메뉴를 조회한다.")
    public void getMenus() throws Exception {
        // given
        List<Menu> menus = Arrays.asList(menu);
        given(menuService.list()).willReturn(menus);

        // when
        ResultActions actions = mockMvc.perform(
                get("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
        ).andDo(print());

        // then
        actions.andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name", is("양념치킨")))
                .andExpect(jsonPath("$[0].menuProducts", hasSize(2)))
                .andDo(print());
    }


    @Test
    @DisplayName("메뉴를 등록한다.")
    public void postMenu() throws Exception{
        // given
        ObjectMapper mapper = new ObjectMapper();
        given(menuService.create(any(Menu.class))).willReturn(menu);

        // when
        ResultActions actions = mockMvc.perform(
                post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(menu))
        ).andDo(print());

        // then
        actions.andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.name", is(menu.getName())))
                .andExpect(jsonPath("$.menuProducts", hasSize(2)))
                .andDo(print());
    }
}