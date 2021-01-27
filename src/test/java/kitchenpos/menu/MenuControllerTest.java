package kitchenpos.menu;

import com.fasterxml.jackson.core.type.TypeReference;
import kitchenpos.common.BaseContollerTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuProduct;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.util.NestedServletException;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
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

        메뉴_생성_요청(menu, status().isCreated())
        ;
    }

    @Test
    @DisplayName("메뉴의 가격이 없는 경우 등록 시 오류가 발생합니다.")
    void createMenuNoPriceOccurredError() {
        Menu menu = MenuTestSupport.createMenu("더 맛있는 후라이드 치킨", 20000, 2L);
        menu.setPrice(null);
        MenuTestSupport.addMenuGroup(menu, 1L, 3);

        assertThatThrownBy(() -> {
            메뉴_생성_요청(menu, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("IllegalArgumentException");

    }

    @Test
    @DisplayName("메뉴의 그룹이 없는 경우 등록 시 오류가 발생합니다.")
    void createMenuNoMenuGroupOccurredError() {
        Menu menu = MenuTestSupport.createMenu("더 맛있는 후라이드 치킨", 20000, 2L);
        menu.setMenuGroupId(null);
        MenuTestSupport.addMenuGroup(menu, 1L, 3);

        assertThatThrownBy(() -> {
            메뉴_생성_요청(menu, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("IllegalArgumentException");
    }

    @Test
    @DisplayName("메뉴에 등록되지 않은 상품이 있는 경우 등록 시 오류가 발생합니다.")
    void createMenuNoRegistProductOccurredError() {
        Menu menu = MenuTestSupport.createMenu("더 맛있는 후라이드 치킨", 20000, 2L);
        List<MenuProduct> menuProducts = new ArrayList<>();
        menuProducts.add(new MenuProduct());
        menu.setMenuProducts(menuProducts);
        MenuTestSupport.addMenuGroup(menu, 1L, 3);

        assertThatThrownBy(() -> {
            메뉴_생성_요청(menu, status().is5xxServerError());
        }).isInstanceOf(NestedServletException.class).hasMessageContaining("IllegalArgumentException");
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

    private ResultActions 메뉴_생성_요청(Menu menu, ResultMatcher created) throws Exception {
        return this.mockMvc.perform(post("/api/menus")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(menu)
                ))
                .andDo(print())
                .andExpect(created);
    }

}
