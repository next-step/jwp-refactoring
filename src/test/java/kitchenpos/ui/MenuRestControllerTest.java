package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dto.MenuDto;
import kitchenpos.dto.MenuProductDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-08
 */
class MenuRestControllerTest extends BaseControllerTest {

    @DisplayName("메뉴 생성")
    @Test
    public void menuGroupCreateTest() throws Exception {

        MenuDto menu = getMenu();

        mockMvc.perform(
                post("/api/menus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(menu)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value("메뉴"));
    }

    @DisplayName("메뉴 조회")
    @Test
    public void menuGroupSelectTest() throws Exception {

        mockMvc.perform(get("/api/menus")
                        .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk());
    }

    private MenuDto getMenu() {
        MenuProductDto menuProduct = new MenuProductDto();
        menuProduct.setProductId(6L);
        menuProduct.setQuantity(1);
        List<MenuProductDto> menuProducts = Collections.singletonList(menuProduct);

        MenuDto menu = new MenuDto();
        menu.setName("메뉴");
        menu.setPrice(BigDecimal.valueOf(17_000));
        menu.setMenuGroupId(1L);
        menu.setMenuProducts(menuProducts);

        return menu;
    }

}
