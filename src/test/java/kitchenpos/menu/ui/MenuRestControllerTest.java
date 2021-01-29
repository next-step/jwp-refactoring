package kitchenpos.menu.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    private MenuResponse menuResponse;
    @Test
    @DisplayName("메뉴 생성 확인")
    public void whenPostMenu_thenReturnStatus() throws Exception {
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        Product product = new Product("닭강정", new BigDecimal(17000));
        Menu menu = new Menu("후라이드+후라이드", new BigDecimal(34000), menuGroup);
        MenuProduct menuProduct = new MenuProduct(menu, product, 2);

        when(menuService.create(any())).thenReturn(menuResponse.of(menu));

        mockMvc.perform(post("/api/menus")
                .content(asJsonString(menu))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    @Test
    @DisplayName("메뉴 생성 조회")
    public void givenMenu_whenGetMenu_thenReturnStatus() throws Exception {
        MenuGroup menuGroup = new MenuGroup("추천메뉴");
        Product product = new Product("닭강정", new BigDecimal(17000));
        Menu menu = new Menu("후라이드+후라이드", new BigDecimal(34000), menuGroup);
        MenuProduct menuProduct = new MenuProduct(menu, product, 2);

        given(menuService.list()).willReturn(Arrays.asList(menu));

        mockMvc.perform(get("/api/menus"))
                .andExpect(status().isOk())
                .andDo(print());
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
