package kitchenpos.menu.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.product.domain.Product;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("메뉴 관리 기능")
@WebMvcTest(controllers = MenuRestController.class)
class MenuRestControllerTest extends MockMvcControllerTest {
    private static final String REQUEST_URL = "/api/menus";

    @MockBean
    private MenuService menuService;

    @Autowired
    private MenuRestController menuRestController;

    private Product product1;
    private Product product2;
    private MenuGroup menuGroup;
    private Menu menu;
    private MenuProduct menuProduct1;
    private MenuProduct menuProduct2;

    @Override
    protected Object controller() {
        return menuRestController;
    }

    @BeforeEach
    void setUp() {
        product1 = new Product("A", BigDecimal.valueOf(1000));
        product2 = new Product("B", BigDecimal.valueOf(2000));
        menuGroup = new MenuGroup("1");
        menu = new Menu("AB", BigDecimal.valueOf(3000), menuGroup);
        menuProduct1 = new MenuProduct(menu, product1, 1L);
        menuProduct2 = new MenuProduct(menu, product1, 1L);
        menu.addMenuProduct(menuProduct1);
        menu.addMenuProduct(menuProduct2);
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void retrieve_menuList1() throws Exception {
        // given
        MenuResponse menuResponse = MenuResponse.of(menu);
        given(menuService.findAllMenu()).willReturn(Arrays.asList(menuResponse));

        // then
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(1))
                .andExpect(jsonPath("[0].name").value(menu.getName()))
                .andExpect(jsonPath("[0].menuProductResponses.length()").value(2))
        ;
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void save_menu1() throws Exception {
        // given
        MenuRequest menuRequest = new MenuRequest("A", null, 1L, new ArrayList<>());
        MenuGroup menuGroup = new MenuGroup("AB");
        Menu menu = new Menu("A", BigDecimal.valueOf(20000.00), menuGroup);
        menu.addMenuProduct(new MenuProduct(menu, new Product("a", BigDecimal.valueOf(15000.00)), 1));
        menu.addMenuProduct(new MenuProduct(menu, new Product("a", BigDecimal.valueOf(15000.00)), 1));
        MenuResponse menuResponse = MenuResponse.of(menu);
        given(menuService.create(any(MenuRequest.class))).willReturn(menuResponse);

        // then
        mockMvc.perform(post(REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(menuRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value(menuResponse.getName()))
                .andExpect(jsonPath("price").value(menuResponse.getPrice()))
                .andExpect(jsonPath("menuProductResponses.length()").value(2))
        ;
    }
}
