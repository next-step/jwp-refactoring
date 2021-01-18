package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import java.math.BigDecimal;

import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@DisplayName("상품 관련 기능")
public class ProductRestControllerTest extends BaseControllerTest {

    @DisplayName("상품 관리")
    @Test
    void testManageProduct() throws Exception {
        상품_등록();

        상품_추가됨_목록_조회();
    }

    void 상품_등록() throws Exception {
        // given
        String name = "강정치킨";
        int price = 17000;
        Product product = new Product();
        product.setName(name);
        product.setPrice(new BigDecimal(price));

        // when & then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(name))
                .andExpect(jsonPath("price").value(price));
    }

    void 상품_추가됨_목록_조회() throws Exception {
        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }
}
