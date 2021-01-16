package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.common.DefaultData.메뉴그룹_두마리메뉴_ID;
import static kitchenpos.common.DefaultData.상품_후라이드_ID;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메뉴 관련 기능")
public class MenuRestControllerTest extends BaseControllerTest {

    @DisplayName("메뉴 등록")
    @Test
    void testCreateMenu() throws Exception {
        // given
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.setProductId(상품_후라이드_ID);
        menuProduct.setQuantity(2);

        Menu menu = new Menu();
        menu.setName("후라이드+후라이드");
        menu.setPrice(new BigDecimal(19000));
        menu.setMenuGroupId(메뉴그룹_두마리메뉴_ID);
        menu.setMenuProducts(Collections.singletonList(menuProduct));

        // when & then
        mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menu)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(menu.getName()))
                .andExpect(jsonPath("$.price").value(menu.getPrice().intValue()))
                .andExpect(jsonPath("$.menuGroupId").value(menu.getMenuGroupId()))
                .andExpect(jsonPath("$.menuProducts", hasSize(1)))
                .andExpect(jsonPath("$.menuProducts[0].productId").value(상품_후라이드_ID))
                .andExpect(jsonPath("$.menuProducts[0].quantity").value(menuProduct.getQuantity()));
    }

    @DisplayName("메뉴 목록 조회")
    @Test
    void testGetMenus() throws Exception {
        mockMvc.perform(get("/api/menus")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(6)));
    }
}
