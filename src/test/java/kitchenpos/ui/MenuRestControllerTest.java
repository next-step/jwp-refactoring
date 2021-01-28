package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.MenuService;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(MenuRestController.class)
class MenuRestControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private MenuService menuService;

    @Test
    @DisplayName("메뉴 생성 확인")
    public void whenPostMenu_thenReturnStatus() throws Exception {

        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal(19000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct));

        when(menuService.create(any())).thenReturn(menu);

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
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(1L);
        menuProduct.setQuantity(2L);
        Menu menu = new Menu();
        menu.setId(1L);
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal(19000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(Arrays.asList(menuProduct));

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
