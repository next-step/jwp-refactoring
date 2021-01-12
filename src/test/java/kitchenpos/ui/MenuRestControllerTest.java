package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 등록할 수 있다.")
    @Test
    void createMenu() throws Exception {
        Menu 후라이드_양념 = getMenu(1L, "후라이드+양념", 30000, 1L,
                new MenuProduct(1L, 1),
                new MenuProduct(2L, 1));
        when(menuService.create(any())).thenReturn(후라이드_양념);

        mockMvc.perform(post("/api/menus")
                .content(objectMapper.writeValueAsString(후라이드_양념)).contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(redirectedUrl("/api/menus/" + 후라이드_양념.getId()))
                .andExpect(status().isCreated());
    }

    @DisplayName("매뉴의 목록을 조회할 수 있다.")
    @Test
    void getMenus() throws Exception {
        Menu 후라이드_양념 = getMenu(1L, "후라이드+양념", 30000, 1L,
                new MenuProduct(1L, 1), new MenuProduct(2L, 1));

        Menu 두마리_메뉴 = getMenu(2L, "두마리메뉴", 35000, 1L,
                new MenuProduct(3L, 1),
                new MenuProduct(4L, 1));

        when(menuService.list()).thenReturn(Arrays.asList(후라이드_양념, 두마리_메뉴));

        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("후라이드+양념", "두마리메뉴")))
                .andExpect(jsonPath("$[*].price", Matchers.containsInAnyOrder(30000, 35000)));
    }

    private Menu getMenu(long menuId, String menuName, int price, long menuGroupId, MenuProduct... menuProducts) {
        return new Menu(menuId, menuName, new BigDecimal(price), menuGroupId,
                Arrays.asList(menuProducts));
    }
}