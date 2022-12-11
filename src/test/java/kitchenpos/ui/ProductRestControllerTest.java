package kitchenpos.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import kitchenpos.domain.Product;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

class ProductRestControllerTest extends BaseTest {
    @Test
    void 생성() throws Exception {
        String content = objectMapper.writeValueAsString(new Product("후라이드", BigDecimal.valueOf(16000)));

        생성_요청(content);
    }

    @Test
    void 조회() throws Exception {
        조회_요청();
    }

    private void 생성_요청 (String content) throws Exception {
        mockMvc.perform(post("/api/products")
                        .content(content)
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated())
                .andDo(print());
    }

    private void 조회_요청 () throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andDo(print());
    }
}
