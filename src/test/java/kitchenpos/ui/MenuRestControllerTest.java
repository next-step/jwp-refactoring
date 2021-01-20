package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.dto.MenuRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Collections;

import static kitchenpos.common.Fixtures.menuProductRequest;
import static kitchenpos.common.Fixtures.menuRequest;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("메뉴 관련 기능")
public class MenuRestControllerTest extends BaseControllerTest {

    @DisplayName("메뉴 관리")
    @Test
    void testManageMenu() throws Exception {
        메뉴_등록();

        메뉴_추가됨_목록_조회();
    }

    @DisplayName("존재하지 않는 메뉴그룹으로 메뉴를 등록한다")
    @Test
    void testCreateMenu_withNonExistentMenuGroup() throws Exception {
        // given
        MenuRequest menuRequest = menuRequest()
                .menuGroupId(0L)
                .build();

        // when & then
        mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("존재하지 않는 상품으로 메뉴를 등록한다")
    @Test
    void testCreateMenu_withNonExistentProduct() throws Exception {
        // given
        MenuRequest menuRequest = menuRequest()
                .menuProducts(Collections.singletonList(menuProductRequest().productId(0L).build()))
                .build();

        // when & then
        mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuRequest)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @DisplayName("상품 가격 합계보다 비싼 메뉴를 등록한다")
    @Test
    void testCreateMenu_withMoreExpensiveProductTotalPrice() throws Exception {
        // given
        MenuRequest menuRequest = menuRequest()
                .price(BigDecimal.valueOf(100_000))
                .build();

        // when & then
        mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    void 메뉴_등록() throws Exception {
        // given
        MenuRequest menuRequest = menuRequest().build();

        // when & then
        mockMvc.perform(post("/api/menus")
                .contentType(APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(menuRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value(menuRequest.getName()))
                .andExpect(jsonPath("$.price").value(menuRequest.getPrice().intValue()))
                .andExpect(jsonPath("$.menuGroupId").value(menuRequest.getMenuGroupId()))
                .andExpect(jsonPath("$.menuProducts", hasSize(1)))
                .andExpect(jsonPath("$.menuProducts[0].productId")
                        .value(menuRequest.getMenuProducts().get(0).getProductId()))
                .andExpect(jsonPath("$.menuProducts[0].quantity")
                        .value(menuRequest.getMenuProducts().get(0).getQuantity()));
    }

    void 메뉴_추가됨_목록_조회() throws Exception {
        mockMvc.perform(get("/api/menus")
                .contentType(APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }
}
