package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuGroupService;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
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
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    private MockMvc mvc;

    @Autowired
    private WebApplicationContext ctx;

    @MockBean
    private MenuService menuService;

    @BeforeEach
    void setup() {
        this.mvc = MockMvcBuilders.webAppContextSetup(ctx)
                .addFilters(new CharacterEncodingFilter("UTF-8", true))
                .alwaysDo(print())
                .build();
    }

    @DisplayName("메뉴를 등록한다.")
    @Test
    void create() throws Exception {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final Menu menu = new Menu(1L, "메뉴", new BigDecimal("10000"), 1L, Collections.singletonList(menuProduct));
        given(menuService.create(any())).willReturn(menu);

        // when
        final ResultActions actions = mvc.perform(post("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .accept(MediaType.APPLICATION_JSON_VALUE)
                        .content(new ObjectMapper().writeValueAsString(menu)))
                .andDo(print());
        //then
        actions.andExpect(status().isCreated())
                .andExpect(header().stringValues("Location", "/api/menus/1"))
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("name").value("메뉴"))
                .andExpect(jsonPath("price").value(new BigDecimal("10000")))
                .andExpect(jsonPath("menuGroupId").value(1L))
                .andExpect(jsonPath("menuProducts").isArray());
    }

    @DisplayName("메뉴를 조회한다.")
    @Test
    void list() throws Exception {
        // given
        final MenuProduct menuProduct = new MenuProduct(1L, 1L, 1L, 2);
        final Menu menu = new Menu(1L, "메뉴", new BigDecimal("10000"), 1L, Collections.singletonList(menuProduct));
        given(menuService.list()).willReturn(Collections.singletonList(menu));

        // when
        final ResultActions actions = mvc.perform(get("/api/menus")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print());

        //then
        actions.andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.valueOf("application/json;charset=UTF-8")))
                .andExpect(content().string(containsString("메뉴")));
    }
}
