package kitchenpos.ui;

import kitchenpos.common.BaseControllerTest;
import kitchenpos.dto.ProductRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static kitchenpos.common.Fixtures.productRequest;
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
        ProductRequest productRequest = productRequest().build();

        // when & then
        mockMvc.perform(post("/api/products")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").isNotEmpty())
                .andExpect(jsonPath("name").value(productRequest.getName()))
                .andExpect(jsonPath("price").value(productRequest.getPrice()));
    }

    void 상품_추가됨_목록_조회() throws Exception {
        mockMvc.perform(get("/api/products")
                .contentType(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(7)));
    }
}
