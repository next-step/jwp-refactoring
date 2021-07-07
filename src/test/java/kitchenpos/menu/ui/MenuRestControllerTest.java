package kitchenpos.menu.ui;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
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

import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import kitchenpos.ui.MenuRestController;
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
    private Menu menu1;

    @BeforeEach
    void setUp() {
        product1 = new Product();
        product1.setId(1L);
        product1.setPrice(BigDecimal.valueOf(16000.00));
        product1.setName("후라이드");
        menu1 = new Menu();
        menu1.setId(1L);
        menu1.setName("후라이드치킨");
        menu1.setPrice(BigDecimal.valueOf(16000.00));
        menu1.setMenuGroupId(2L);
        menu1.setMenuProducts(new ArrayList(Arrays.asList(product1)));
    }

    @Override
    protected Object controller() {
        return menuRestController;
    }

    @Test
    @DisplayName("메뉴 목록을 조회할 수 있다.")
    void retrieve_menuList() throws Exception {
        // given
        when(menuService.list()).thenReturn(new ArrayList(Arrays.asList(menu1)));

        // then
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("[0].id").value(menu1.getId()))
                .andExpect(jsonPath("[0].name").value(menu1.getName()))
                .andExpect(jsonPath("[0].price").value(menu1.getPrice()))
        ;
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    void save_menu() throws Exception {
        // given
        when(menuService.create(any(Menu.class))).thenReturn(menu1);

        // then
        mockMvc.perform(post(REQUEST_URL).contentType(MediaType.APPLICATION_JSON).content(new ObjectMapper().writeValueAsString(menu1)))
                .andDo(print())
                .andExpect(jsonPath("id").value(menu1.getId()))
                .andExpect(jsonPath("name").value(menu1.getName()))
                .andExpect(jsonPath("price").value(menu1.getPrice()))
                .andExpect(jsonPath("menuProducts.[0].id").value(product1.getId()))
        ;
    }
}
