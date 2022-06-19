package kitchenpos.ui;

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class MenuRestControllerTest extends BaseRestController {

    @Mock
    private MenuService menuService;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(new MenuRestController(menuService)).build();
    }

    @Test
    void create() throws Exception {
        //given
        String name = "menu";
        BigDecimal price = BigDecimal.valueOf(1000);
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = Arrays.asList(createMenuProduct());
        Menu request = new Menu(name, price, menuGroupId, menuProducts);
        String requestBody = objectMapper.writeValueAsString(request);

        Long id = 1L;
        given(menuService.create(any())).willReturn(new Menu(id, name, price, menuGroupId, menuProducts));

        //when //then
        mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)
        )
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id));
    }

    @Test
    void list() throws Exception {
        //given
        Long id = 1L;
        String name = "menu";
        BigDecimal price = BigDecimal.valueOf(1000);
        Long menuGroupId = 1L;
        List<MenuProduct> menuProducts = Arrays.asList(createMenuProduct());

        given(menuService.list()).willReturn(Arrays.asList(new Menu(id, name, price, menuGroupId, menuProducts)));

        //when //then
        mockMvc.perform(get("/api/menus"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value(1));
    }

    private MenuProduct createMenuProduct() {
        return new MenuProduct(1L, 1L, 1);
    }
}