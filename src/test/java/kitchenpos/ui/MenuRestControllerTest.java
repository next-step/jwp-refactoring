package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
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
import static org.junit.jupiter.api.Assertions.*;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private MenuService menuService;

    @DisplayName("메뉴를 등록한다")
    @Test
    void create() throws Exception {
        Menu menu = new Menu(1L, "menu", BigDecimal.valueOf(10_000), 1L);
        when(menuService.create(any())).thenReturn(menu);

        mockMvc.perform(post("/api/menus")
            .content(objectMapper.writeValueAsString(menu)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(redirectedUrl("/api/menus/" + menu.getId()))
            .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() throws Exception {
        Menu menu1 = new Menu(1L, "menu1", BigDecimal.valueOf(10_000), 1L);
        Menu menu2 = new Menu(2L, "menu2", BigDecimal.valueOf(10_000), 1L);

        when(menuService.list()).thenReturn(Arrays.asList(menu1, menu2));

        mockMvc.perform(get("/api/menus"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("menu1", "menu2")))
            .andExpect(jsonPath("$[*].price", Matchers.containsInAnyOrder(10_000, 10_000)));
    }


}
