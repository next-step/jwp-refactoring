package kitchenpos.ui;

import com.fasterxml.jackson.databind.ObjectMapper;
import kitchenpos.application.ProductService;
import kitchenpos.domain.Product;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private ProductService productService;

    @DisplayName("상품을 등록한다.")
    @Test
    void create() throws Exception {
        Product product = new Product(1L, "product", new BigDecimal(10_000));
        when(productService.create(any())).thenReturn(product);

        mockMvc.perform(post("/api/products")
            .content(objectMapper.writeValueAsString(product)).contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(redirectedUrl("/api/products/" + product.getId()))
            .andExpect(status().isCreated());
    }

    @DisplayName("상품 목록을 조회한다.")
    @Test
    void list() throws Exception {
        Product product1 = new Product("product1", new BigDecimal(10_000));
        Product product2 = new Product("product2", new BigDecimal(10_000));

        when(productService.list()).thenReturn(Arrays.asList(product1, product2));

        mockMvc.perform(get("/api/products").contentType(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$", hasSize(2)))
            .andExpect(jsonPath("$[*].name", Matchers.containsInAnyOrder("product1", "product2")))
            .andExpect(jsonPath("$[*].price", Matchers.containsInAnyOrder(10_000, 10_000)));
    }

}
