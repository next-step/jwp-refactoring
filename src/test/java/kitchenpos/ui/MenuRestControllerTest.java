package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import kitchenpos.dto.MenuCreateRequest;
import kitchenpos.dto.MenuProductRequest;
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

        MenuProductRequest menuProduct = new MenuProductRequest(6L, 1);
        List<MenuProductRequest> menuProducts = Collections.singletonList(menuProduct);

        MenuCreateRequest menuRequest = new MenuCreateRequest("메뉴", BigDecimal.valueOf(17_000), 1L, menuProducts);

        mockMvc.perform(
                post("/api/menus")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(menuRequest)))
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

}
