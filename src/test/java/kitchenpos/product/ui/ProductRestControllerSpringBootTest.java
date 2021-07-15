package kitchenpos.product.ui;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;

import com.fasterxml.jackson.databind.ObjectMapper;

import kitchenpos.product.dto.ProductRequest;
import kitchenpos.utils.MockMvcControllerTest;

@DisplayName("상품 관리 기능")
@SpringBootTest
class ProductRestControllerSpringBootTest extends MockMvcControllerTest {
    private static final String REQUEST_URL = "/api/products";
    @Autowired
    private ProductRestController productRestController;

    @Override
    protected Object controller() {
        return productRestController;
    }

    @Test
    @DisplayName("상품 목록을 조회한다.")
    void retrieve_productList() throws Exception {
        // when
        mockMvc.perform(get(REQUEST_URL))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("length()").value(6))
        ;
    }

    @Test
    @DisplayName("상품을 등록할 수 있다.")
    void save_product() throws Exception {
        // then
        mockMvc.perform(post(REQUEST_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(new ProductRequest("A", BigDecimal.valueOf(10_000.00)))))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("name").value("A"))
                .andExpect(jsonPath("price").value(BigDecimal.valueOf(10_000.00)))
        ;
    }
}
