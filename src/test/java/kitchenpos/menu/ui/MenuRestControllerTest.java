package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menu.ui.MenuRestController;
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
import java.util.Collection;
import java.util.Collections;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
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
        MenuResponse menuResponse = new MenuResponse(1L, "menu", BigDecimal.valueOf(10_000), 1L,
            Arrays.asList());
        when(menuService.create(any())).thenReturn(menuResponse);

        mockMvc.perform(post("/api/menus")
            .content(objectMapper.writeValueAsString(menuResponse)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(redirectedUrl("/api/menus/" + menuResponse.getId()))
            .andExpect(status().isCreated());
    }

    @DisplayName("메뉴 목록을 조회한다.")
    @Test
    void list() throws Exception {
        MenuResponse menu1 = new MenuResponse(1L, "menu1", BigDecimal.valueOf(10_000), 1L, Collections.EMPTY_LIST);
        MenuResponse menu2 = new MenuResponse(2L, "menu2", BigDecimal.valueOf(10_000), 1L, Collections.EMPTY_LIST);

        when(menuService.list()).thenReturn(Arrays.asList(menu1, menu2));

        mockMvc.perform(get("/api/menus"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("menu1", "menu2")))
            .andExpect(jsonPath("$[*].price", Matchers.containsInAnyOrder(10_000, 10_000)));
    }


}
