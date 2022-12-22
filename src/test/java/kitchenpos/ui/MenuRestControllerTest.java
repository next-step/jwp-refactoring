package kitchenpos.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest extends ControllerTest {
    @MockBean
    private MenuService menuService;

    private Menu lunchMenu;
    private Menu dinnerMenu;

    @Override
    @BeforeEach
    protected void setUp() {
        super.setUp();

        lunchMenu  = new Menu(1L, "lunch-menu",  BigDecimal.ONE, 1L, Collections.singletonList(new MenuProduct(1L, 1)));
        dinnerMenu = new Menu(2L, "dinner-menu", BigDecimal.TEN, 2L, Collections.singletonList(new MenuProduct(2L, 1)));
    }

    @DisplayName("[POST] 메뉴 생성")
    @Test
    void create() throws Exception {
        given(menuService.create(any(Menu.class))).willReturn(lunchMenu);

        perform(postAsJson("/api/menus", lunchMenu))
            .andExpect(status().isCreated())
            .andExpect(header().string("location", "/api/menus/1"))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$.id").value(1L))
            .andExpect(jsonPath("$.name").value("lunch-menu"))
            .andExpect(jsonPath("$.price").value(BigDecimal.ONE))
            .andExpect(jsonPath("$.menuGroupId").value(1L));
    }

    @DisplayName("[GET] 메뉴 목록 조회")
    @Test
    void list() throws Exception {
        given(menuService.list()).willReturn(Arrays.asList(lunchMenu, dinnerMenu));

        perform(get("/api/menus"))
            .andExpect(status().isOk())
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$").isNotEmpty())
            .andExpect(jsonPath("$[0].id").value(1L))
            .andExpect(jsonPath("$[0].name").value("lunch-menu"))
            .andExpect(jsonPath("$[0].price").value(BigDecimal.ONE))
            .andExpect(jsonPath("$[0].menuGroupId").value(1L))
            .andExpect(jsonPath("$[1].id").value(2L))
            .andExpect(jsonPath("$[1].name").value("dinner-menu"))
            .andExpect(jsonPath("$[1].price").value(BigDecimal.TEN))
            .andExpect(jsonPath("$[1].menuGroupId").value(2L));
    }
}
