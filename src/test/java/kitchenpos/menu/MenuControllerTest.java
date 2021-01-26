package kitchenpos.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.common.BaseContollerTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.Product;
import kitchenpos.product.ProductTest;
import kitchenpos.product.ProductTestSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class MenuControllerTest extends BaseContollerTest {

    @Test
    @DisplayName("새로운 메뉴를 등록합니다.")
    void createMenu() throws Exception {
        Menu menu = MenuTestSupport.createMenu("더 맛있는 후라이드 치킨", 20000, 2L);
        MenuTestSupport.addMenuGroup(menu, 1L, 3);

        this.mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(menu)
                ))
                .andDo(print())
                .andExpect(status().isCreated())
        ;
    }

    @Test
    @DisplayName("모든 메뉴 목록을 조회합니다.")
    void getProducts() throws Exception {
        MvcResult mvcResult = this.mockMvc.perform(get("/api/menus")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath(".id").exists())
                .andReturn();

        String responseProducts = mvcResult.getResponse().getContentAsString();
        ArrayList<Product> products
                = this.objectMapper.readValue(responseProducts, new TypeReference<ArrayList<Product>>() {});

        assertThat(products).hasSize(6);
    }
}
