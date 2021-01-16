package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import kitchenpos.dto.ProductCreateRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

/**
 * @author : leesangbae
 * @project : kitchenpos
 * @since : 2021-01-09
 */
class ProductRestControllerTest extends BaseControllerTest {

    @DisplayName("상품 생성 테스트")
    @Test
    void productCreateTest() throws Exception {

        ProductCreateRequest request = new ProductCreateRequest("테스트", BigDecimal.valueOf(30_000));

        mockMvc.perform(post("/api/products")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").exists())
                .andExpect(jsonPath("name").value("테스트"))
                .andExpect(jsonPath("price").value(30_000));
    }

    @DisplayName("상품 조회 테스트")
    @Test
    void productSelectTest() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andDo(print())
                .andExpect(status().isOk());
    }
}
