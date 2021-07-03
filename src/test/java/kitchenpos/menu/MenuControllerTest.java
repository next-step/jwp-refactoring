package kitchenpos.menu;

import kitchenpos.common.ControllerTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.web.util.NestedServletException;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends ControllerTest {

    @Test
    @DisplayName("메뉴를 생성 한다")
    public void createMenu() throws Exception {
        String name = "후라이드치킨";
        BigDecimal price = new BigDecimal(16000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1L));
        Menu menu = new Menu(name, price, 1L, menuProducts);

        메뉴_생성_요청(menu)
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value(name))
                .andExpect(jsonPath("price").isNumber())
        ;
    }


    @Test
    @DisplayName("메뉴 생성 실패 - 가격이 음수")
    public void createMenuFailByPriceMinus() {
        String name = "불고기피자";
        BigDecimal price = new BigDecimal(-10000);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct(1L, 1L, 1L));
        Menu menu = new Menu(name, price, 1L, menuProducts);

        assertThrows(NestedServletException.class, () -> 메뉴_생성_요청(menu));
    }

    @Test
    @DisplayName("메뉴 리스트를 가져온다")
    public void selectMenuList() throws Exception {
        메뉴_리스트_요청()
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)))
                .andExpect(jsonPath("$.[0].id").value(1L))
                .andExpect(jsonPath("$.[0].name").value("후라이드치킨"))
        ;
    }

    private ResultActions 메뉴_생성_요청(Menu menu) throws Exception {
        return mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu)))
                .andDo(print());
    }

    private ResultActions 메뉴_리스트_요청() throws Exception {
        return mockMvc.perform(get("/api/menus")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
